/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密相关工具类
 * 
 * @author Michael
 */
public class CipherUtil {
    /**
     * 生成密钥, 通过Key.getEncoded()可以获取byte[]
     * 
     * @param algorithm
     * @return 生成的密钥
     * @throws Exception
     */
    public static Key generateKey(CipherAlgorithm algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm
                .toString());
        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;

    }

    /**
     * 根据Key.getEncoded()的内容还原密钥
     * 
     * @param key
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static Key toKey(byte[] key, CipherAlgorithm algorithm)
            throws Exception {
        if (algorithm == CipherAlgorithm.DES) {
            DESKeySpec des = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(algorithm.toString());
            SecretKey secretKey = keyFactory.generateSecret(des);
            return secretKey;
        } else if (algorithm == CipherAlgorithm.DESede) {
            DESedeKeySpec des = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(algorithm.toString());
            SecretKey secretKey = keyFactory.generateSecret(des);
            return secretKey;
        } else if (algorithm == CipherAlgorithm.AES
                || algorithm == CipherAlgorithm.IDEA) {
            SecretKey secretKey = new SecretKeySpec(key, algorithm.toString());
            return secretKey;
        } else {
            return null;
        }
    }

    /**
     * 加密数据
     * @param data
     * @param key
     * @param algorithm
     * @return 加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key,
            CipherAlgorithm algorithm) throws Exception {
        Key k = toKey(key, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm.toString());
        cipher.init(Cipher.ENCRYPT_MODE, k);  
        return cipher.doFinal(data);  
    }
    
    /**
     * 解密数据
     * @param data
     * @param key
     * @param algorithm
     * @return 解密后的数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key,
            CipherAlgorithm algorithm) throws Exception {
        Key k = toKey(key, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm.toString());
        cipher.init(Cipher.DECRYPT_MODE, k);  
        return cipher.doFinal(data);  
    }

    /**
     * 支持的加密算法
     * 
     * @author Michael
     *
     */
    public enum CipherAlgorithm {
        DES, AES, DESede, IDEA;
    }
}
