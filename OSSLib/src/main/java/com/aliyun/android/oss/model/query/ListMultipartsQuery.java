/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.model.query; 

import java.util.List;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.model.ListMultipartUploadsXmlObject;
import com.aliyun.android.oss.model.MultipartUploadSummary;
import com.aliyun.android.oss.model.PageMarker;
import com.aliyun.android.oss.task.ListMultipartUploadsTask;
import com.aliyun.android.util.Pagination;

/** 
 * 获取正在执行中的Multipart Upload任务的Query对象
 * 
 * @author luoruici 
 * 
 */
public class ListMultipartsQuery extends OSSQuery<MultipartUploadSummary> {

    public static Integer DEFAULT_MAX_KEYS = 1000;
    
    private String bucketName;
    
    public ListMultipartsQuery(String accessId, String accessKey,
            String bucketName) {
        super(accessId, accessKey, DEFAULT_MAX_KEYS);
        this.setBucketName(bucketName);
    }
    
    public ListMultipartsQuery(String accessId, String accessKey,
            Integer maxKeys, String bucketName) {
        super(accessId, accessKey, maxKeys);
        this.setBucketName(bucketName);
    }

    /**
     * 返回查询的第一页对象
     * @return
     */
    public Pagination<MultipartUploadSummary> paginate() {
        PageMarker marker = new PageMarker("");
        return paginate(marker);
    }
    
    /* (non-Javadoc) * @see com.aliyun.android.oss.model.query.OSSQuery#getItems(com.aliyun.android.oss.model.PageMarker) */
    @Override
    public List<MultipartUploadSummary> getItems(PageMarker curMarker) throws OSSException {
        ListMultipartUploadsTask task = new ListMultipartUploadsTask(bucketName);
        task.initKey(accessId, accessKey);
        task.setMaxUploads(maxKeys);
        task.setKeyMarker(curMarker.getContent());
        
        ListMultipartUploadsXmlObject obj = task.getResult();
        if (obj.getTruncated().equals("true")) {
            curMarker.setNext(new PageMarker(obj.getNextKeyMarker()));
            curMarker.getNext().setPrevious(curMarker);
        }
        return obj.getUploads();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}

