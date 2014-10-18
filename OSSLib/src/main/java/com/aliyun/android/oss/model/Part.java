/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.Date;

/**
 * 整体数据的部分数据块
 * 
 * @author Michael
 */
public class Part {
    /**
     * md5 tag
     */
    private String etag;

    /**
     * 块序号
     */
    private Integer partNumber;

    /**
     * 块名
     */
    private String partName;

    /**
     * 最后修改时间
     */
    private Date lastModified;

    /**
     * 块大小
     */
    private int size;

    /**
     * 数据
     */
    public byte[] data;

    /**
     * 创建实例
     */
    public Part(int partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * 创建实例
     */
    public Part(String etag, int partNumber) {
        this.etag = etag;
        this.partNumber = partNumber;
    }

    /**
     * 创建实例
     */
    public Part(String etag, Integer partNumber, int size) {
        this.etag = etag;
        this.partNumber = partNumber;
        this.size = size;
    }
    
    /**
     * 创建实例
     */
    public Part(String etag, Integer partNumber, String partName, int size) {
        this.etag = etag;
        this.partNumber = partNumber;
        this.partName = partName;
        this.size = size;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Integer getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(Integer partNumber) {
        this.partNumber = partNumber;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /** * @return the lastModified */
    public Date getLastModified() {
        return lastModified;
    }

    /** * @param lastModified the lastModified to set */

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /** * @return the partName */
    public String getPartName() {
        return partName;
    }

    
    /** * @param partName the partName to set */
    
    public void setPartName(String partName) {
        this.partName = partName;
    }
}
