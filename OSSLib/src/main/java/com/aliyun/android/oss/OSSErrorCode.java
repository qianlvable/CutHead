/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss;

/**
 * OSS定义的错误代码
 * 
 * @author Michael
 */
public interface OSSErrorCode {
    /**
     * 拒绝访问
     */
    public static final String ACCESS_DENIED = "AccessDenied";

    /**
     * Bucket已经存在
     */
    public static final String BUCKET_ALREADY_EXISTS = "BucketAlreadyExists";

    /**
     * Bucket不为空
     */
    public static final String BUCKET_NOT_EMPTY = "BucketNotEmpty";

    /**
     * 实体过大
     */
    public static final String ENTITY_TOO_LARGE = "EntityTooLarge";

    /**
     * Bucket不符合命名规范
     */
    public static final String INVALID_BUCKET_NAME = "InvalidBucketName";

    /**
     * 超过bucket最大创建数
     */
    public static final String TOO_MANY_BUCKETS = "TooManyBuckets";

    /**
     * 不合法的AccessKey和AccessId
     */
    public static final String INVALID_ACCESS_KEY_ID = "InvalidAccessKeyId";

    /**
     * Bucket不存在
     */
    public static final String NO_SUCH_BUCKET = "NoSuchBucket";
}
