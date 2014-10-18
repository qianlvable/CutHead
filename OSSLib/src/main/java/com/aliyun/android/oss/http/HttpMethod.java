/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.http;

/**
 * Http请求方法
 * 
 * @author Michael
 */
public enum HttpMethod {
    GET("GET"), PUT("PUT"), DELETE("DELETE"), POST("POST"), HEAD("HEAD");
    
    /**
     * 请求方法的字符串表示
     */
    private String context;
    
    /**
     * 创建新枚举实例
     */
    private HttpMethod(String context) {
        this.context = context;
    }
    
    /**
     * toString
     */
    public String toString() {
        return this.context;
    }
}
