/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.http;

/**
 * OSS http请求的Header
 * 需要构造http请求的OSS类继承这个接口
 * 
 * @author Michael
 */
public interface IHttpHeaders {
    
    /**
     * ETag
     */
    public static final String ETAG = "ETag";
    /**
     * Content-Length
     */
    public static final String CONTENT_TYPE = "Content-Type";
    
    /**
     * Cache-control
     */
    public static final String CACHE_CONTROL = "Cache-control";
    
    /**
     * Server
     */
    public static final String SERVER = "Server";
    
    /**
     * Expires
     */
    public static final String EXPIRES = "Expires";
    
    /**
     * Content-Encoding
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    
    /**
     * Content-Disposition
     */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    
    /**
     * Content-Length
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    
    /**
     * Host
     */
    public static final String HOST = "Host";
    
    /**
     * Last-Modified
     */
    public static final String LAST_MODIFIED = "Last-Modified";
    
    /**
     * Date
     */
    public static final String DATE = "Date";
    
    /**
     * 验证
     */
    public static final String AUTHORIZATION = "Authorization";
    
    /**
     * ObjectGroup 标识
     */
    public static final String X_OSS_FILE_GROUP = "x-oss-file-group";
    
    /**
     * 设置访问权限Header标识
     */
    public static final String X_OSS_ACL = "x-oss-acl";
    
    /**
     * Get(HEAD) Object请求常量：范围
     */
    public static final String RANGE = "Range";
    
    /**
     * Get(HEAD) Object请求常量：在指定时间之后有过更改
     */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    
    /**
     * Get(HEAD) Object请求常量：在指定时间后没有过更改
     */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    
    /**
     * Get(HEAD) Object请求常量：如果计算出来的MD5与指定的tag相同
     */
    public static final String IF_MATCH = "If-Match";
    
    /**
     * Get(HEAD) Object请求常量：如果计算出来的MD5与指定的tag不同
     */
    public static final String IF_NONE_MATCH = "If-None-Match";
    
    /**
     * 压缩方式
     */
    public static final String X_OSS_META_COMPRESS = "x-oss-meta-compress";
    
    /**
     * 加密方式
     */
    public static final String X_OSS_META_ENCRYPT = "x-oss-meta-encrypt";
}
