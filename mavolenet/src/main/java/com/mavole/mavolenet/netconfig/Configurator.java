package com.mavole.mavolenet.netconfig;

import com.mavole.mavolenet.MavoHttpClient;
import com.mavole.mavolenet.internal.InternalNetworking;

import java.util.HashMap;

import okhttp3.OkHttpClient;

/**
 * Created by mavole
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

    //
    private static class Holder{

        private static final Configurator INSTANCE = new Configurator();
    }

    public final void configure(){

        MAVO_CONFIGS.put( ConfigType.CONFIG_READY.name(),true );

        OkHttpClient okHttpClient = (OkHttpClient) MavoHttp.getConfigurations().get(ConfigType.OKHTTPCLIENT.name() );

        if ( okHttpClient != null ){

            MavoHttpClient.initClient(okHttpClient);

        }else {
            MavoHttpClient.initClient(InternalNetworking.getClient());
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


