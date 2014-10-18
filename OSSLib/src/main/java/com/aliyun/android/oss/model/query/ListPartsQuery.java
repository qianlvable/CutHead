/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.model.query;

import java.util.List;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.model.ListPartXmlObject;
import com.aliyun.android.oss.model.PageMarker;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.oss.task.ListPartsTask;
import com.aliyun.android.util.Pagination;

/**
 * @author ruici
 */
public class ListPartsQuery extends OSSQuery<Part> {

    public static Integer DEFAULT_MAX_KEYS = 1000;
    
    private String bucketName;

    private String key;

    private String uploadId;
    
    public ListPartsQuery(String accessId, String accessKey, String bucketName,
            String key, String uploadId, Integer maxKeys) {
        super(accessId, accessKey, maxKeys);
        this.bucketName = bucketName;
        this.key = key;
        this.uploadId = uploadId;
    }
    
    public ListPartsQuery(String accessId, String accessKey, String bucketName,
            String key, String uploadId) {
        this(accessId, accessKey, bucketName, key, uploadId, DEFAULT_MAX_KEYS);
    }

    /*
     * (non-Javadoc) * @see
     * com.aliyun.android.util.OSSQuery#getItems(com.aliyun.
     * android.oss.model.PageMarker)
     */
    @Override
    public List<Part> getItems(PageMarker curMarker) throws OSSException {
        ListPartsTask task = new ListPartsTask(bucketName, key, uploadId);
        task.initKey(accessId, accessKey);
        task.setMaxParts(maxKeys);
        task.setPartNumberMarker(curMarker.getContent());
        
        ListPartXmlObject obj = task.getResult();
        if (obj.getTruncated().equals("true")) {
            curMarker.setNext(new PageMarker(obj.getNextPartNumberMarker()));
            curMarker.getNext().setPrevious(curMarker);
        }
        
        return obj.getParts();
    }

    public String getBucketName() {
        return bucketName;
    }
    
    public Pagination<Part> paginate() {
        PageMarker marker = new PageMarker("");
        return paginate(marker);
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

}
