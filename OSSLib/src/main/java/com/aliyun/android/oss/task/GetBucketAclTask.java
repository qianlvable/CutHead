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
import com.aliyun.android.oss.model.AccessLevel;
import com.aliyun.android.oss.xmlparser.GetBucketAclXmlParser;
import com.aliyun.android.util.Helper;

/**
 * 获取Bucket访问权限的任务
 * 
 * @author Michael
 */
public class GetBucketAclTask extends Task {
    /**
     * 构造新实例
     */
    public GetBucketAclTask(String bucketName) {
        super(HttpMethod.GET, bucketName);
    }

    /**
     * 合法性验证
     */
    protected void checkArguments() {
        if (bucketName == null || bucketName.equals("")) {
            throw new IllegalArgumentException("bucketName not set");
        }
    }

    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.oss.task.Task#generateHttpRequest()
     */
    @Override
    protected HttpUriRequest generateHttpRequest() {
        httpTool.setIsAcl(true);
        
        // 由于阿里云API连域名都改了，这里得做些改动，原来的方法用起来有点别扭了，等有时间了再重构一记吧
        String requestUri = this.getOSSEndPoint() + httpTool.generateCanonicalizedResource("/");
        Log.d("requestUri", requestUri);
        HttpGet httpGet = new HttpGet(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/" + bucketName + "/");
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);
        httpGet.setHeader("Authorization", authorization);
        httpGet.setHeader("Date", dateStr);

        return httpGet;
    }

    /**
     * 获取指定对象ACL信息* @return
     * 
     * @throws Exception
     * @throws
     * @throws
     */
    public AccessLevel getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            GetBucketAclXmlParser parser = new GetBucketAclXmlParser();
            return parser.parse(r.getEntity().getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }
}
