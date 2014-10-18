/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model;

import java.util.Date;

/**
 * @author ruici
 */
public class MultipartUploadSummary {

    private String key;

    private String uploadId;

    private Date initiated;

    public MultipartUploadSummary(String key, String uploadId, Date initiated) {
        super();
        this.key = key;
        this.uploadId = uploadId;
        this.initiated = initiated;
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

    public Date getInitiated() {
        return initiated;
    }

    public void setInitiated(Date initiated) {
        this.initiated = initiated;
    }
    
    

}
