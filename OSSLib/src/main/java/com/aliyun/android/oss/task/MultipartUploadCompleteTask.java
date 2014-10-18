/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.oss.xmlparser.MultipartUploadCompleteXmlParser;
import com.aliyun.android.oss.xmlserializer.PartsXmlSerializer;
import com.aliyun.android.util.Helper;

/**
 * 完成MultipartUpload
 * 所有数据都上传完成后，必须调用Multipart Upload Complete Task来完成整个Multipart Upload过程。
 * 在执行该操作时，必须提供所有有效的数据Part的列表(包括part号码和ETAG值)：OSS收到提交的Part
 * 列表后，会逐一校验每个数据Part的有效性。当所有Part都通过验证后，OSS将这些数据Part组合成一个
 * 完整的Object。
 * 
 * @author Michael
 */
public class MultipartUploadCompleteTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * uploadId
     */
    private String uploadId;

    /**
     * 已经上传的Part
     */
    private List<Part> partsList;
    
    /**
     * 构造新实例
     */
    public MultipartUploadCompleteTask(String bucketName, String objectKey,
            String uploadId, List<Part> partsList) {
        super(HttpMethod.POST, bucketName);
        this.objectKey = objectKey;
        this.uploadId = uploadId;
        this.partsList = partsList;
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
        if (partsList == null) {
            throw new IllegalArgumentException("partsList cannot be null");
        }
    }

    /**
     * 构造已经上传的part的XML描述
     */
    private String generateHttpEntity() {
        PartsXmlSerializer serializer = new PartsXmlSerializer(
                "CompleteMultipartUpload");
        return serializer.serialize(partsList);
    }

    /**
     * 构造HttpPost
     * 
     * @throws UnsupportedEncodingException
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpPost httpPost = new HttpPost(requestUri);

        // 验证的数据
        byte[] data = generateHttpEntity().getBytes();
        
        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpPost.setHeader(AUTHORIZATION, authorization);
        httpPost.setHeader(DATE, dateStr);

        try {
            httpPost.setEntity(new ByteArrayEntity(data));
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
        

        return httpPost;
    }
    
    /**
     * 返回经过分块组合而成的Object的信息，包括bucket, objectKey以及ETag
     * 
     * @return
     * @throws OSSException
     */
    public OSSObject getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            return new MultipartUploadCompleteXmlParser().parse(r.getEntity().getContent());
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

    public List<Part> getPartsList() {
        return partsList;
    }

    public void setPartsList(List<Part> partsList) {
        this.partsList = partsList;
    }
}
