/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 * 
 * @author Michael
 */
public class CompressUtils {
    /**
     * 使用zip压缩byte数组
     * @param source
     * @return 压缩后的byte数组
     * @throws Exception
     */
    public static byte[] zipBytes(byte[] source) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.putNextEntry(new ZipEntry("1"));
        zos.write(source, 0, source.length);
        zos.closeEntry();
        zos.close();
        
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }
    
    /**
     * 解压缩
     * @param source
     * @return 解压缩后的byte数组
     * @throws Exception
     */
    public static byte[] unzipBytes(byte[] source) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(source);
        ZipInputStream zis = new ZipInputStream(bais);
        
        ZipEntry zipEntry = zis.getNextEntry();
        if (zipEntry == null) {
            throw new IllegalArgumentException("ZipEntry is null, not in zip format");
        }
        int bufSize = 1024;
        byte[] data = new byte[bufSize];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while ((c = zis.read(data, 0, bufSize)) != -1) {
            baos.write(data, 0, c);
        }
        
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }
}
