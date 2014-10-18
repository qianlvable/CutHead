/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

/**
 * 阿里云OSS文件
 * 
 * @author Michael
 */
public class OSSObject {
    /**
     * 所属bucket
     */
    private String bucketName;

    /**
     * 文件(夹)名
     */
    private String objectKey;

    /**
     * Object 元数据
     */
    private ObjectMetaData objectMetaData;

    /**
     * 数据
     */
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * 构造新实例
     */
    public OSSObject(String bucketName, String objectKey) {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }

    /**
     * 判断是否是文件夹
     */
    public boolean isDirectory() {
        return objectKey.endsWith("/");
    }

    /**
     * 判断是否是文件夹
     */
    public static boolean isDirectory(String path) {
        return path.endsWith("/");
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

    public ObjectMetaData getObjectMetaData() {
        return objectMetaData;
    }

    public void setObjectMetaData(ObjectMetaData objectMetaData) {
        this.objectMetaData = objectMetaData;
    }

}
