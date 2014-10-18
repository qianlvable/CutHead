/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.IHttpHeaders;
import com.aliyun.android.oss.http.IHttpParameters;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.HttpResponseError;
import com.aliyun.android.oss.xmlparser.HttpResponseErrorParser;

/**
 * OSS请求的任务基类
 * 
 * @author Michael
 */
public abstract class Task implements IHttpParameters, IHttpHeaders {

    public static String OSS_PROTOCOL = "http://";

    /**
     * OSS HOST
     */
    public static String OSS_HOST = "oss-cn-beijing.aliyuncs.com";

    /**
     * 用户授权ID，匿名访问可以不设置
     */
    protected String accessId;

    /**
     * 用户授权key, 匿名访问可以不设置
     */
    protected String accessKey;

    /**
     * http请求方法
     */
    protected HttpMethod httpMethod;

    /**
     * Http请求工具
     */
    protected OSSHttpTool httpTool;

    /**
     * bucket名称
     */
    protected String bucketName;

    /**
     * Http客户端，用来执行请求方法
     */

    protected HttpClient client;

    /**
     * 构造新实例
     * 
     * @param httpMethod
     *            任务请求方法的类型
     */
    public Task(HttpMethod httpMethod) {
        this(httpMethod, null);
    }

    public Task(HttpMethod httpMethod, String bucketName) {
        httpTool = new OSSHttpTool();
        this.httpMethod = httpMethod;
        this.client = new DefaultHttpClient();
        this.bucketName = bucketName;
    }

    protected String getOSSEndPoint() {
        if (this.bucketName == null) {
            return String.format("%s%s", OSS_PROTOCOL, OSS_HOST);
        }

        return String
                .format("%s%s.%s", OSS_PROTOCOL, this.bucketName, OSS_HOST);
    }

    protected String getOSSHost() {
        if (this.bucketName == null) {
            return OSS_HOST;
        }

        return String.format("%s.%s", this.bucketName, OSS_HOST);
    }

    /**
     * 初始化AccessKey
     * 
     * @param accessKeyId
     * @param accessKeySecret
     */
    public void initKey(String accessKeyId, String accessKeySecret) {
        this.accessId = accessKeyId;
        this.accessKey = accessKeySecret;
    }

    /**
     * 执行任务，对于派生出来的任务类型，分别执行他们实现的抽象方法 由于对响应的处理有相似之处：如果返回码为200表示请求成功，由子类自己处理响应对象；
     * 否则出现错误，统一处理服务器返回xml格式错误信息并封装为 {@link OSSException}抛出
     * 
     * @return 服务器返回的响应对象
     * @throws OSSException
     */
    protected HttpResponse execute() throws OSSException {
        checkArguments();

        try {
            HttpResponse r = this.client.execute(this.generateHttpRequest());
            if (r.getStatusLine().getStatusCode() / 100 <= 3) {
                return r;
            } else {
                HttpResponseError error = this.getResponseError(r);
                OSSException osse = new OSSException(error);
                throw osse;
            }
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        }
    }

    /**
     * 仅在Http返回码不为200时调用，获取服务器返回的错误信息
     * 
     * @param response
     * @return 返回{@link HttpResponseError}对象，由{@link HttpResponseErrorParser}
     *         解析后获得
     * @throws IllegalStateException
     * @throws XmlPullParserException
     * @throws IOException
     */
    protected HttpResponseError getResponseError(HttpResponse response)
            throws IllegalStateException, XmlPullParserException, IOException {
        HttpResponseErrorParser parser = new HttpResponseErrorParser();
        HttpResponseError error = parser.parse(response.getEntity()
                .getContent());

        return error;
    }

    /**
     * 生成Http请求对象，根据请求方法不同可能是不同Method对应的对象
     */
    protected abstract HttpUriRequest generateHttpRequest();

    /**
     * 参数合法性验证
     */
    protected abstract void checkArguments();

    /**
     * 释放HttpClient，在Task完成后必须调用
     */
    protected void releaseHttpClient() {
        this.client.getConnectionManager().shutdown();
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
