/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.ObjectMetaData;
import com.aliyun.android.oss.xmlparser.MultipartUploadInitXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 初始化MultipartUpload（分块上传object）的任务
 * 使用 Multipart  Upload模式传输数据前，必须先调用该接口来通知OSS初始化一个Multipart Upload事件。
 * 该接口会返回一个OSS服务器创建的全局唯一的Upload ID，用于标识本次Multipart Upload事件。用户可以
 * 根据这个ID来发起相关的操作。
 * 
 * @author Michael
 */
public class MultipartUploadInitTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * Object元数据
     */
    private ObjectMetaData objectMetaData;

    /**
     * 构造新实例
     */
    public MultipartUploadInitTask(String bucketName, String objectKey) {
        super(HttpMethod.POST, bucketName);
        this.objectKey = objectKey;
        this.objectMetaData = new ObjectMetaData();
        httpTool.setIsUploads(true);
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
//        if (objectMetaData == null) {
//            throw new IllegalArgumentException(
//                    "objectMetaData should not be null");
//        }
    }

    /**
     * 构造HttpHost
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint() +
                httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpPost httpPost = new HttpPost(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String xossHeader = OSSHttpTool
                .generateCanonicalizedHeader(objectMetaData.getAttrs());
        String contentType = objectMetaData.getContentType() == null ? ""
                : objectMetaData.getContentType();
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", contentType, dateStr,
                xossHeader, resource);

        httpPost.setHeader(AUTHORIZATION, authorization);
        httpPost.setHeader(DATE, dateStr);

        OSSHttpTool.addHttpRequestHeader(httpPost, CACHE_CONTROL,
                objectMetaData.getCacheControl());
        OSSHttpTool.addHttpRequestHeader(httpPost, CONTENT_DISPOSITION,
                objectMetaData.getContentDisposition());
        OSSHttpTool.addHttpRequestHeader(httpPost, CONTENT_ENCODING,
                objectMetaData.getContentEncoding());
        OSSHttpTool.addHttpRequestHeader(httpPost, CONTENT_TYPE,
                objectMetaData.getContentType());
        OSSHttpTool.addHttpRequestHeader(httpPost, EXPIRES,
                Helper.getGMTDate(objectMetaData.getExpirationTime()));

        // 加入用户自定义header
        for (Entry<String, String> entry: objectMetaData.getAttrs().entrySet()) {
            OSSHttpTool.addHttpRequestHeader(httpPost, entry.getKey(),
                    entry.getValue());
        }

        return httpPost;
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

    public ObjectMetaData getObjectMetaData() {
        return objectMetaData;
    }

    /**
     * 初始化Multipart Upload请求时，支持如下标准的HTTP请求头：Cache-Control, Content-Disposition, Content-Encoding, Content-Type, Expires, 
     * 以及以x-oss-meta-开头的用户自定义Headers。
     * 通过传入封装了这些请求头的{@link ObjectMetaData}对象将它们添加至请求
     * 
     * @param objectMetaData
     * @see ObjectMetaData
     */
    public void setObjectMetaData(ObjectMetaData objectMetaData) {
        this.objectMetaData = objectMetaData;
    }
    
    
    public String getResult() {
        try {
            HttpResponse r = this.execute();
            String uploadId = new MultipartUploadInitXmlParser().parse(r.getEntity().getContent());
            if (uploadId == null) {
                throw new OSSException("no UploadId returned by OSS server.");
            }
            
            return uploadId;
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }
    
}
