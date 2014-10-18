
/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.android.oss.asynctask; 

import android.os.AsyncTask;

/** 
 * 
 * @author Ruici 
 * 
 */
public abstract class OSSAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    
    private String accessKeyId;
    private String accessKeySecret;
    
    private String bucketName;
    
    public OSSAsyncTask(String accessKeyId, String accessKeySecret) {
        super();
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public OSSAsyncTask(String accessKeyId, String accessKeySecret,
            String bucketName) {
        this(accessKeyId, accessKeySecret);
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}

