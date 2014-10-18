/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruici
 */
public class ListPartXmlObject {

    private String bucketName;

    private String key;

    private String uploadId;

    private String nextPartNumberMarker;

    private String maxParts;

    private String truncated;

    private List<Part> parts = new ArrayList<Part>();

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getNextPartNumberMarker() {
        return nextPartNumberMarker;
    }

    public void setNextPartNumberMarker(String nextPartNumberMarker) {
        this.nextPartNumberMarker = nextPartNumberMarker;
    }

    public String getMaxParts() {
        return maxParts;
    }

    public void setMaxParts(String maxParts) {
        this.maxParts = maxParts;
    }

    /**
     * 如果返回true，则表示结果未完，还可以继续请求下一页。否则表示已经到达末尾。
     * @return
     */
    public String getTruncated() {
        return truncated;
    }

    public void setTruncated(String truncated) {
        this.truncated = truncated;
    }

    /**
     * 获取Part列表
     * @return
     */
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

}
