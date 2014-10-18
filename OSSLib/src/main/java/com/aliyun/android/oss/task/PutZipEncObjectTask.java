/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.oss.task;

import org.apache.http.HttpResponse;

import com.aliyun.android.oss.OSSException;
import com.aliyun.android.util.CipherUtil;
import com.aliyun.android.util.CompressUtils;
import com.aliyun.android.util.Helper;
import com.aliyun.android.util.CipherUtil.CipherAlgorithm;

/**
 * 上传加密或者压缩的Object
 * 
 * @author Michael
 */
public class PutZipEncObjectTask extends PutObjectTask {
    /**
     * 是否启用压缩模式
     */
    private boolean isZipped = false;

    /**
     * 是否启用加密模式
     */
    private boolean isEncrypted = false;

    /**
     * 加密密钥
     */
    private byte[] encryptKey;

    /**
     * 加密方式, 默认为des加密
     */
    private CipherAlgorithm cipherAlgorithm = CipherAlgorithm.DES;

    /**
     * 构造新实例
     * 
     * @param bucketName
     * @param objectKey
     * @param contentType
     * @param data
     * @param isZipped
     * @param isEncrypted
     * @param encryptKey
     */
    public PutZipEncObjectTask(String bucketName, String objectKey,
            String contentType, byte[] data, boolean isZipped,
            boolean isEncrypted, byte[] encryptKey) {
        super(bucketName, objectKey, contentType, data);
        this.isZipped = isZipped;
        this.isEncrypted = isEncrypted;
        this.encryptKey = encryptKey;
    }

    /**
     * 构造新实例
     * 
     * @param bucketName
     * @param objectKey
     * @param contentType
     * @param path
     * @param isZipped
     * @param isEncrypted
     * @param encryptKey
     */
    public PutZipEncObjectTask(String bucketName, String objectKey,
            String contentType, String path, boolean isZipped,
            boolean isEncrypted, byte[] encryptKey) {
        super(bucketName, objectKey, contentType, path);
        this.isZipped = isZipped;
        this.isEncrypted = isEncrypted;
        this.encryptKey = encryptKey;
    }

    /**
     * 执行上传压缩或者加密Object任务，首先把数据进行压缩或加密，然后调用父类的execute方法返回
     */
    @Override
    protected HttpResponse execute() throws OSSException {
        if (isEncrypted == true && Helper.isEmptyString(new String(encryptKey))) {
            throw new IllegalArgumentException("Encrypt key not set!");
        }
        try {
            if (isZipped) {
                byte[] zipped = CompressUtils.zipBytes(this.getData());
                this.setData(zipped);
                this.getObjectMetaData().addCustomAttr(X_OSS_META_COMPRESS,
                        "zip");
            }
            if (isEncrypted) {
                byte[] encrypted = CipherUtil.encrypt(this.getData(),
                        encryptKey, cipherAlgorithm);
                this.setData(encrypted);
                this.getObjectMetaData().addCustomAttr(X_OSS_META_ENCRYPT,
                        cipherAlgorithm.toString());
            }
        } catch (Exception e) {
            throw new OSSException(e);
        }
        return super.execute();
    }

    /** * @return the isZipped */
    public boolean isZipped() {
        return isZipped;
    }

    /** * @return the isEncrypted */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /** * @return the encryptKey */
    public byte[] getEncryptKey() {
        return encryptKey;
    }

    /** * @return the cipherAlgorithm */
    public CipherAlgorithm getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    /** * @param cipherAlgorithm the cipherAlgorithm to set */
    public void setCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }
}
