/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.http;

/**
 * OSS http请求的参数
 * 需要构造http请求的OSS类继承这个接口
 * 
 * @author Michael
 */
public interface IHttpParameters {
    /**
     * 前缀
     */
    public static final String PREFIX = "prefix";
    
    /**
     * 返回的Object最大数
     */
    public static final String MAX_KEYS = "max-keys";
    
    /**
     * 返回结果的起始标识符
     */
    public static final String MARKER = "marker";
    
    /**
     * 返回结果的起始标志符，用于List Multipart Upload（不明白为什么不直接用marker)
     */
    public static final String KEY_MARKER = "key-marker";
    
    /**
     * 对object名字进行分组的分割符
     */
    public static final String DELIMITER = "delimiter";
    
    /**
     * Multipart uploads事件的最大数目
     */
    public static final String MAX_UPLOADS = "max-uploads";
    
    /**
     * 与key-marker合用, 用于List Multipart Upload
     */
    public static final String UPLOAD_ID_MARKER = "upload-id-marker";
    
    /**
     * 返回的最大Part数，用于List parts
     */
    public static final String MAX_PARTS = "max-parts";
    
    /**
     * 返回结果partNumber的起始值，用于List parts
     */
    public static final String PART_NUMBER_MARKER = "part-number-marker";
    
    /**
     * OSS 支持的子资源
     */
    public static final String ACL = "acl";

    public static final String GROUP = "group";

    public static final String UPLOAD_ID = "uploadId";

    public static final String PART_NUMBER = "partNumber";

    public static final String UPLOADS = "uploads";

    /**
     * OSS 支持的override查询字符串
     */
    public static final String RESPONSE_CONTENT_TYPE = "response-content-type";

    public static final String RESPONSE_CONTENT_LANGUAGE = "response-content-language";

    public static final String RESPONSE_EXPIRES = "response-expires";

    public static final String RESPONSE_CACHE_CONTROL = "response-cache-control";

    public static final String RESPONSE_CONTENT_DISPOSITION = "response-content-disposition";

    public static final String RESPONSE_CONTENT_ENCODING = "response-content-encoding";
}
