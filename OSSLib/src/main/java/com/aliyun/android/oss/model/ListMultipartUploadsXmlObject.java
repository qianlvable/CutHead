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
public class ListMultipartUploadsXmlObject {

    private String bucketName;

    private String keyMarker;

    private String uploadIdMarker;

    private String nextKeyMarker;

    private String nextUploadIdMarker;

    private String prefix;

    private String delimiter;

    private String maxUploads;
    
    private String truncated;

    private List<MultipartUploadSummary> uploads = new ArrayList<MultipartUploadSummary>();

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public void setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
    }

    public String getUploadIdMarker() {
        return uploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public void setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
    }

    public String getNextUploadIdMarker() {
        return nextUploadIdMarker;
    }

    public void setNextUploadIdMarker(String nextUploadIdMarker) {
        this.nextUploadIdMarker = nextUploadIdMarker;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getMaxUploads() {
        return maxUploads;
    }

    public void setMaxUploads(String maxUploads) {
        this.maxUploads = maxUploads;
    }

    public List<MultipartUploadSummary> getUploads() {
        return uploads;
    }

    public void setUploads(List<MultipartUploadSummary> uploads) {
        this.uploads = uploads;
    }

    /** * @return the prefix */
    public String getPrefix() {
        return prefix;
    }

    /** * @param prefix the prefix to set */

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /** * @return the truncated */
    public String getTruncated() {
        return truncated;
    }

    
    /** * @param truncated the truncated to set */
    
    public void setTruncated(String truncated) {
        this.truncated = truncated;
    }

}
