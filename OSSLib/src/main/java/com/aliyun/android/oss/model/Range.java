/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

/**
 * 范围类
 * 
 * @author Michael
 */
public class Range<T> {
    /**
     * 起始
     */
    private T start;
    
    /**
     * 终止
     */
    private T end;
    
    /**
     * 构造新实例
     */
    public Range (T start, T end) {
        this.start = start;
        this.end = end;
    }

    /**
     * to string
     */
    public String toString() {
        return start.toString() + "-" + end.toString();
    }
    
    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }
}
