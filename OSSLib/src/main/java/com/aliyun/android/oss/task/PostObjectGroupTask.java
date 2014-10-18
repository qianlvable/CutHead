/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.ObjectGroup;
import com.aliyun.android.oss.model.ObjectGroupMetaData;
import com.aliyun.android.oss.xmlparser.PostObjectGroupXmlParser;
import com.aliyun.android.oss.xmlserializer.PartsXmlSerializer;
import com.aliyun.android.util.Helper;

/**
 * Post Object Group操作将根据用户提供的Object信息，在OSS服务器端创建出一个新的Object Group。
 * 用户需要将创建该Object Group所需的Object Name,E-tag以及标识该Object在整个Group中相对位置 的Part
 * ID按照规定的XML格式发给OSS
 * 
 * @author Harttle
 */
public class PostObjectGroupTask extends Task {

    /**
     * 要建立的ObjectGroup
     */
    private ObjectGroup objectGroup;

    /**
     * 要建立的ObjectGroup的Meta
     */
    private ObjectGroupMetaData objectGroupMetaData;

    /** * @param httpMethod */
    public PostObjectGroupTask(ObjectGroup objectGroup) {
        super(HttpMethod.POST);
        this.objectGroup = objectGroup;
        super.httpTool.setGroup(true);
    }

    /**
     * 得到FileGroup列表的Xml * @return xml
     */
    private String generateHttpEntity() {
        PartsXmlSerializer serializer = new PartsXmlSerializer(
                "CreateFileGroup");
        return serializer.serialize(objectGroup.getParts());
    }

    /* (non-Javadoc) * @see com.aliyun.android.oss.task.Task#checkArguments() */
    @Override
    protected void checkArguments() {

        if (Helper.isEmptyString(objectGroup.getBucketName())
                || Helper.isEmptyString(objectGroup.getName())) {
            throw new IllegalArgumentException(
                    "bucketName or objectKey not set");
        }
        if (objectGroup.getParts() == null || objectGroup.getParts().isEmpty()) {
            throw new IllegalArgumentException("partList not set");
        }
    }

    /**
     * 构造HttpPut
     */
    protected HttpUriRequest generateHttpRequest() {
        // 生成Http请求
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/"
                        + objectGroup.getName());;
        HttpPut httpPut = new HttpPut(requestUri);

        // 构造HttpPut
        String resource = httpTool.generateCanonicalizedResource("/"
                + objectGroup.getBucketName() + "/" + objectGroup.getName());
        String authorization = OSSHttpTool.generateAuthorization(accessId,
                accessKey, httpMethod.toString(), "", objectGroupMetaData
                        .getContentType(), Helper.getGMTDate(), OSSHttpTool
                        .generateCanonicalizedHeader(objectGroupMetaData
                                .getAttrs()), resource);

        httpPut.setHeader(AUTHORIZATION, authorization);
        httpPut.setHeader(DATE, Helper.getGMTDate());
        httpPut.setHeader(HOST, OSS_HOST);

        try {
            httpPut.setEntity(new StringEntity(generateHttpEntity()));
        } catch (UnsupportedEncodingException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

        return httpPut;
    }

    /**
     * 获取Post操作后得到的结果，返回一个{@link OSSObjectSummary}对象 包含以下信息：
     * <ul>
     * <li>Bucket: Object所属Bucket名称</li>
     * <li>Key: Object的Key值</li>
     * <li>Size: Object的大小</li>
     * <li>ETag: Object的ETag值</li>
     * </ul>
     * 
     * @return
     * @throws OSSException
     */
    public OSSObjectSummary getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            return new PostObjectGroupXmlParser().parse(r.getEntity()
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
