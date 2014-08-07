package com.cuthead.controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Jiaqi Ning on 2014/8/7.
 */
public class VollyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @return
     */
    public static String getMessage(Object error) {
        if (error instanceof TimeoutError) {
            return "网络超时,稍后重试";
        }
        else if (isServerProblem(error)) {
            return "服务器端错误";
        }
        else if (isNetworkProblem(error)) {
            return "未联网";
        }
        return "未知错误";
    }

    /**
     * Determines whether the error is related to network
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }
    /**
     * Determines whether the error is related to server
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }



}
