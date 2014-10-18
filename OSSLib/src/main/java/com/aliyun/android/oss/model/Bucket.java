/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.Date;

/**
 * OSS Bucket对象
 * 
 * @author Michael
 */
public class Bucket {
    /**
     * Bucket名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date creationDate;

    /**
     * bucket所有者
     */
    private User owner;

    /**
     * 构造函数
     */
    public Bucket() {}

    /**
     * 传入名字的构造函数
     */
    public Bucket(String name) {
        this.name = name;
    }

    /**
     * 参数包含所有属性的构造函数* @param name
    /** * @param creationDate
    /** * @param owner
     */
    public Bucket(String name, Date creationDate, User owner) {
        super();
        this.name = name;
        this.creationDate = creationDate;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
