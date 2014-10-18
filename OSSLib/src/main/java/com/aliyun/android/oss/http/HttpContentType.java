/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. 
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.http;

/**
 * @author Harttle
 */
public enum HttpContentType {
    DIR("application/x-director"), TXT("text/plain");

    /**
     * 请求方法的字符串表示
     */
    private String context;

    /**
     * 创建新枚举实例
     */
    private HttpContentType(String context) {
        this.context = context;
    }

    /**
     * toString
     */
    public String toString() {
        return this.context;
    }
}
