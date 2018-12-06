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

//        //配置网络框架的参数
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .hostnameVerifier(new HostnameVerifier()
//                {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session)
//                    {
//                        return true;
//                    }
//                })
//                .build();

        MavoHttpConfigure.init(this)
                .withApiHost("http://172.17.0.172:49842/api/")
                .withTimeOut(40)
                .configure();

    }
}
