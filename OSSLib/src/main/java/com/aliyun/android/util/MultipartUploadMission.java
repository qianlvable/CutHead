/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.oss.model.query.ListPartsQuery;
import com.aliyun.android.oss.task.MultipartUploadAbortTask;
import com.aliyun.android.oss.task.MultipartUploadCompleteTask;
import com.aliyun.android.oss.task.MultipartUploadInitTask;
import com.aliyun.android.oss.task.UploadPartTask;

/**
 * 分组上传任务
 * 
 * @author Michael
 */
public class MultipartUploadMission {

    public static Integer DEFAULT_MAX_PARTS = 1000;
    
    private String accessId;

    private String accessKey;

    /**
     * 所属Bucket
     */
    private String bucketName;

    /**
     * 目标文件
     */
    private String targetObjectKey;

    /**
     * 本地文件
     */
    private String localFile;

    /**
     * 分块上传标识ID
     */
    private String uploadId;

    /**
     * 初始化时间
     */
    private Date initTime;

    /**
     * 文件块大小,以Byte为单位
     */
    private Integer partSize;

    /**
     * 文件块信息, 以partNumber作为索引 Modified by harttle, changed from HashMap, no need
     * to use hashMap(better sparseArray)
     */
    private List<Part> parts;

    /**
     * 构造新实例
     */
    public MultipartUploadMission(String accessId, String accessKey,
            String bucketName, String targetObjectKey, String localFile,
            Integer partSize) {
        if (partSize < 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "partSize should be greater than 5M");
        }
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.bucketName = bucketName;
        this.targetObjectKey = targetObjectKey;
        this.localFile = localFile;
        this.partSize = partSize;
    }

    /**
     * 使用MultipartUploadInitTask进行初始化
     * 然后读取文件，并根据partSize进行截断，填充数据和partNumber到parts中
     */
    public void init() {
        try {
            MultipartUploadInitTask initTask = new MultipartUploadInitTask(
                    bucketName, targetObjectKey);
            initTask.initKey(accessId, accessKey);
            this.uploadId = initTask.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 调用UploadPartTask上传每一块文件，并对parts进行etag填充。这样用户可以单独起线程去完成每一块的上传。
     */
    public void uploadPart(Part part) {

        try {
            UploadPartTask uploadTask = new UploadPartTask(bucketName,
                    targetObjectKey, uploadId, part);
            uploadTask.initKey(accessId, accessKey);
            String eTag = uploadTask.getResult();
            part.setEtag(eTag);
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 结束分组上传
     */
    public OSSObject complete() {
        // 调用MultipartUploadCompleteTask.

        try {
            MultipartUploadCompleteTask task = new MultipartUploadCompleteTask(
                    bucketName, targetObjectKey, uploadId, parts);
            task.initKey(accessId, accessKey);
            return task.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 取消上传，使用MultipartUploadAbortTask，如果其所属的某些 Part 仍然在上传， 那么这次中止操作将无法删除这些
     * Part。所以如果存在并发访问的情况,为了彻底释放OSS 上的空间,需要调用几次 Abort Multipart Upload 接口。
     */
    public boolean abort() {
        // 取消上传，
        try {
            MultipartUploadAbortTask task = new MultipartUploadAbortTask(
                    bucketName, targetObjectKey, uploadId);
            task.initKey(accessId, accessKey);
            return task.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 罗列已经上传成功的文件块，默认每页1000个Part
     */
    public Pagination<Part> listSuccessParts() {
        return listSuccessParts(DEFAULT_MAX_PARTS);
    }
    
    
    /**
     * 罗列已经上传成功的文件块
     */
    public Pagination<Part> listSuccessParts(Integer maxParts) {
        try {
            ListPartsQuery query = new ListPartsQuery(accessId, accessKey,
                    bucketName, targetObjectKey, uploadId, maxParts);
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 简单的分组上传方式
     */
    public OSSObject quickUpload() {
        // 自动调用init, uploadPart和complete，不启用多线程，或者用户直接为这整个过程开启一个线程。
        init();
        parts = uploadFile();
        return complete();
    }

    /**
     * 切割文件并进行上传
     * 由于分块上传的文件往往较手机内存来说很大，所以不能全部读入到内存中，需要切割一块后就直接上传，然后释放内存
     * 
     * @throws IOException
     */
    public List<Part> uploadFile() {

        try {
            if (Helper.isEmptyString(localFile)) {
                throw new IllegalArgumentException(
                        "localFile not set for multipart upload mission");
            }
            FileInputStream file = new FileInputStream(localFile);
            ArrayList<Part> tempParts = new ArrayList<Part>();

            int partNumber = 1;// 当前的组号

            while (true) {
                // 服务器支持的组数上限
                if (partNumber > 10000) {
                    file.close();
                    throw new OSSException("Part number exeeded!");
                }
                byte[] bytes = new byte[partSize];
                int byteRead = file.read(bytes, 0, partSize);// 读入的字节数
                if (byteRead == -1)
                    break;
                
                // 对文件的最后一块进行处理
                if (byteRead < partSize) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(partSize);
                    baos.write(bytes, 0, byteRead);
                    bytes = baos.toByteArray();
                    baos.close();
                }
                Part part = new Part(partNumber++);
                part.setData(bytes);
                
                // 上传文件块
                uploadPart(part);
                
                // 释放内存
                part.setData(null);

                tempParts.add(part);
            }
            file.close();
            return tempParts;

        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /** * @return the bucketName */
    public String getBucketName() {
        return bucketName;
    }

    /** * @param bucketName the bucketName to set */

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /** * @return the targetObjectKey */
    public String getTargetObjectKey() {
        return targetObjectKey;
    }

    /** * @param targetObjectKey the targetObjectKey to set */

    public void setTargetObjectKey(String targetObjectKey) {
        this.targetObjectKey = targetObjectKey;
    }

    /** * @return the localFile */
    public String getLocalFile() {
        return localFile;
    }

    /** * @param localFile the localFile to set */

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    /** * @return the uploadId */
    public String getUploadId() {
        return uploadId;
    }

    /** * @param uploadId the uploadId to set */

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /** * @return the initTime */
    public Date getInitTime() {
        return initTime;
    }

    /** * @param initTime the initTime to set */

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    /** * @return the partSize */
    public Integer getPartSize() {
        return partSize;
    }

    /** * @param partSize the partSize to set */

    public void setPartSize(Integer partSize) {
        this.partSize = partSize;
    }

    /** * @return the accessId */
    public String getAccessId() {
        return accessId;
    }

    /** * @param accessId the accessId to set */

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    /** * @return the accessKey */
    public String getAccessKey() {
        return accessKey;
    }

    /** * @param accessKey the accessKey to set */

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
