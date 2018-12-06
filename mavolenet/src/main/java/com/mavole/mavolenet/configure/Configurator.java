package com.mavole.mavolenet.configure;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by mavole
 * on {2018-11-04}.
 */
public class Configurator {

    private static final HashMap<Object,Object> MAVO_CONFIGS = new HashMap <>(  );

    //INTERCEPTOR 最终放到 MAVO_CONFIGS 里面
    private static final ArrayList<Interceptor> INTERCEPTOR = new ArrayList <>(  );

    private Configurator(){

        MAVO_CONFIGS.put( ConfigType.CONFIG_READY.name(),false );
    }

    public static Configurator getInstance(){

        return Holder.INSTANCE;
    }

    private static class Holder{

        private static final Configurator INSTANCE = new Configurator();
    }

    private void checkConfiguration(){

        final boolean isReady= ( boolean ) MAVO_CONFIGS.get( ConfigType.CONFIG_READY.name() );

        if ( !isReady ){

            throw new RuntimeException( "Configuartion is not ready, call configure" );
        }
    }

    @SuppressWarnings( "unchecked" )
    final <T> T getConfiguration(Enum<ConfigType> key){

        checkConfiguration();
        return ( T ) MAVO_CONFIGS.get( key.name() );

    }

    final HashMap<Object,Object> getConfigurations(){

        return MAVO_CONFIGS;
    }

    public final void configure(){

        MAVO_CONFIGS.put( ConfigType.CONFIG_READY.name(),true );
    }

    public final Configurator withApiHost(String host){

        MAVO_CONFIGS.put( ConfigType.API_HOST.name(),host );

        return this;
    }

    public final Configurator withTimeOut(int second){

        MAVO_CONFIGS.put( ConfigType.TIMEOUT.name(),second );

        return this;
    }

    public final Configurator withInterceptor(Interceptor interceptor){

        INTERCEPTOR.add( interceptor );
        MAVO_CONFIGS.put( ConfigType.INTERCEPTOR.name(),INTERCEPTOR );

        return this;
    }

    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors){

        INTERCEPTOR.addAll( interceptors );
        MAVO_CONFIGS.put( ConfigType.INTERCEPTOR.name(),INTERCEPTOR );

        return this;
    }

//    public final Configurator withOKHttpClient(OkHttpClient okHttpClient){
//
//        MAVO_CONFIGS.put( ConfigType.OKHTTPCLIENT.name(), okHttpClient );
//        return this;
//    }

}


