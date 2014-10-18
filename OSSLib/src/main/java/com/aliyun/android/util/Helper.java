/**
 * Copyright (c) 2012 The Wiseserc. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.aliyun.android.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 工具类
 * 
 * @author Michael
 */
public class Helper {
    /**
     * 获取当前GMT时间
     * 
     * @return GMT时间的字符串表示
     */
    public static String getGMTDate() {
        return getGMTDate(new Date());
    }

    /**
     * 获取GMT时间
     */
    public static String getGMTDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(
                "E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 解析日期字符串得到对象
     * 
     * @param dateStr
     *            表示日期的字符串
     * @return 日期对象
     * @throws ParseException
     */
    public static Date getGMTDateFromString(String dateStr) throws ParseException {
        
        return getDateFromString("E, dd MMM yyyy HH:mm:ss 'GMT'", dateStr);
    }
    
    /**
     * 获取Hmac sha1的签名
     * 
     * @return base64 encode后的签名字符串
     */
    public static String getHmacSha1Signature(String value, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(value.getBytes());
        return new String(Base64.encode(rawHmac));
    }

    /**
     * 判断是否为空字符串
     */
    public static boolean isEmptyString(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 将inputStream的内容转为String
     */
    public static String inputStream2String(InputStream inputStream)
            throws Exception {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, count);
        }
        String convertedBuffer = new String(outputStream.toByteArray());
        outputStream.close();
        return convertedBuffer;
    }

    /**
     * 将String格式的日期按默认格式转换为Date对象* @param dateString /** * @return /** * @throws
     * ParseException
     */
    public static Date getDateFromString(String dateString)
            throws ParseException {
        return getDateFromString("yyyy-MM-dd'T'hh:mm:ss.S'Z'", dateString);
    }

    /**
     * 将String格式的日起按给定格式* @param format 转换为Date对象 /** * @param dateString /** * @return
     * /** * @throws ParseException
     */
    public static Date getDateFromString(String format, String dateString)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.parse(dateString);
    }

    /**
     * 格式化文件夹路径 * @param path /** * @return
     */
    public static String formatPath(String path) {
        if (Helper.isEmptyString(path)) {
            return "";
        } else {
            return path.endsWith("/") ? path : path + "/";
        }
    }
//    
//    public static String inputStream2String(InputStream is) {
//        if (is != null) {
//            StringBuilder sb = new StringBuilder();
//            String line;
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line).append("\n");
//                }
//            } catch (Exception e) {
//                Log.i("inputstream2string", e.toString());
//            } finally {
//                is.close();
//            }
//            return sb.toString();
//        } else {
//            return "";
//        }
//    }
}
