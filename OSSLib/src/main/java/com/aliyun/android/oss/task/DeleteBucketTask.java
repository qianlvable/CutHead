/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.util.Helper;

/**
 * @author Michael
 */
public class DeleteBucketTask extends Task {

    /**
     * 创建新实例
     */
    public DeleteBucketTask(String bucketName) {
        super(HttpMethod.DELETE, bucketName);
    }

    /**
     * 参数合法性验证
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName)) {
            throw new IllegalArgumentException("bucketName not set");
        }
    }

    /**
     * 获取结果,true表示删除成功，false表示删除失败
     * 
     * @return
     * @throws Exception
     */
    public boolean getResult() throws OSSException {
        try {
            this.execute();
            return true;
        } catch (OSSException osse) {
            throw osse;
        } finally {
            this.releaseHttpClient();
        }
    }

    /* (non-Javadoc) * @see com.aliyun.android.oss.task.Task#generateHttpRequest() */
    @Override
    protected HttpUriRequest generateHttpRequest() {
        HttpDelete httpDelete = new HttpDelete(this.getOSSEndPoint() + "/");
        Log.d("requestUri", this.getOSSEndPoint() + "/");
        
        String resource = httpTool.generateCanonicalizedResource("/" + this.bucketName + "/");
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", "", dateStr, "", resource);
        httpDelete.setHeader("Authorization", authorization);
        httpDelete.setHeader("Date", dateStr);
        return httpDelete;
    }
}
