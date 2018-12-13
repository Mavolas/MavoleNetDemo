package com.mavole.mavolenetdemo.application;

import android.app.Application;

import com.mavole.mavolenet.configure.MavoHttpConfigure;

/**
 * Created by 宋棋安
 * on {2018-11-04}.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MavoHttpConfigure.init(this)
                .withApiHost("http://172.17.0.172:49842/api/")
                .withTimeOut(40)
                .configure();
    }
}
