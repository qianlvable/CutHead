/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.ObjectMetaData;
import com.aliyun.android.oss.xmlparser.CopyObjectXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 复制Object的任务
 * 
 * @author Michael
 */
public class CopyObjectTask extends Task {
    /**
     * 目标object
     */
    private String destObjectKey;

    /**
     * 源bucket
     */
    private String srcBucketName;

    /**
     * 源object
     */
    private String srcObjectKey;

    /**
     * x-oss Headers, 不对外开放setter, getter，只开放固定几种header的添加
     */
    private Map<String, String> xossHeaders;

    /**
     * x-oss 请求Header常量
     */
    private static final String X_OSS_COPY_SOURCE = "x-oss-copy-source";

    private static final String X_OSS_COPY_SOURCE_IF_MATCH = "x-oss-copy-source-if-match";

    private static final String X_OSS_COPY_SOURCE_IF_NONE_MATCH = "x-oss-copy-source-if-none-match";

    private static final String X_OSS_COPY_SOURCE_IF_UNMODIFIED_SINCE = "x-oss-copy-source-if-unmodified-since";

    private static final String X_OSS_COPY_SOURCE_IF_MODIFIED_SINCE = "x-oss-copy-source-if-modified-since";

    private static final String X_OSS_METADATA_DIRECTIVE = "x-oss-metadata-directive";

    /** 
     * 初始化一个CopyObjectTask实例
     * 
     * @param srcBucket 源Object所在的Bucket的名称
     * @param srcObj 源Object的Key
     * @param destBucket 目标Object所在的Bucket的名称
     * @param destObj 目标Object的Key
     */
    public CopyObjectTask(String srcBucket, String srcObj, String destBucket,
            String destObj) {
        super(HttpMethod.PUT, destBucket);
        this.srcBucketName = srcBucket;
        this.srcObjectKey = srcObj;
        this.destObjectKey = destObj;
        xossHeaders = new HashMap<String, String>();
        xossHeaders.put(X_OSS_COPY_SOURCE, "/" + srcBucketName + "/"
                + srcObjectKey);
    }

    /**
     * 参数合法性检测
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(srcBucketName)
                || Helper.isEmptyString(srcObjectKey)) {
            throw new IllegalArgumentException("source object not properly set");
        }
        if (Helper.isEmptyString(bucketName)
                || Helper.isEmptyString(destObjectKey)) {
            throw new IllegalArgumentException("dest object not properly set");
        }
    }

    /**
     * 设置source object
     */
    public void setSource(String srcBucket, String srcObj) {
        this.srcBucketName = srcBucket;
        this.srcObjectKey = srcObj;
        String source = "/" + srcBucket + "/" + srcObj;
        if (!Helper.isEmptyString(source)) {
            xossHeaders.put(X_OSS_COPY_SOURCE, source);
        }
    }

    /**
     * 设置ETag匹配限定值，可选。如果源Object的ETag值和用户提供的ETag限定值相等，则执行拷贝操作。否则抛出异常
     * @param matchETag ETag匹配限定值
     */
    public void addXossCopySrcIfMatch(String matchETag) {
        if (!Helper.isEmptyString(matchETag)) {
            xossHeaders.put(X_OSS_COPY_SOURCE_IF_MATCH, matchETag);
        }
    }

    /**
     * 设置ETag不匹配限定值，可选。如果源Object的ETag值和用户提供的ETag限定值不相等，则执行拷贝操作。否则抛出异常
     * @param notMatchETag ETag不匹配限定值
     */
    public void addXossCopySrcIfNotMatch(String notMatchETag) {
        if (!Helper.isEmptyString(notMatchETag)) {
            xossHeaders.put(X_OSS_COPY_SOURCE_IF_NONE_MATCH, notMatchETag);
        }
    }

    /**
     * 设置一个时间，如果该时间等于或者晚于文件实际修改时间，则正常传输文件； 否则抛出异常。可选。
     * @param date
     */
    public void addXossCopySrcIfUnModifiedSince(Date date) {
        String datestr = Helper.getGMTDate(date);
        if (!Helper.isEmptyString(datestr)) {
            xossHeaders.put(X_OSS_COPY_SOURCE_IF_UNMODIFIED_SINCE, datestr);
        }
    }

    /**
     * 设置返回一个时间，如果源Object自从该时间以后被修改过，则执行拷贝操作； 否则抛出异常。可选。
     * @param date
     */
    public void addXossCopySrcIfModifedSince(Date date) {
        String datestr = Helper.getGMTDate(date);
        if (!Helper.isEmptyString(datestr)) {
            xossHeaders.put(X_OSS_COPY_SOURCE_IF_MODIFIED_SINCE, datestr);
        }
    }

    /**
     * 如果值为<code>{@link MetaDirective.COPY}</code>，则新的Object的Meta信息都从源Object复制过来<br/>
     * 如果值为<code>{@link MetaDirective.REPLACE}</code>，则忽视所有源Object的Meta值，而采用用户这次请求中指定的meta值
     * 
     * @param directive
     * @see MetaDirective
     */
    public void addXossmetaDirective(MetaDirective directive) {
        if (directive != null) {
            xossHeaders.put(X_OSS_METADATA_DIRECTIVE, directive.toString());
        }
    }

    /**
     * MetaDataDirective枚举
     * 
     * @author Michael
     */
    public enum MetaDirective {
        COPY("COPY"), REPLACE("REPLACE");
        private String context;

        private MetaDirective(String context) {
            this.context = context;
        }

        public String toString() {
            return this.context;
        }
    }

    /**
     * @return 返回目标{@link OSSObject}对象，其Metadata只有<code>lastModified</code>和<code>eTag</code>两个域的值存在，只可以调用响应的get方法。
    /* @throws OSSException
     * @see OSSObject
     */
    public OSSObject getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            CopyObjectXmlParser parser = new CopyObjectXmlParser();
            OSSObject obj = new OSSObject(this.bucketName, this.destObjectKey);
            ObjectMetaData md = parser.parse(r.getEntity().getContent());
            obj.setObjectMetaData(md);
            
            return obj;
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }

    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.oss.task.Task#generateHttpRequest()
     */
    @Override
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint() + 
                httpTool.generateCanonicalizedResource("//" + destObjectKey);
        HttpPut httpPut = new HttpPut(requestUri);
        Log.d("requestUri", httpMethod.toString() + " " + requestUri);

        // 构造Http请求
        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + destObjectKey);
        String dateStr = Helper.getGMTDate();
        String header = OSSHttpTool.generateCanonicalizedHeader(xossHeaders);
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", "", dateStr, header,
                resource);

        httpPut.setHeader(AUTHORIZATION, authorization);
        httpPut.setHeader(DATE, dateStr);

        for (Entry<String, String> entry: xossHeaders.entrySet()) {
            httpPut.setHeader(entry.getKey(), entry.getValue());
        }

        return httpPut;
    }
    
}
