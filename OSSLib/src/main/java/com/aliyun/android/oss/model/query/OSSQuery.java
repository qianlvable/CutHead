/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.android.oss.model.query; 

import java.util.List;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.model.PageMarker;
import com.aliyun.android.util.Pagination;

/** 
 * 用于生成分页对象的查询抽象类
 * @author Ruici 
 * 
 */
public abstract class OSSQuery<T> {

    protected String accessId;

    protected String accessKey;
    
    protected Integer maxKeys;

    public abstract List<T> getItems(PageMarker curMarker) throws OSSException;
    
    public OSSQuery(String accessId, String accessKey, Integer maxKeys) {
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.maxKeys = maxKeys;
    }
    
    public abstract Pagination<T> paginate();
    
    public Pagination<T> paginate(PageMarker curMarker) {
        return new Pagination<T>(curMarker, this.getItems(curMarker), this);
    }
    
    public Integer getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(Integer maxKeys) {
        this.maxKeys = maxKeys;
    }
    
    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

}

