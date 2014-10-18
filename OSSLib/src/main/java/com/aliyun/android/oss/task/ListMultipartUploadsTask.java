/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.ListMultipartUploadsXmlObject;
import com.aliyun.android.oss.xmlparser.ListMultipartUploadXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 罗列所有执行中的Multipart Upload事件
 * 
 * @author Michael
 */
public class ListMultipartUploadsTask extends Task {
    /**
     * 分割符
     */
    private String delimiter;

    /**
     * 返回的multipart uploads事件的最大数
     */
    private Integer maxUploads;

    /**
     * 起始标识符
     */
    private String keyMarker;

    /**
     * 指定返回指定前缀的结果
     */
    private String prefix;

    /**
     * 与keyMarker一起使用来筛选结果
     */
    private String uploadIdMarker;

    /**
     * 构造新实例
     */
    public ListMultipartUploadsTask(String bucketName) {
        super(HttpMethod.GET, bucketName);
        httpTool.setIsUploads(true);
    }

    /**
     * 参数合法性检测
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName)) {
            throw new IllegalArgumentException("bucketname not properly set");
        }
        if (maxUploads != null && maxUploads > 1000) {
            throw new IllegalArgumentException(
                    "max-uploads should not be greater than 1000");
        }
    }

    /**
     * 构造HttpGet
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint() +
                httpTool.generateCanonicalizedResource("/");
        requestUri = OSSHttpTool.appendParameterPair(requestUri, DELIMITER,
                delimiter);
        if (maxUploads != null) {
            requestUri = OSSHttpTool.appendParameterPair(requestUri,
                    MAX_UPLOADS, maxUploads.toString());
        }
        requestUri = OSSHttpTool.appendParameterPair(requestUri, KEY_MARKER,
                keyMarker);
        requestUri = OSSHttpTool
                .appendParameterPair(requestUri, PREFIX, prefix);
        requestUri = OSSHttpTool.appendParameterPair(requestUri,
                UPLOAD_ID_MARKER, uploadIdMarker);
        HttpGet httpGet = new HttpGet(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpGet.setHeader("Authorization", authorization);
        httpGet.setHeader("Date", dateStr);

        return httpGet;
    }

    /**
     * @return
     * @throws OSSException
     */
    public ListMultipartUploadsXmlObject getResult() throws OSSException {
        try {
            HttpResponse r = execute();
            return new ListMultipartUploadXmlParser().parse(r.getEntity().getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Integer getMaxUploads() {
        return maxUploads;
    }

    public void setMaxUploads(Integer maxUploads) {
        this.maxUploads = maxUploads;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
    }
    
    
}
