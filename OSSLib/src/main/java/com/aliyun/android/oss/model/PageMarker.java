/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

/**
 * 分页器每页的起始内容(相当于页码)
 * 
 * @author Michael
 */
public class PageMarker {
    /**
     * 内容
     */
    private String content;
    
    /**
     * 前一页的Marker
     */
    private PageMarker previous;
    
    /**
     * 下一页的Marker
     */
    private PageMarker next;
    
    /**
     * 构造新实例
     */
    public PageMarker(String content) {
        this.content = content;
    }
    
    /**
     * to string
     */
    public String toString() {
        return content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PageMarker getPrevious() {
        return previous;
    }

    public void setPrevious(PageMarker previous) {
        this.previous = previous;
    }

    public PageMarker getNext() {
        return next;
    }

    public void setNext(PageMarker next) {
        this.next = next;
    }
}
