package com.mavole.mavolenet.netconfig;

import com.mavole.mavolenet.ZirukHttpClient;
import com.mavole.mavolenet.internal.InternalNetworking;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by 宋棋安
 * on {2018-11-04}.
 */
public class Configurator {

    private static final HashMap<Object,Object> MAVO_CONFIGS = new HashMap <>(  );

    private Configurator(){

        MAVO_CONFIGS.put( ConfigType.CONFIG_READY.name(),false );
    }

    public static Configurator getInstance(){

        return Holder.INSTANCE;
    }

    //静态内部类，线程安全的懒汉模式
    private static class Holder{

        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure(){

        MAVO_CONFIGS.put( ConfigType.CONFIG_READY.name(),true );

        OkHttpClient okHttpClient = (OkHttpClient) MavoHttp.getConfigurations().get(ConfigType.OKHTTPCLIENT.name() );

        if ( okHttpClient != null ){

            ZirukHttpClient.initClient(okHttpClient);

        }else {
            ZirukHttpClient.initClient(InternalNetworking.getClient());
        }

    }

    final HashMap<Object,Object> getMavoHttpConfigs(){

        return MAVO_CONFIGS;
    }

    private void checkConfiguration(){

        final boolean isReady= ( boolean ) MAVO_CONFIGS.get( ConfigType.CONFIG_READY.name() );

        if ( !isReady ){

            throw new RuntimeException( "Configuartion is not ready, call configure" );
        }
    }

    public final Configurator withApiHost(String host){

        MAVO_CONFIGS.put( ConfigType.API_HOST.name(),host );

        return this;
    }

    public final Configurator withTimeOut(int timeOut){

        MAVO_CONFIGS.put( ConfigType.TIMEOUT.name(),timeOut );

        return this;
    }

    public final Configurator withOKHttpClient(OkHttpClient okHttpClient){

        MAVO_CONFIGS.put( ConfigType.OKHTTPCLIENT.name(), okHttpClient );

        return this;
    }

    @SuppressWarnings( "unchecked" )
    final <T> T getConfiguration(Enum<ConfigType> key){

        checkConfiguration();
        return ( T ) MAVO_CONFIGS.get( key.name() );

    }

}


