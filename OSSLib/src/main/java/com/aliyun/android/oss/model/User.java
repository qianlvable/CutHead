/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

/**
 * OSS 用户信息，如Bucket的所有者
 * 
 * @author Michael
 */
public class User {
    /**
     * 用户id
     */
    private String id;

    /**
     * 用户显示名
     */
    private String displayName;

    public User(String id, String displayName) {
        super();
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * toString方法
     */
    @Override
    public String toString() {
        return this.toString() + "[id:" + id + ",displayName:" + displayName
                + "]";
    }
}
