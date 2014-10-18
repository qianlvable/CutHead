/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.util.Helper;

/**
 * 终止Multipart upload任务。 如果存在并发访问的情况，往往需要调用几次abort multipart upload才能彻底释放oss上的空间
 * 
 * @author Michael
 */
public class MultipartUploadAbortTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * uploadId
     */
    private String uploadId;

    /**
     * 构造新实例
     */
    public MultipartUploadAbortTask(String bucketName, String objectKey,
            String uploadId) {
        super(HttpMethod.DELETE, bucketName);
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

    /**
     * 构造HttpDelete
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpDelete httpDelete = new HttpDelete(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpDelete.setHeader(AUTHORIZATION, authorization);
        httpDelete.setHeader(DATE, dateStr);

        return httpDelete;
    }
    
    public boolean getResult() throws OSSException {
        try {
            this.execute();
            return true;
        } finally {
            this.releaseHttpClient();
        }
    }
}
