package com.cuthead.controller;

/**
 * Created by Jiaqi Ning on 10/2/2015.
 */
public class OthersUtil {
    /**检查手机号*/
    public static boolean isPhoneValid(String phone){
        if (phone.length() != 11)
            return false;
        String[] prefix  = {"130", "131", "132",
                "133", "134", "135",
                "136", "137", "138",
                "139", "150", "151",
                "152", "153", "155",
                "156", "157", "158",
                "159", "170", "180",
                "181", "182", "183",
                "184", "185", "186",
                "187", "188", "189"};
        for (String pre : prefix){
            if (phone.startsWith(pre))
                return true;
        }
        return false;
    }
}
