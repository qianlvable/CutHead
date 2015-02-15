package com.cuthead.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Jiaqi Ning on 2014/7/25.
 */
public class MyApplication extends Application {
    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;


    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        mInstance = this;
    }
}
