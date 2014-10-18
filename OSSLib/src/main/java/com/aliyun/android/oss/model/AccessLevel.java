/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

/**
 * 访问权限
 * 
 * @author Michael
 */
public enum AccessLevel {
    PRIVATE("private"), PUBLIC_READ("public-read"), PUBLIC_READ_WRITE(
            "public-read-write");

    /**
     * 枚举字符串内容
     */
    private String context;

    /**
     * 构造函数
     */
    private AccessLevel(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return this.context;
    }
    
    /**
     * 根据* @param context获取对应的枚举类型常量
    /** * @return
     */
    public static AccessLevel valueFromName(String context) {
        if (context.equals("private")) {
            return AccessLevel.PRIVATE;
        } else if (context.equals("public-read")) {
            return AccessLevel.PUBLIC_READ;
        } else if (context.equals("public-read-write")) {
            return AccessLevel.PUBLIC_READ_WRITE;
        }
        
        return null;
    }
}
