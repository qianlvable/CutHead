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
import com.aliyun.android.oss.model.ObjectGroup;
import com.aliyun.android.oss.xmlparser.GetObjectGroupIndexXmlParser;
import com.aliyun.android.util.Helper;

/**
 * Get Object Group Index操作用于返回Object Group中的Object List信息。改操作是在Get Object
 * Group请求的基础上增加了一个Header：x-oss-file-group，用于标识获取Object List操作
 * 
 * @author Harttle
 */
public class GetObjectGroupIndexTask extends Task {

    /**
     * 要获取的ObjectGroup
     */
    private ObjectGroup objectGroup;

    /**
     * 构造新实例
     */
    public GetObjectGroupIndexTask(ObjectGroup objectGroup) {
        super(HttpMethod.GET);
        this.objectGroup = objectGroup;
    }

    /**
     * 参数合法性验证
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(objectGroup.getBucketName())
                || Helper.isEmptyString(objectGroup.getName())) {
            throw new IllegalArgumentException(
                    "bucketName or objectKey not properly set");
        }
    }

    /**
     * 构造HttpGet
     */
    protected HttpUriRequest generateHttpRequest() {
        // 生成Http请求
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/"
                        + objectGroup.getName());;
        HttpGet httpGet = new HttpGet(requestUri);

        // 构造Http请求
        String resource = httpTool.generateCanonicalizedResource("/"
                + objectGroup.getBucketName() + "/" + objectGroup.getName());
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", "", Helper.getGMTDate(),
                "", resource);

        httpGet.setHeader(AUTHORIZATION, authorization);
        httpGet.setHeader(DATE, Helper.getGMTDate());
        httpGet.setHeader(HOST, OSS_HOST);
        httpGet.setHeader(X_OSS_FILE_GROUP, "");

        return httpGet;
    }

    /**
     * 返回一个{@link ObjectGroup} 对象，包含了Part列表
     * 
     * @return
     */
    public ObjectGroup getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            return new GetObjectGroupIndexXmlParser().parse(r.getEntity()
                    .getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }
}
