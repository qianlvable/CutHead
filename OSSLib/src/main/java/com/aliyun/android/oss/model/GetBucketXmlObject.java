/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruici
 */
public class GetBucketXmlObject {
    private String bucketName = null;

    private String marker = null;

    private String maxkeys = null;

    private String delimiter = null;

    private String nextMarker = null;

    private List<OSSObjectSummary> items = new ArrayList<OSSObjectSummary>();

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public String getMaxkeys() {
        return maxkeys;
    }

    public void setMaxkeys(String maxkeys) {
        this.maxkeys = maxkeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public List<OSSObjectSummary> getItems() {
        return items;
    }

    public void setItems(List<OSSObjectSummary> items) {
        this.items = items;
    }

}
