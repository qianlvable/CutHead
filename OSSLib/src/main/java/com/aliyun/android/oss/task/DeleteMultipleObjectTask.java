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
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.xmlparser.DeleteMultipleObjectXmlParser;
import com.aliyun.android.oss.xmlserializer.DeleteMultipleObjectXmlSerializer;
import com.aliyun.android.util.Base64;
import com.aliyun.android.util.Helper;
import com.aliyun.android.util.MD5Util;

/**
 * 通过一个HTTP请求删除同一个Bucket中的多个Object。操作支持一次请求内最多删除1000个Object，
 * 并提供两种返回模式：详细(verbose)模式和简单模式(quiet)模式：
 * <ul>
 *   <li>详细模式:OSS返回的消息体中会包含每一个删除Object的结果。</li>
 *   <li>简单模式:OSS返回的消息体中只包含删除过程中出错的Object结果；如果所有删除都成功的话，则没有消息体</li>
 * </ul>
 * 
 * @author luoruici 
 *
 */
public class DeleteMultipleObjectTask extends Task {
    /**
     * 待删除的ObjectKey List
     */
    private List<String> keyList;
    
    /**
     * 删除模式：是否为后台quiet删除模式。
     * 为true时表示quiet删除；quiet删除不返回删除成功的结果；默认为false
     */
    private boolean quiet;
    
    /**
     * 构造函数，初始化一个DeleteMultipleObjectTask，quite属性默认为false，即以详细模式进行删除
     *
     * @param bucketName
     * @param keyList
     */
    public DeleteMultipleObjectTask(String bucketName, List<String> keyList) {
        this(bucketName, keyList, false);
    }
    
    public DeleteMultipleObjectTask(String bucketName, List<String> keyList, boolean quiet) {
        super(HttpMethod.POST, bucketName);
        this.keyList = keyList;
        this.quiet = quiet;
    }

    private String generateHttpEntityStr() throws UnsupportedEncodingException {
        DeleteMultipleObjectXmlSerializer serializer = new DeleteMultipleObjectXmlSerializer("Delete");
        String xml = serializer.serialize(this.keyList, this.quiet);
        return xml;
    }
    
    /**
     * 返回被删除的对象key列表，如果是详细模式，则会包含每一个对象的结果；如果是简单模式，则只包含出错的对象。
     * 简单模式中，如果返回一个大小为0的List则表示全部删除成功
     * 
     * @return 被删除的对象key列表
     * @throws OSSException
     */
    public List<String> getResult() throws OSSException{
        try {
            HttpResponse r = this.execute();
            return new DeleteMultipleObjectXmlParser().parse(r.getEntity().getContent());
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }

    @Override
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint() +
                httpTool.generateCanonicalizedResource("/?delete");
        
        HttpPost httpPost = new HttpPost(requestUri);
        
        String md5 = "";
        try {
            String entity = generateHttpEntityStr();
            byte[] mds = MD5Util.getMD5(entity.getBytes("UTF-8"));
            md5 = Base64.encode(mds);
            httpPost.setHeader("Content-MD5", md5);
            httpPost.setEntity(new StringEntity(entity, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e("exception occurs when deleting mutiple object", e.getMessage());
        }
        
        String resource = httpTool.generateCanonicalizedResource("/" + bucketName + "/?delete");
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), md5, "text/plain; charsert=UTF-8", dateStr, "", resource);
        
        httpPost.setHeader(CONTENT_TYPE, "text/plain; charsert=UTF-8");
        httpPost.setHeader(AUTHORIZATION, authorization);
        httpPost.setHeader(DATE, dateStr);
        
        return httpPost;
    }

    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName) || keyList == null || keyList.size() == 0) {
            throw new IllegalArgumentException(
                    "bucketName or objectKeyList not set");
        }
    }
    
}

