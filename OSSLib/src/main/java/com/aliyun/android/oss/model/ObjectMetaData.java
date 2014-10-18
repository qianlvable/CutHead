/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.Date;

/**
 * object http请求的元数据
 * 
 * @author Michael
 */
public class ObjectMetaData extends MetaData {
    /**
     * cache-control请求头
     */
    private String cacheControl = null;

    /**
     * Content-Disposition请求头
     */
    private String contentDisposition = null;

    /**
     * Content-Encoding请求头
     */
    private String contentEncoding = null;

    /**
     * Content-Length请求头
     */
    private String contentLength = null;

    /**
     * Content-Type请求头
     */
    private String contentType = null;

    /**
     * MD5摘要
     */
    private String eTag = null;

    /**
     * Expires请求头
     */
    private Date expirationTime = null;

    /**
     * Object最后一次修改的时间
     */
    private Date lastModified = null;

    /**
     * Server
     */
    private String server = null;
    
    /**
     * Date请求头
     */
    private Date date = null;

    /**
     * 添加用户自定义header，重写父类 只支持以x-oss-meta-为前缀的参数
     */
    @Override
    public void addCustomAttr(String key, String value) {
        if (key.startsWith("x-oss-meta-")) {
            addAttr(key, value);
        }
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

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
