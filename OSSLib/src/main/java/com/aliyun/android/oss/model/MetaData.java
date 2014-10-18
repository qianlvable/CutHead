/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Http请求的元数据
 * 
 * @author Michael
 */
public class MetaData {
    /**
     * 用户自定义的元数据参数
     */
    private Map<String, String> attrs;
    
    /**
     * 构造实例
     */
    public MetaData() {
        attrs = new HashMap<String, String>();
    }
    
    /**
     * 添加用户自定义的元数据参数
     */
    public void addCustomAttr(String key, String value) {
        attrs.put(key, value);
    }

    /**
     * 获取用户自定义的元数据参数
     */
    public Map<String, String> getAttrs() {
        return attrs;
    }
    
    /**
     * 添加用户属性
     */
    protected void addAttr(String key, String value) {
        if (attrs.containsKey(key)) {
            value = attrs.get(key) + ":" + value;
        }
        attrs.put(key, value);
    }
}
