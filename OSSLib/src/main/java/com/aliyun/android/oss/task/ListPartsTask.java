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
import com.aliyun.android.oss.model.ListPartXmlObject;
import com.aliyun.android.oss.xmlparser.ListPartXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 罗列指定Upload Id所属的所有已经上传成功的part
 * 
 * @author Michael
 */
public class ListPartsTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * uploadId
     */
    private String uploadId;

    /**
     * 最大parts展示数
     */
    private Integer maxParts;

    /**
     * 结果起始值
     */
    private String partNumberMarker;

    /**
     * 构造新实例
     */
    public ListPartsTask(String bucketName, String objectKey, String uploadId) {
        super(HttpMethod.GET, bucketName);
        this.objectKey = objectKey;
        this.uploadId = uploadId;
        httpTool.setUploadId(uploadId);
    }

    /**
     * 参数合法性验证
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName) || Helper.isEmptyString(objectKey)) {
            throw new IllegalArgumentException(
                    "bucketName or objectKey not set");
        }
        if (Helper.isEmptyString(uploadId)) {
            throw new IllegalArgumentException("uploadId not set");
        }
    }

    public ListPartXmlObject getResult() throws OSSException {
        try {
            HttpResponse r = execute();
            return new ListPartXmlParser().parse(r.getEntity().getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }

    /**
     * 构造HttpGet
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpGet httpGet = new HttpGet(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);
        httpGet.setHeader(AUTHORIZATION, authorization);
        httpGet.setHeader(DATE, dateStr);

        OSSHttpTool.addHttpRequestHeader(httpGet, MAX_PARTS,
                maxParts.toString());
        OSSHttpTool.addHttpRequestHeader(httpGet, PART_NUMBER_MARKER,
                partNumberMarker);

        return httpGet;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * 获取每次请求返回结果最大数目
     * @return
     */
    public Integer getMaxParts() {
        return maxParts;
    }

    /**
     * 设置每次请求返回结果最大数目
     * @param maxParts
     */
    public void setMaxParts(Integer maxParts) {
        this.maxParts = maxParts;
    }

    /**
     * 获取每次请求的起始对象，类似{@link GetBucketTask}中的marker参数
     * @return
     */
    public String getPartNumberMarker() {
        return partNumberMarker;
    }

    /**
     * 设置每次请求的起始对象，类似{@link GetBucketTask}中的marker参数
     * @param partNumberMarker
     */
    public void setPartNumberMarker(String partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
    }
}
