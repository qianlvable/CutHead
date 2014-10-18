/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Object分组，每个分组包含多个Part信息，不允许递归分组
 * 
 * @author Michael
 */
public class ObjectGroup {
    /**
     * 分组名称
     */
    private String name;
    
    /**
     * 所属的bucketName
     */
    private String bucketName;

    /**
     * 分组包含的part
     */
    private List<Part> parts;

    /**
     * Object Group的元信息
     */
    private ObjectMetaData meta;
    
    /**
     * 创建实例
     */
    public ObjectGroup(String name) {
        this.name = name;
        parts = new ArrayList<Part>();
    }

    /**
     * 用key, bucketName, part列表来创建一个实例
     * 
     * @param name
     * @param bucketName
     * @param parts
     */
    public ObjectGroup(String name, String bucketName, List<Part> parts) {
        super();
        this.name = name;
        this.bucketName = bucketName;
        this.parts = parts;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    /** * @return the bucketName */
    public String getBucketName() {
        return bucketName;
    }

    /** * @param bucketName the bucketName to set */    
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public ObjectMetaData getMeta() {
        return meta;
    }

    public void setMeta(ObjectMetaData meta) {
        this.meta = meta;
    }
}
