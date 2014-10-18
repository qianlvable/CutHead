/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.GetBucketXmlObject;
import com.aliyun.android.oss.xmlparser.GetBucketObjectsXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 获取Bucket内容的任务
 * 
 * @author Michael
 */
public class GetBucketTask extends Task {
    /**
     * 前缀
     */
    private String prefix;

    /**
     * 起始标识符，设定Get Bucket的返回结果从marker之后按字母排序的第一个开始返回
     */
    private String marker;

    /**
     * Object的最大数
     */
    private Integer maxKeys;

    /**
     * 分割符
     */
    private String delimiter;

    /**
     * 单词请求的最大返回数
     */
    public static Integer DEFAULT_MAX_KEYS = 1000;

    /**
     * 创建新实例
     */
    public GetBucketTask(String bucketName) {
        super(HttpMethod.GET, bucketName);
        this.bucketName = bucketName;
        this.maxKeys = DEFAULT_MAX_KEYS;
    }

    /**
     * 创建新实例
     */
    public GetBucketTask(String bucketName, Integer maxKeys) {
        super(HttpMethod.GET, bucketName);
        this.bucketName = bucketName;
        this.maxKeys = maxKeys;
    }

    /**
     * 参数合法性验证
     */
    @Override
    protected void checkArguments() {
        if (bucketName == null || bucketName.equals("")) {
            throw new IllegalArgumentException("bucketName not set");
        }
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * * @return
    /** * @throws OSSException
     */
    public GetBucketXmlObject getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            GetBucketObjectsXmlParser parser = new GetBucketObjectsXmlParser();
            return parser.parse(r.getEntity().getContent());
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
        // 生成Http请求
        String requestUri = this.getOSSEndPoint() + "/";
        requestUri = OSSHttpTool
                .appendParameterPair(requestUri, PREFIX, prefix);
        requestUri = OSSHttpTool
                .appendParameterPair(requestUri, MARKER, marker);
        if (maxKeys != null && maxKeys != DEFAULT_MAX_KEYS) {
            requestUri = OSSHttpTool.appendParameterPair(requestUri, MAX_KEYS,
                    maxKeys.toString());
        }
        requestUri = OSSHttpTool.appendParameterPair(requestUri, DELIMITER,
                delimiter);
        Log.d("requestUri", requestUri);
        HttpGet httpGet = new HttpGet(requestUri);

        // 构造HttpGet
        String resource = httpTool.generateCanonicalizedResource("/" + bucketName + "/");
        String dateStr = Helper.getGMTDate();
        String content = "GET\n\n\n" + dateStr + "\n" + resource;
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, content);
        httpGet.setHeader("Authorization", authorization);
        httpGet.setHeader("Date", dateStr);

        return httpGet;
    }
}
