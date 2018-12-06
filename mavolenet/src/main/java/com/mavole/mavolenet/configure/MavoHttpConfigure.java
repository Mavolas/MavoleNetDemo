package com.mavole.mavolenet.configure;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by mavole
 * on {2018-11-04}.
 */
public class MavoHttpConfigure {

    //initial
    public static Configurator init(Context context){

        getConfigurations().put( ConfigType.APPLICATION_CONTEXT.name(),context.getApplicationContext() );

        return Configurator.getInstance();
    }

    //获取所有配置
    public static HashMap<Object,Object> getConfigurations(){

        return Configurator.getInstance().getConfigurations();
    }

    //获取单个种类配置
    public static <T> T getConfiguration(Enum<ConfigType> key){

        return Configurator.getInstance().getConfiguration( key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }
}
