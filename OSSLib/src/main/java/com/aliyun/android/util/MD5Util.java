/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.util; 

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * 
 * @author luoruici 
 * 
 */
public class MD5Util {

    public static String getMD5String(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            
            return toHexString(algorithm.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    public static byte[] getMD5(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            
            return algorithm.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                hexString.append("0").append(Integer.toHexString(0xFF & b));
            } else {
                hexString.append(Integer.toHexString(0xFF & b));
            }
        }
        return hexString.toString().toUpperCase();
    }
}

