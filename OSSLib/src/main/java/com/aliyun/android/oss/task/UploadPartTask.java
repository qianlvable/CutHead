/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.util.Helper;

/**
 * 上传一个数据块，用于Multipart Upload任务中
 * 调用该Task上传分块数居前，必须调用Multipart Upload Init Task来获取服务器返回的UploadId。
 * 在获取该UploadId后，可以根据指定的Object名和UploadId来分块(Part)上传数据。每一个上传的
 * Part都有一个标识它的号码(part number, 范围是1~10000)。如果用同一个part号码上传了新的数
 * 据，那么该Part已有的数据将会被覆盖。除了最后一块Part之外，其它part最小为5MB；最后一块
 * Part大小没有限制
 * 
 * @author Michael
 */
public class UploadPartTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * 上传的数据块
     */
    private Part part;

    /**
     * uploadId
     */
    private String uploadId;

    /**
     * 构造新实例
     */
    public UploadPartTask(String bucketName, String objectKey, String uploadId,
            Part part) {
        super(HttpMethod.PUT, bucketName);
        this.objectKey = objectKey;
        this.uploadId = uploadId;
        this.part = part;
        httpTool.setPartNumber(part.getPartNumber());
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
        if (part.getPartNumber() == null || part.getPartNumber() < 1
                || part.getPartNumber() > 10000) {
            throw new IllegalArgumentException("partNumber should be 1~10000");
        }
        if (null == part.getData()) {
            throw new IllegalArgumentException("this part doesn't contain data");
        }
    }

    /**
     * 构造HttpPut
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint() + 
                httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpPut httpPut = new HttpPut(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpPut.setHeader(AUTHORIZATION, authorization);
        httpPut.setHeader(DATE, dateStr);

        ByteArrayEntity entity = new ByteArrayEntity(part.getData());
        httpPut.setEntity(entity);

        return httpPut;
    }
    
    /**
     * 返回上传Part数据的MD5值，推荐在收到该值后用MD5对上传数据进行正确性校验
     * @return
     */
    public String getResult() {
        HttpResponse r = this.execute();
        Header etagHeader = r.getFirstHeader("ETag");
        if (etagHeader == null) {
            throw new OSSException("no ETag header returned from oss.");
        }
        String value = etagHeader.getValue();
        
        //去掉返回值首尾的"
        while (value.startsWith("\"")) {
            value = value.substring(1);
        }
        while (value.endsWith("\"")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
