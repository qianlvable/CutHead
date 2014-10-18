/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

import com.aliyun.android.oss.model.ObjectMetaData;
import com.aliyun.android.util.Helper;

/**
 * OSS构造Http请求的辅助类
 * 
 * @author Michael
 */
public class OSSHttpTool implements IHttpParameters, IHttpHeaders {
    /**
     * OSS 支持的子资源属性
     */
    private Boolean isAcl = null;

    private Boolean isGroup = false;

    private String uploadId = null;

    private Integer partNumber = null;

    private Boolean isUploads = false;

    /**
     * OSS 支持的override查询字符串属性
     */
    private String contentType = null;

    private String contentLanguage = null;

    private String expires = null;

    private String cacheControl = null;

    private String contentDisposition = null;

    private String contentEncoding = null;

    /**
     * 生成子资源和override查询字符串组成的签名访问资源 按签名时资源构成的顺序，这样可以方便签名时使用
     */
    public String generateCanonicalizedResource(String baseResource) {
        if (isAcl != null) {
            baseResource = appendParameter(baseResource, ACL);
        }
        if (isGroup) {
            baseResource = appendParameter(baseResource, GROUP);
        }
        if (partNumber != null) {
            baseResource = appendParameterPair(baseResource, PART_NUMBER,
                    partNumber.toString());
        }
        if (cacheControl != null) {
            baseResource = appendParameterPair(baseResource,
                    RESPONSE_CACHE_CONTROL, cacheControl);
        }
        if (contentDisposition != null) {
            baseResource = appendParameterPair(baseResource,
                    RESPONSE_CONTENT_DISPOSITION, contentDisposition);
        }
        if (contentEncoding != null) {
            baseResource = appendParameterPair(baseResource,
                    RESPONSE_CONTENT_ENCODING, contentEncoding);
        }
        if (contentLanguage != null) {
            baseResource = appendParameterPair(baseResource,
                    RESPONSE_CONTENT_LANGUAGE, contentLanguage);
        }
        if (contentType != null) {
            baseResource = appendParameterPair(baseResource,
                    RESPONSE_CONTENT_TYPE, contentType);
        }
        if (expires != null) {
            baseResource = appendParameterPair(baseResource, RESPONSE_EXPIRES,
                    expires);
        }
        if (uploadId != null) {
            baseResource = appendParameterPair(baseResource, UPLOAD_ID,
                    uploadId);
        }
        if (isUploads) {
            baseResource = appendParameter(baseResource, UPLOADS);
        }

        return baseResource;
    }

    public static byte[] getBytesFromIS(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        while ((b = is.read()) != -1)
            baos.write(b);
        return baos.toByteArray();
    }

    /**
     * 拼装一对参数pair到url后面
     */
    public static String appendParameterPair(String baseUri, String paramKey,
            String paramValue) {
        if (Helper.isEmptyString(paramValue) || Helper.isEmptyString(paramKey)) {
            return baseUri;
        }
        if (baseUri.contains("?")) {
            baseUri += "&" + paramKey + "=" + paramValue;
        } else {
            baseUri += "?" + paramKey + "=" + paramValue;
        }
        return baseUri;
    }

    /**
     * 拼装参数到url后面
     */
    public static String appendParameter(String baseUri, String param) {
        if (Helper.isEmptyString(param)) {
            return baseUri;
        }
        if (baseUri.contains("?")) {
            baseUri += "&" + param;
        } else {
            baseUri += "?" + param;
        }
        return baseUri;
    }

    /**
     * 从Http Response中获取OSS Object的元信息，包括系统和用户自定义信息
     * 
     * @param response
     *            Http Response对象，要求对象必须可用，即对应的client不能事先释放了资源 /* @return
     *            {@link ObjectMetaData} 对象
     * @throws ParseException
     * @see ObjectMetaData
     */
    public static ObjectMetaData getObjectMetadataFromResponse(
            HttpResponse response) throws ParseException {
        Header[] headers = response.getAllHeaders();
        ObjectMetaData meta = new ObjectMetaData();
        for (Header h: headers) {
            if (h.getName().equalsIgnoreCase(CONTENT_LENGTH)) {
                meta.setContentLength(h.getValue());
            } else if (h.getName().equalsIgnoreCase(CONTENT_TYPE)) {
                meta.setContentType(h.getValue());
            } else if (h.getName().equalsIgnoreCase(CONTENT_ENCODING)) {
                meta.setContentEncoding(h.getValue());
            } else if (h.getName().equalsIgnoreCase(CONTENT_DISPOSITION)) {
                meta.setContentDisposition(h.getValue());
            } else if (h.getName().equalsIgnoreCase(CACHE_CONTROL)) {
                meta.setCacheControl(h.getValue());
            } else if (h.getName().equalsIgnoreCase(EXPIRES)) {
                meta.setExpirationTime(Helper.getGMTDateFromString(h.getValue()));
            } else if (h.getName().equalsIgnoreCase(DATE)) {
                meta.setDate(Helper.getGMTDateFromString(h.getValue()));
            } else if (h.getName().equalsIgnoreCase(LAST_MODIFIED)) {
                meta.setLastModified(Helper.getGMTDateFromString(h.getValue()));
            } else if (h.getName().equalsIgnoreCase(SERVER)) {
                meta.setServer(h.getValue());
            } else if (h.getName().equals(ETAG)) {
                meta.seteTag(h.getValue());
            } else if (h.getName().startsWith("x-oss-meta-")) {
                meta.addCustomAttr(h.getName(), h.getValue());
            }
        }
        return meta;
    }

    /**
     * 添加http请求Header
     */
    public static void addHttpRequestHeader(HttpRequestBase request,
            String key, String value) {
        if (!Helper.isEmptyString(key) && !Helper.isEmptyString(value)) {
            request.setHeader(key, value);
        }
    }

    /**
     * 生成签名字符串，已经拼装好待签名内容
     */
    public static String generateAuthorization(String accessId,
            String accessKey, String content) {
        String signature = null;
        try {
            signature = Helper.getHmacSha1Signature(content, accessKey);
        } catch (Exception e) {
            Log.i("authorization", e.toString());
        }
        return "OSS " + accessId + ":" + signature;
    }

    /**
     * 生成签名字符串
     */
    public static String generateAuthorization(String accessId,
            String accessKey, String httpMethod, String md5, String type,
            String date, String ossHeaders, String resource) {
        String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date
                + "\n" + ossHeaders + resource;
        Log.d("content", content);
        return generateAuthorization(accessId, accessKey, content);
    }

    /**
     * 生成CanonicalizedHeader, 以\n结束
     */
    public static String generateCanonicalizedHeader(Map<String, String> headers) {
        String ossHeader = "";
        List<String> list = new ArrayList<String>();
        list.addAll(headers.keySet());
        Collections.sort(list);

        String post = "";
        for (String s: list) {
            if (s.equals(post)) {
                ossHeader += "," + headers.get(s);
            } else {
                ossHeader += "\n" + s + ":" + headers.get(s);
            }
            post = s;
        }

        if (!Helper.isEmptyString(ossHeader)) {
            ossHeader = ossHeader.trim();
            ossHeader += "\n";
        }
        return ossHeader;
    }

    public Boolean getIsAcl() {
        return isAcl;
    }

    public void setIsAcl(Boolean isAcl) {
        this.isAcl = isAcl;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        this.isGroup = group;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public Boolean getIsUploads() {
        return isUploads;
    }

    public void setIsUploads(Boolean isUploads) {
        this.isUploads = isUploads;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }
}
