package com.cuthead.app;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Jiaqi Ning on 2014/7/25.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
