package com.mavole.mavolenet.netconfig;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by 宋棋安
 * on {2018-11-04}.
 */
public class MavoHttp {

    public static Configurator init(Context context){

        getConfigurations().put( ConfigType.APPLICATION_CONTEXT.name(),context.getApplicationContext() );

        return Configurator.getInstance();
    }

    public static HashMap<Object,Object> getConfigurations(){

        return Configurator.getInstance().getMavoHttpConfigs();
    }
}
