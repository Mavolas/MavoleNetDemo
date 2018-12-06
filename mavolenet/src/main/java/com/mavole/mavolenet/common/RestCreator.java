package com.mavole.mavolenet.common;


import com.mavole.mavolenet.configure.ConfigType;
import com.mavole.mavolenet.configure.MavoHttpConfigure;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 宋棋安
 * on 2018/9/25.
 */
public class RestCreator {


    public static WeakHashMap<String , Object> getPostParams(){

       return ParamsHolder.POST_PARAMS;
    }

    public static WeakHashMap<String , Object> getQueryParams(){

        return ParamsHolder.QUERY_PARAMS;
    }

    public static RestService getRestService(){

        return RestServiceHolder.REST_SERVICE;
    }

    private static final class ParamsHolder{

        public  static  final WeakHashMap<String ,Object> POST_PARAMS = new WeakHashMap<>(  );
        public  static  final WeakHashMap<String ,Object> QUERY_PARAMS = new WeakHashMap<>(  );
    }

    private static final class RetrofitHolder{

        private static final String BASE_URL = (String) MavoHttpConfigure.getConfigurations().get( ConfigType.API_HOST.name());

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .client( OKHttpHolder.OK_HTTP_CLIENT )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

    }

    private static final class OKHttpHolder{

        private static final int TIME_OUT = 60;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final int timeout = MavoHttpConfigure.getConfiguration(ConfigType.TIMEOUT );
        private static final ArrayList<Interceptor> INTERCEPTORS = MavoHttpConfigure.getConfiguration(ConfigType.INTERCEPTOR );

        private static OkHttpClient.Builder addInterceptor(){
            if ( INTERCEPTORS != null && !INTERCEPTORS.isEmpty() ){
                for (Interceptor interceptor:INTERCEPTORS) {
                    BUILDER.addInterceptor( interceptor );
                }
                
            }
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout( timeout==0?TIME_OUT:timeout, TimeUnit.SECONDS )
                .build();

    }

    private static final class RestServiceHolder{

        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create( RestService.class );
    }


}
