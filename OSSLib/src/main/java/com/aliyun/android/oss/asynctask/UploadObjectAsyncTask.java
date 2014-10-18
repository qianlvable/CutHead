
/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
 package com.aliyun.android.oss.asynctask; 

import com.aliyun.android.oss.http.HttpContentType;
import com.aliyun.android.oss.task.PutObjectTask;

/** 
 * 上传一个Object，返回一个String值表示服务器计算的该对象的MD5值
 * 可在onPostExecute方法中进行验证
 * @author Ruici 
 * 
 */
public class UploadObjectAsyncTask<Progress> extends OSSAsyncTask<byte[], Progress, String> {

    private String objectKey;

    public UploadObjectAsyncTask(String accessKeyId, String accessKeySecret, String bucketName, String objectKey) {
        super(accessKeyId, accessKeySecret, bucketName);
        this.objectKey = objectKey;
    }

    @Override
    protected String doInBackground(byte[]... params) {
        if (params == null || params.length == 0) {
            return null;
        }
        byte[] bytes = params[0];
        
        PutObjectTask task = new PutObjectTask(getBucketName(), objectKey, HttpContentType.DIR.toString(), bytes);
        task.initKey(getAccessKeyId(), getAccessKeySecret());
        
        return task.getResult();
    }

}

