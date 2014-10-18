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
 * 删除Object任务
 * 
 * @author Michael
 */
public class DeleteObjectTask extends Task {
    /**
     * object名称
     */
    private String objectKey;

    public DeleteObjectTask(String bucketName, String objectKey) {
        super(HttpMethod.DELETE, bucketName);
        this.objectKey = objectKey;
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

    /**
     * 如果删除成功返回true，否则抛出异常
     * 
     * @return
     * @throws Exception
     */
    public boolean getResult() throws OSSException {
        try {
            this.execute();
            return true;
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
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpDelete httpDelete = new HttpDelete(requestUri);

        // 构造HttpPut
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
}
