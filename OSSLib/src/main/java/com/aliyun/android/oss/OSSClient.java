/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved. Use of this source code
 * is governed by a BSD-style license that can be found in the LICENSE file.
 */
package com.aliyun.android.oss;

import java.io.FileOutputStream;
import java.util.List;

import com.aliyun.android.oss.http.HttpContentType;
import com.aliyun.android.oss.model.AccessLevel;
import com.aliyun.android.oss.model.Bucket;
import com.aliyun.android.oss.model.MultipartUploadSummary;
import com.aliyun.android.oss.model.OSSObject;
import com.aliyun.android.oss.model.OSSObjectSummary;
import com.aliyun.android.oss.model.ObjectGroup;
import com.aliyun.android.oss.model.Part;
import com.aliyun.android.oss.model.query.ListMultipartsQuery;
import com.aliyun.android.oss.model.query.ObjectsQuery;
import com.aliyun.android.util.Helper;
import com.aliyun.android.util.MultipartUploadMission;
import com.aliyun.android.util.Pagination;
import com.aliyun.android.oss.task.*;

/**
 * OSS请求客户端
 * 
 * @author Michael
 * @Modified Harttle
 */
public class OSSClient {
    /**
     * 用户授权ID
     */
    private String accessId;

    /**
     * 用户授权Key
     */
    private String accessKey;

    /**
     * 获取用户Bucket列表* @return /** * @throws Exception
     */
    public List<Bucket> getBucketList() throws OSSException {
        GetServiceTask tsk = new GetServiceTask();
        setAuthorization(tsk);

        return tsk.getResult();
    }

    /**
     * 创建bucket
     */
    public boolean createBucket(String bucketName) {
        try {
            // 使用PutBucketTask
            PutBucketTask tsk = new PutBucketTask(bucketName);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 删除bucket
     */
    public boolean deleteBucket(String bucketName) {
        try {
            // 使用DeleteBucketTask
            DeleteBucketTask tsk = new DeleteBucketTask(bucketName);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 非递归罗列bucket下的文件和文件夹, 获得一个分页器
     */
    public Pagination<OSSObjectSummary> viewBucket(String bucketName) {
        try {
            ObjectsQuery query = new ObjectsQuery(accessId, accessKey,
                    bucketName, "");
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 非递归罗列bucket下的文件和文件夹, 获得一个分页器
     */
    public Pagination<OSSObjectSummary> viewBucket(String bucketName,
            Integer maxKeys) {
        try {
            ObjectsQuery query = new ObjectsQuery(accessId, accessKey,
                    bucketName, "", maxKeys);
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 改变Bucket的可见性
     */
    public boolean changeBucketAcl(String bucketName, AccessLevel accessLevel) {
        try {
            // 使用PutBucketAclTask
            PutBucketAclTask tsk = new PutBucketAclTask(bucketName, accessLevel);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 获取Bucket的可见性信息
     */
    public AccessLevel getBucketAcl(String bucketName) {
        try {
            // 使用GetBucketAclTask
            GetBucketAclTask tsk = new GetBucketAclTask(bucketName);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 创建文件夹
     */
    public String createFolder(String bucketName, String serverPath) {
        try {
            serverPath = Helper.formatPath(serverPath);
            // 使用PutObjectTask. ObjectKey = serverPath( + "/"),
            // 注意folder使用object来表示，这与OSS控制台的实现是一致的。
            PutObjectTask tsk = new PutObjectTask(bucketName, serverPath,
                    HttpContentType.DIR.toString());
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 非递归查看文件夹里的所有文件和子文件夹, 获得一个文件分页器
     */
    public Pagination<OSSObjectSummary> viewFolder(String bucketName,
            String serverPath) {
        try {
            ObjectsQuery query = new ObjectsQuery(accessId, accessKey,
                    bucketName, serverPath);
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 非递归查看文件夹里的所有文件和子文件夹, 获得一个文件分页器
     */
    public Pagination<OSSObjectSummary> viewFolder(String bucketName,
            String serverPath, Integer maxKeys) {
        try {
            ObjectsQuery query = new ObjectsQuery(accessId, accessKey,
                    bucketName, serverPath, maxKeys);
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 删除文件夹，并且递归的删除文件夹内的所有文件
     */
    public boolean deleteFolder(String bucketName, String serverPath) {
        try {
            serverPath = Helper.formatPath(serverPath);
            deleteSubContents(bucketName, serverPath);
            deleteObject(bucketName, serverPath);
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 删除文件夹下的所有内容, 但保留该文件夹
     */
    public boolean deleteSubContents(String bucketName, String serverPath) {
        try {
            serverPath = Helper.formatPath(serverPath);
            deleteSubFolders(bucketName, serverPath);
            deleteSubObjects(bucketName, serverPath);
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 删除文件夹下的所有子文件夹
     */
    public boolean deleteSubFolders(String bucketName, String serverPath) {
        try {
            serverPath = Helper.formatPath(serverPath);

            for (OSSObjectSummary obj: viewFolder(bucketName, serverPath)
                    .getContents()) {
                if (obj.isDirectory()) {
                    deleteFolder(bucketName, obj.getKey());
                }
            }
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 删除文件夹下的所有子文件
     */
    public boolean deleteSubObjects(String bucketName, String serverPath) {
        try {
            serverPath = Helper.formatPath(serverPath);
            for (OSSObjectSummary obj: viewFolder(bucketName, serverPath)
                    .getContents()) {
                if (!obj.isDirectory()) {
                    deleteObject(bucketName, obj.getKey());
                }
            }
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 复制文件夹
     */
    public boolean copyFolder(String srcBucketName, String srcPath,
            String destBucketName, String destPath) {
        try {
            srcPath = Helper.formatPath(srcPath);
            destPath = Helper.formatPath(destPath);

            // 拷贝文件夹
            copyObject(srcBucketName, srcPath, destBucketName, destPath);
            for (OSSObjectSummary obj: viewFolder(srcBucketName, srcPath)
                    .getContents()) {
                // 拷贝子文件夹
                if (obj.isDirectory()) {
                    copyFolder(srcBucketName, obj.getKey(), destBucketName, obj
                            .getKey().replaceFirst(srcPath, destPath));// 得到之后的路径
                } else {// 拷贝子文件
                    copyObject(srcBucketName, obj.getKey(), destBucketName, obj
                            .getKey().replaceFirst(srcPath, destPath));
                }
            }
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 移动文件夹
     */
    public boolean moveFolder(String srcBucketName, String srcPath,
            String destBucketName, String destPath) {
        try {
            copyFolder(srcBucketName, srcPath, destBucketName, destPath);
            deleteFolder(srcBucketName, srcPath);
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
        return true;
    }

    /**
     * 上传文件
     */
    public String uploadObject(String bucketName, String objectKey,
            String localPath) {
        try {
            PutObjectTask tsk = new PutObjectTask(bucketName, objectKey,
                    HttpContentType.DIR.toString());
            tsk.setUploadFilePath(localPath);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传文件
     * 
     * @param bucketName
     * @param objectKey
     * @param data
     * @return
     */
    public String uploadObject(String bucketName, String objectKey, byte[] data) {
        try {
            PutObjectTask tsk = new PutObjectTask(bucketName, objectKey,
                    HttpContentType.DIR.toString(), data);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传压缩文件
     * 
     * @param bucketName
     * @param objectKey
     * @param localPath
     * @return
     */
    public String uploadZippedObject(String bucketName, String objectKey,
            String localPath) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), localPath, true,
                    false, null);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传压缩文件
     * 
     * @param bucketName
     * @param objectKey
     * @param data
     * @return
     */
    public String uploadZippedObject(String bucketName, String objectKey,
            byte[] data) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), data, true,
                    false, null);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传加密文件
     * 
     * @param bucketName
     * @param objectKey
     * @param localPath
     * @return
     */
    public String uploadEncObject(String bucketName, String objectKey,
            String localPath, byte[] key) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), localPath,
                    false, true, key);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }
    
    /**
     * 上传加密文件
     * 
     * @param bucketName
     * @param objectKey
     * @param data
     * @return
     */
    public String uploadEncObject(String bucketName, String objectKey,
            byte[] data, byte[] key) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), data, false,
                    true, key);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传压缩并加密的文件
     * 
     * @param bucketName
     * @param objectKey
     * @param localPath
     * @return
     */
    public String uploadZipEncObject(String bucketName, String objectKey,
            String localPath, byte[] key) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), localPath, true,
                    true, key);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 上传压缩并加密的文件
     * 
     * @param bucketName
     * @param objectKey
     * @param data
     * @return
     */
    public String uploadZipEncObject(String bucketName, String objectKey,
            byte[] data, byte[] key) {
        try {
            PutZipEncObjectTask tsk = new PutZipEncObjectTask(bucketName,
                    objectKey, HttpContentType.DIR.toString(), data, true,
                    true, key);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 获取文件，返回完整的OSSObject，包括其数据
     */
    public OSSObject getObject(String bucketName, String objectKey) {
        try {
            GetObjectTask tsk = new GetObjectTask(bucketName, objectKey);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }
    
    /**
     * 获取加密文件，返回完整的OSSObject，包括其数据
     */
    public OSSObject getObject(String bucketName, String objectKey, byte[] key) {
        try {
            GetObjectTask tsk = new GetObjectTask(bucketName, objectKey);
            tsk.setDecryptKey(key);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 下载文件
     */
    public boolean downloadObject(String bucketName, String objectKey,
            String localPath) {
        try {
            GetObjectTask tsk = new GetObjectTask(bucketName, objectKey);

            setAuthorization(tsk);
            FileOutputStream file = new FileOutputStream(localPath);
            file.write(tsk.getResult().getData());
            file.close();
            return true;
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }
    
    /**
     * 下载加密文件
     */
    public boolean downloadObject(String bucketName, String objectKey,
            String localPath, byte[] key) {
        try {
            GetObjectTask tsk = new GetObjectTask(bucketName, objectKey);
            tsk.setDecryptKey(key);
            
            setAuthorization(tsk);
            FileOutputStream file = new FileOutputStream(localPath);
            file.write(tsk.getResult().getData());
            file.close();
            return true;
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteObject(String bucketName, String objectKey) {
        try {
            DeleteObjectTask tsk = new DeleteObjectTask(bucketName, objectKey);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 获取文件摘要信息, 仍以OSSObject作为结果返回，但是不包含数据
     */
    public OSSObject getObjectSummary(String bucketName, String objectKey) {
        try {
            HeadObjectTask tsk = new HeadObjectTask(bucketName, objectKey);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 复制文件
     */
    public OSSObject copyObject(String srcBucket, String srcObj,
            String destBucket, String destObj) {
        try {
            CopyObjectTask tsk = new CopyObjectTask(srcBucket, srcObj,
                    destBucket, destObj);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 移动文件
     */
    public OSSObject moveObject(String srcBucket, String srcObj,
            String destBucket, String destObj) {
        try {
            OSSObject obj = copyObject(srcBucket, srcObj, destBucket, destObj);
            deleteObject(srcBucket, srcObj);
            return obj;
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 创建分块上传任务
     */
    public MultipartUploadMission createMultipartUploadMission(
            String bucketName, String objectKey, String localPath,
            Integer partSize) {
        try {
            MultipartUploadMission mission = new MultipartUploadMission(
                    accessId, accessKey, bucketName, objectKey, localPath,
                    partSize);
            return mission;
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 查看bucket下所有已经初始化但是还未complete或者abort的分块上传任务
     */
    public Pagination<MultipartUploadSummary> listMultipartUploadMissions(
            String bucketName) {
        try {
            ListMultipartUploadsTask tsk = new ListMultipartUploadsTask(
                    bucketName);
            setAuthorization(tsk);

            ListMultipartsQuery query = new ListMultipartsQuery(accessId,
                    accessKey, bucketName);
            return query.paginate();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 创建出一个新的Object Group
     */
    public OSSObjectSummary createObjectGroup(String bucketName, String objectKey,
            List<Part> parts) {
        try {
            ObjectGroup objectGroup = new ObjectGroup(objectKey);
            objectGroup.setBucketName(bucketName);
            objectGroup.setParts(parts);

            PostObjectGroupTask tsk = new PostObjectGroupTask(objectGroup);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 下载Object Group
     */
    public boolean downloadObjectGroup(String bucketName, String objectKey,
            String localPath) {
        try {
            GetObjectGroupTask tsk = new GetObjectGroupTask(bucketName,
                    objectKey);
            setAuthorization(tsk);
            FileOutputStream file = new FileOutputStream(localPath);
            file.write(tsk.getResult().getData());
            file.close();
            return true;
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 删除Object Group
     */
    public boolean deleteObjectGroup(String bucketName, String objectKey) {
        try {
            DeleteObjectGroupTask tsk = new DeleteObjectGroupTask(bucketName,
                    objectKey);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /**
     * 获取Object Group中的文件列表
     */
    public ObjectGroup getObjectGroupIndex(String bucketName, String objectKey) {
        try {
            ObjectGroup objectGroup = new ObjectGroup(objectKey);
            objectGroup.setBucketName(bucketName);
            GetObjectGroupIndexTask tsk = new GetObjectGroupIndexTask(
                    objectGroup);
            setAuthorization(tsk);
            return tsk.getResult();
        } catch (OSSException e) {
            throw e;
        } catch (Exception ee) {
            throw new OSSException(ee);
        }
    }

    /*
     * 用签名运行Task
     */
    private void setAuthorization(Task tsk) {
        tsk.setAccessId(accessId);
        tsk.setAccessKey(accessKey);
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
