package com.mavole.mavolenet.common;

import com.mavole.mavolenet.configure.ConfigType;
import com.mavole.mavolenet.configure.MavoHttpConfigure;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 宋棋安
 * on 2018/9/25.
 */
public class RestCreator {

    private static final int DEFAULT_TIME_OUT = 20;

    public static WeakHashMap<String , Object> getPostParams(){

       return ParamsHolder.POST_PARAMS;
    }

    public static WeakHashMap<String , Object> getQueryParams(){

        return ParamsHolder.QUERY_PARAMS;
    }

    public static WeakHashMap<String , Object> getHeaderParams(){

        return ParamsHolder.HEADER_PARAMS;
    }

    public static RestService getRestService(){

        return RestServiceHolder.REST_SERVICE;
    }

    private static final class ParamsHolder{

        public static final WeakHashMap<String ,Object> POST_PARAMS = new WeakHashMap<>(  );
        public static final WeakHashMap<String ,Object> QUERY_PARAMS = new WeakHashMap<>(  );
        public static final WeakHashMap<String ,Object> HEADER_PARAMS = new WeakHashMap<>(  );
    }

    private static final class RetrofitHolder{

        private static final String BASE_URL = (String) MavoHttpConfigure.getConfigurations().get( ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .client( OKHttpHolder.initialOkHttpClient() )
                .addConverterFactory( ScalarsConverterFactory.create() )
                .build();

    }

    private static final class OKHttpHolder{

        private static OkHttpClient initialOkHttpClient(){

             final OkHttpClient OKHTTPCLIENT = MavoHttpConfigure.getConfiguration(ConfigType.OKHTTP_CLIENT );
             final Integer TIMEOUT = MavoHttpConfigure.getConfiguration( ConfigType.TIMEOUT);
             final ArrayList<Interceptor> INTERCEPTORS = MavoHttpConfigure.getConfiguration(ConfigType.INTERCEPTOR );

             if (OKHTTPCLIENT != null) {
                 return OKHTTPCLIENT;
             } else {
                 final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
                 if ( INTERCEPTORS != null && !INTERCEPTORS.isEmpty() ){
                     for (Interceptor interceptor:INTERCEPTORS) {
                         BUILDER.addInterceptor( interceptor );
                     }
                 }
                 OkHttpClient OK_HTTP_CLIENT = BUILDER
                         .connectTimeout(TIMEOUT == null ? DEFAULT_TIME_OUT:TIMEOUT, TimeUnit.SECONDS )
                         .readTimeout(TIMEOUT == null ? DEFAULT_TIME_OUT:TIMEOUT, TimeUnit.SECONDS)
                         .build();
                 return OK_HTTP_CLIENT;
             }
        }
    }

    private static final class RestServiceHolder{

        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create( RestService.class );
    }

}
