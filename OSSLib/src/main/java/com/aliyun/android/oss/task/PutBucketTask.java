/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.util.Helper;

/**
 * OSS PutBucket任务
 * 
 * @author Michael
 */
public class PutBucketTask extends Task {
    /**
     * 构造函数
     */
    public PutBucketTask(String bucketName) {
        super(HttpMethod.PUT, bucketName);
    }

    /**
     * 合法性验证
     */
    protected void checkArguments() {
        if (bucketName == null || bucketName.equals("")) {
            throw new IllegalArgumentException("bucketName not set");
        }
    }

    /**
     * 获取请求结果，如果成功返回true
     * @return
     * @throws OSSException
     */
    public boolean getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            return r.getStatusLine().getStatusCode() == 200;
        } catch (OSSException osse) {
            throw osse;
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
        HttpPut httpPut = new HttpPut(this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/"));

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/");
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);
        httpPut.setHeader("Authorization", authorization);
        httpPut.setHeader("Date", dateStr);

        return httpPut;
    }
}
