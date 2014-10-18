/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss.model.query;

import java.util.List;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.model.GetBucketXmlObject;
import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.PageMarker;
import com.aliyun.android.oss.task.GetBucketTask;
import com.aliyun.android.util.Helper;
import com.aliyun.android.util.Pagination;

/**
 * 文件列表查询器
 * 
 * @author Michael
 */
public class ObjectsQuery extends OSSQuery<OSSObjectSummary> {

    /**
     * 所属bucket
     */
    private String bucketName;

    /**
     * 文件夹路径
     */
    private String path;

    /**
     * 构造新实例
     */
    public ObjectsQuery(String accessId, String accessKey, String bucketName,
            String path, Integer maxKeys) {
        super(accessId, accessKey, maxKeys);
        this.bucketName = bucketName;
        this.path = path;
    }

    /**
     * 构造新实例
     */
    public ObjectsQuery(String accessId, String accessKey, String bucketName,
            String path) {
        this(accessId, accessKey, bucketName, path,
                GetBucketTask.DEFAULT_MAX_KEYS);
    }

    /**
     * 获得文件第一页分页器 第一页的marker即path
     */
    public Pagination<OSSObjectSummary> paginate() {
        path = Helper.formatPath(path);
        PageMarker curMarker = new PageMarker(path);
        return paginate(curMarker);
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /* (non-Javadoc) * @see com.aliyun.android.util.OSSQuery#getItems() */
    @Override
    public List<OSSObjectSummary> getItems(PageMarker curMarker)
            throws OSSException {
        GetBucketTask getBucketTask = new GetBucketTask(bucketName, maxKeys);
        getBucketTask.setAccessId(this.accessId);
        getBucketTask.setAccessKey(this.accessKey);

        path = Helper.formatPath(path);
        getBucketTask.setPrefix(path);
        getBucketTask.setMarker(curMarker.getContent());
        getBucketTask.setDelimiter("/");

        GetBucketXmlObject obj = getBucketTask.getResult();
        if (obj.getNextMarker() != null) {
            curMarker.setNext(new PageMarker(obj.getNextMarker()));
            curMarker.getNext().setPrevious(curMarker);
        }

        return obj.getItems();
    }
}
