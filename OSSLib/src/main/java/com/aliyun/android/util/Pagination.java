/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.util;

import java.util.List;

import com.aliyun.android.oss.model.PageMarker;
import com.aliyun.android.oss.model.query.OSSQuery;

/**
 * 文件列表页
 * 
 * @author Michael
 */
public class Pagination<T> {
    /**
     * 当前页码
     */
    private PageMarker curMarker;

    /**
     * 文件列表
     */
    private List<T> contents;

    /**
     * 文件列表查询器
     */
    private OSSQuery<T> query;

    /**
     * 构造新实例
     */
    public Pagination(PageMarker curMarker, List<T> contents, OSSQuery<T> query) {
        this.curMarker = curMarker;
        this.contents = contents;
        this.query = query;
    }

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return curMarker.getPrevious() != null;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return curMarker.getNext() != null;
    }

    /**
     * 提供下一个分页器
     */
    public Pagination<T> next() {
        if (hasNext()) {
            return query.paginate(curMarker.getNext());
        }
        return null;
    }

    /**
     * 提供上一个分页器
     */
    public Pagination<T> previous() {
        if (hasPrevious()) {
            return query.paginate(curMarker.getPrevious());
        }
        return null;
    }

    public PageMarker getCurMarker() {
        return curMarker;
    }

    public void setCurMarker(PageMarker curMarker) {
        this.curMarker = curMarker;
    }

    public Integer getMaxKeys() {
        return this.query.getMaxKeys();
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }

    public OSSQuery<T> getQuery() {
        return query;
    }

    public void setQuery(OSSQuery<T> query) {
        this.query = query;
    }

}
