/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.android.oss.asynctask; 

import java.util.List;

import com.aliyun.android.oss.model.Bucket;
import com.aliyun.android.oss.task.GetServiceTask;

/** 
 * 
 * 列出所有Bucket，返回一个List
 * @author Ruici 
 * 
 */
public abstract class ListBucketAsyncTask<Progress> extends OSSAsyncTask<Void, Progress, List<Bucket>> {

    public ListBucketAsyncTask(String accessKeyId, String accessKeySecret) {
        super(accessKeyId, accessKeySecret, null);
    }

    /* (non-Javadoc) * @see android.os.AsyncTask#doInBackground(Params[]) */
    @Override
    protected List<Bucket> doInBackground(Void... params) {
        GetServiceTask task = new GetServiceTask();
        task.initKey(this.getAccessKeyId(), this.getAccessKeySecret());
        
        return task.getResult();
    }
}

