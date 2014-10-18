/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */

package com.aliyun.android.oss.task;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.http.HttpMethod;
import com.aliyun.android.oss.http.OSSHttpTool;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.Range;
import com.aliyun.android.util.CipherUtil;
import com.aliyun.android.util.CompressUtils;
import com.aliyun.android.util.Helper;
import com.aliyun.android.util.CipherUtil.CipherAlgorithm;

/**
 * @author Michael
 */
public class GetObjectTask extends Task {
    /**
     * Object名称
     */
    private String objectKey;

    /**
     * 范围
     */
    private Range<Integer> range;

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
     * OSS返回请求的自定义header
     */
    private String responseContentType = null;

    private String responseContentLanguage = null;

    private String responseExpires = null;

    private String responseCacheControl = null;

    private String responseContentDisposition = null;

    private String responseContentEncoding = null;

    /**
     * 解密密钥
     */
    private byte[] decryptKey;
    
    /**
     * 构造新实例
     */
    public GetObjectTask(String bucketName, String objectKey) {
        super(HttpMethod.GET, bucketName);
        this.objectKey = objectKey;
    }

    /**
     * 参数合法性验证
     */
    @Override
    protected void checkArguments() {
        if (Helper.isEmptyString(bucketName) || Helper.isEmptyString(objectKey)) {
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
                + httpTool.generateCanonicalizedResource("/" + objectKey);
        HttpGet httpGet = new HttpGet(requestUri);

        // 构造Http请求
        String resource = httpTool.generateCanonicalizedResource("/"
                + bucketName + "/" + objectKey);
        String dateStr = Helper.getGMTDate();
        String authorization = OSSHttpTool
                .generateAuthorization(accessId, accessKey,
                        httpMethod.toString(), "", "", dateStr, "", resource);

        httpGet.setHeader(AUTHORIZATION, authorization);
        httpGet.setHeader(DATE, dateStr);

        if (range != null) {
            OSSHttpTool.addHttpRequestHeader(httpGet, RANGE,
                    "bytes=" + range.toString());
        }
        OSSHttpTool.addHttpRequestHeader(httpGet, IF_MODIFIED_SINCE,
                Helper.getGMTDate(modifiedSince));
        OSSHttpTool.addHttpRequestHeader(httpGet, IF_UNMODIFIED_SINCE,
                Helper.getGMTDate(unModifiedSince));
        OSSHttpTool.addHttpRequestHeader(httpGet, IF_MATCH, expectedETag);
        OSSHttpTool
                .addHttpRequestHeader(httpGet, IF_NONE_MATCH, unexpectedETag);

        return httpGet;
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
     * 获取文件传输的范围，用{@link Range}对象表示
     * 
     * @return 返回文件传输的范围
     * @see Range
     */
    public Range<Integer> getRange() {
        return range;
    }

    /**
     * 设置文件传输范围，用{@link Range}对象表示
     * 
     * @param range
     *            文件传输范围
     * @see Range
     */
    public void setRange(Range<Integer> range) {
        this.range = range;
    }

    /**
     * 获取期望时间，如果该时间早于实际修改时间，则正常传送文件，否则请求返回304 not modified
     * 
     * @return 期望时间
     */
    public Date getModifiedSince() {
        return modifiedSince;
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

    public String getResponseContentType() {
        return responseContentType;
    }

    /**
     * 设置OSS返回请求的Content-Type header
     * 
     * @param responseContentType
     */
    public void setResponseContentType(String responseContentType) {
        httpTool.setContentType(responseContentType);
        this.responseContentType = responseContentType;
    }

    public String getResponseContentLanguage() {
        return responseContentLanguage;
    }

    /**
     * 设置OSS返回请求的Content-Language header
     * 
     * @param responseContentLanguage
     */
    public void setResponseContentLanguage(String responseContentLanguage) {
        httpTool.setContentLanguage(responseContentLanguage);
        this.responseContentLanguage = responseContentLanguage;
    }

    public String getResponseExpires() {
        return responseExpires;
    }

    /**
     * 设置OSS返回请求的Expires header
     * 
     * @param responseExpires
     */
    public void setResponseExpires(String responseExpires) {
        httpTool.setExpires(responseExpires);
        this.responseExpires = responseExpires;
    }

    public String getResponseCacheControl() {
        return responseCacheControl;
    }

    /**
     * 设置OSS返回请求的Cache-Contorl header
     * 
     * @param responseCacheControl
     */
    public void setResponseCacheControl(String responseCacheControl) {
        httpTool.setCacheControl(responseCacheControl);
        this.responseCacheControl = responseCacheControl;
    }

    public String getResponseContentDisposition() {
        return responseContentDisposition;
    }

    /**
     * 设置OSS返回请求的Content-Disposition header
     * 
     * @param responseContentDisposition
     */
    public void setResponseContentDisposition(String responseContentDisposition) {
        httpTool.setContentDisposition(responseContentDisposition);
        this.responseContentDisposition = responseContentDisposition;
    }

    public String getResponseContentEncoding() {
        return responseContentEncoding;
    }

    /**
     * 设置OSS返回请求的Content-Encoding header
     * 
     * @param responseContentEncoding
     */
    public void setResponseContentEncoding(String responseContentEncoding) {
        httpTool.setContentEncoding(responseContentEncoding);
        this.responseContentEncoding = responseContentEncoding;
    }

    /**
     * 获取对象，包括对象元信息以及对象内容 
     * 
     * @return {@link OSSObject}对象，包含对象元信息以及内容
     * @throws Exception
     * @see OSSObject
     */
    public OSSObject getResult() throws OSSException {
        HttpResponse r = this.execute();
        OSSObject obj = new OSSObject(this.bucketName, this.objectKey);
        try {
            obj.setObjectMetaData(OSSHttpTool.getObjectMetadataFromResponse(r));
            byte[] data = OSSHttpTool.getBytesFromIS(r.getEntity().getContent());
            
            // 解密
            String algorithm = obj.getObjectMetaData().getAttrs()
                    .get(X_OSS_META_ENCRYPT);
            if (!Helper.isEmptyString(algorithm)) {
                if (Helper.isEmptyString(new String(decryptKey))) {
                    throw new IllegalArgumentException("decrypt should not be null");
                }
                data = CipherUtil.decrypt(data, decryptKey,
                        CipherAlgorithm.valueOf(algorithm));
            }
            
            // 解压缩
            String compressMethod = obj.getObjectMetaData().getAttrs()
                    .get(X_OSS_META_COMPRESS);
            if (!Helper.isEmptyString(compressMethod)
                    && compressMethod.equals("zip")) {
                data = CompressUtils.unzipBytes(data);
            }
            
            obj.setData(data);
        } catch (Exception e) {
            throw new OSSException(e);
        } finally {
            this.releaseHttpClient();
        }
        
        return obj;
    }

    /** * @return the decryptKey */
    public byte[] getDecryptKey() {
        return decryptKey;
    }

    /** * @param decryptKey the decryptKey to set */
    public void setDecryptKey(byte[] decryptKey) {
        this.decryptKey = decryptKey;
    }
}
