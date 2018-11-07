package com.mavole.mavolenetdemo.application;

import android.app.Application;

import com.mavole.mavolenet.ZirukHttpClient;
import com.mavole.mavolenet.netconfig.MavoHttp;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

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

        MavoHttp.init(this)
                .withApiHost("http://172.17.0.172:59596/api")
                .configure();

    }
}
