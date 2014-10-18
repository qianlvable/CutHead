/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.Date;

/**
 * 阿里云Object的摘要信息
 * 
 * @author Michael
 */
public class OSSObjectSummary {
    
    /**
     * Bucket Name
     */
    private String bucketName;
    /**
     * Object key
     */
    private String key;
    
    /**
     * 最近修改时间
     */
    private Date lastModified;
    
    /**
     * 摘要信息
     */
    private String eTag;
    
    /**
     * FIXME xml里的Type, 貌似文档里没介绍着是啥
     */
    private String type;
    
    /**
     * 大小
     */
    private long size;
    
    /**
     * Object存储类别
     */
    private String storageClass;

    /**
     * Object的拥有者信息
     */
    private User owner;
    
    
    public OSSObjectSummary(String bucketName, String key, Date lastModified, String eTag,
            String type, long size, String storageClass, User owner) {
        this.bucketName = bucketName;
        this.key = key;
        this.lastModified = lastModified;
        this.eTag = eTag;
        this.type = type;
        this.size = size;
        this.storageClass = storageClass;
        this.owner = owner;
    }

    public OSSObjectSummary(String bucketName, String key) {
        this.bucketName = bucketName;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public boolean isDirectory() {
        return this.key.endsWith("/");
    }
}
