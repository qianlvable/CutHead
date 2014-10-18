/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.util.Helper;

/**
 * Head Object请求，只返回某个Object的meta信息，不返回文件内容
 * 
 * @author Michael
 */
public class HeadObjectTask extends Task {
    /**
     * object名称
     */
    private String objectKey;

    /**
     * 指定在此时间后修改过
     */
    private Date modifiedSince;

    /**
     * 指定在此时间后没有修改过
     */
    private Date unModifiedSince;

    /**
     * 期望的ETag
     */
    private String expectedETag;

    /**
     * 不期望的ETag
     */
    private String unexpectedETag;

    /**
     * 构造新实例
     */
    public HeadObjectTask(String bucketName, String objectKey) {
        super(HttpMethod.HEAD, bucketName);
        this.objectKey = objectKey;
    }

    /**
     * 参数合法性检测
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName) || Helper.isEmptyString(objectKey)) {
            throw new IllegalArgumentException(
                    "bucketName or objectKey not properly set");
        }
    }

    /**
     * 构造HttpHead
     */
    protected HttpUriRequest generateHttpRequest() {
        String requestUri = this.getOSSEndPoint()
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpHead httpHead = new HttpHead(requestUri);

        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpHead.setHeader(AUTHORIZATION, authorization);
        httpHead.setHeader(DATE, dateStr);

        OSSHttpTool.addHttpRequestHeader(httpHead, IF_MODIFIED_SINCE,
                Helper.getGMTDate(modifiedSince));
        OSSHttpTool.addHttpRequestHeader(httpHead, IF_UNMODIFIED_SINCE,
                Helper.getGMTDate(unModifiedSince));
        OSSHttpTool.addHttpRequestHeader(httpHead, IF_MATCH, expectedETag);
        OSSHttpTool.addHttpRequestHeader(httpHead, IF_NONE_MATCH,
                unexpectedETag);

        return httpHead;
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
     * 设置期望时间，如果该时间早于实际修改时间，则正常传送文件，否则请求返回304 Not Modified
     * 
     * @param modifiedSince
     *            期望时间
     */
    public void setModifiedSince(Date modifiedSince) {
        this.modifiedSince = modifiedSince;
    }

    /**
     * 获取非期望时间，如果该时间等于或晚于文件实际修改时间，则正常传输文件，否则请求返回412 Precondition Failed
     * 
     * @return 非期望时间
     */
    public Date getUnModifiedSince() {
        return unModifiedSince;
    }

    /**
     * 设置非期望时间，如果该时间等于或晚于文件实际修改时间，则正常传输文件，否则请求返回412 Precondition Failed
     * 
     * @param unModifiedSince
     *            非期望时间
     */
    public void setUnModifiedSince(Date unModifiedSince) {
        this.unModifiedSince = unModifiedSince;
    }

    /**
     * 获取期望Tag，如果期望Tag和object的ETag匹配，则正常返回，否则请求返回412 Precondition Failed
     * 
     * @return 期望Tag
     */
    public String getExpectedETag() {
        return expectedETag;
    }

    /**
     * 设置期望Tag，如果期望Tag和object的ETag匹配，则正常传输文件，否则请求返回412 Precondition Failed
     * 
     * @param expectedETag
     */
    public void setExpectedETag(String expectedETag) {
        this.expectedETag = expectedETag;
    }

    /**
     * 获取期望Tag，如果期望Tag和object的ETag不匹配，则正常传输文件，否则请求返回304 Not Modified
     * 
     * @return 期望Tag
     */
    public String getUnexpectedETag() {
        return unexpectedETag;
    }

    /**
     * 设置期望Tag，如果期望Tag和object的ETag不匹配，则正常传输文件，否则请求返回304 Not Modified
     * 
     * @return 期望Tag
     */
    public void setUnexpectedETag(String unexpectedETag) {
        this.unexpectedETag = unexpectedETag;
    }

    /**
     * 返回的Object只含有Metadata信息，不包含数据
     * 
     * @return Head操作返回的Object
     * @throws OSSException
     */
    public OSSObject getResult() throws OSSException {
        try {
            HttpResponse r = this.execute();
            OSSObject obj = new OSSObject(this.bucketName, this.objectKey);
            obj.setObjectMetaData(OSSHttpTool.getObjectMetadataFromResponse(r));
            
            return obj;
        } catch (OSSException osse) {
            throw osse;
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
    }
}
