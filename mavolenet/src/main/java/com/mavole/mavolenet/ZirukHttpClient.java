package com.mavole.mavolenet;


import android.content.Context;

import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.netconfig.ConfigType;
import com.mavole.mavolenet.netconfig.MavoHttp;
import com.mavole.mavolenet.request.RequestParams;
import com.mavole.mavolenet.response.CommonJsonResponse;
import com.mavole.mavolenet.utils.HttpsUtils;
import com.mavole.mavolenet.utils.StringUtils;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 宋棋安
 * on 2018/7/4.
 */
public class ZirukHttpClient {

    private Builder mBuilder;
    private OkHttpClient mOkHttpClient;
    private volatile static ZirukHttpClient mInstance = null;

    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static ZirukHttpClient initClient(OkHttpClient okHttpClient) {

        if (mInstance == null)
        {
            synchronized (ZirukHttpClient.class)
            {
                if (mInstance == null)
                {
                    mInstance = new ZirukHttpClient(okHttpClient);
                }
            }
        }
        return mInstance;
    }


    private ZirukHttpClient(OkHttpClient okHttpClient){
        this.mOkHttpClient=okHttpClient;
    }

    private ZirukHttpClient(OkHttpClient okHttpClient,Builder builder){
        this.mOkHttpClient=okHttpClient;
        this.mBuilder = builder;
    }


    public Request buildRequest(){

       Request.Builder builder =  new Request.Builder();


        if(mBuilder.method=="GET") {

            builder.url(buildGetRequest());
            builder.get();
        }

        else if (mBuilder.method =="POST"){

            try {
                builder.post(buildPostRequest());

            } catch (Exception e) {
                e.printStackTrace();
            }

            builder.url(mBuilder.url);
        }

        else if (mBuilder.method =="POSTWITHFILES"){

            try {
                builder.post(buildMultiPostRequest());

            } catch (Exception e) {
                e.printStackTrace();
            }

            builder.url(mBuilder.url);
        }

        return builder.build();
    }

    private String buildGetRequest(){

        StringBuilder urlBuilder = new StringBuilder(mBuilder.url).append("?");
        if ( Builder.mRequestParams != null) {
            for (Map.Entry<String, String> entry : Builder.mRequestParams.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        return urlBuilder.substring(0, urlBuilder.length() - 1);

    }

    private RequestBody buildPostRequest()  {

        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if ( Builder.mRequestParams != null) {
            for (Map.Entry<String, String> entry : Builder.mRequestParams.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        return mFormBody;

    }

    public  RequestBody buildMultiPostRequest() {

        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if ( Builder.mRequestParams != null) {

            for (Map.Entry<String, Object> entry : Builder.mRequestParams.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {

                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return requestBody.build();
    }

    /**
     * 异步请求
     * @param callback
     */
    public void enqueue(DisposeDataListener callback){

        CommonJsonResponse.getInstance().request(mOkHttpClient,this,callback);

    }

    public static  Builder newBuilder(){

        return new Builder();
    }

    public static class Builder{

        private String url;
        private String method;
        private Context mContext;
        protected static RequestParams mRequestParams;

        public ZirukHttpClient build(){

            synchronized (ZirukHttpClient.class)
            {
                if (mInstance == null)
                {
                    mInstance = new ZirukHttpClient(new OkHttpClient(),this);

                }else {
                    mInstance.mBuilder=this;
                }
            }
            return mInstance;
        }

        private Builder(){

            method = "GET";
        }


        /**
         * 上下文对象
         * @param context
         * @return
         */
        public Builder setContext(Context context){
             this.mContext= context;
             return this;
        }

        /**
         * 请求的url 地址
         * @param actionUrl
         * @return
         */
        public Builder url(String actionUrl){

            String urlRoot = "";

            if ( StringUtils.isBlank(urlRoot)) {
                 urlRoot = ( String ) MavoHttp.getConfigurations().get( ConfigType.API_HOST.name() );
            }

            String fullUrl = urlRoot;

            if ( StringUtils.startsWith(actionUrl, "/"))
                actionUrl = StringUtils.right(actionUrl, actionUrl.length()-1);
            fullUrl += actionUrl;

            this.url = fullUrl;
            return this;
        }

        /**
         * 添加请求参数
         * @param requestParams
         * @return
         */
        public Builder addParams(RequestParams requestParams){

            this.mRequestParams=requestParams;

            return this;

        }

        /**
         * Get 请求
         * @return
         */
        public Builder get(){

            method="GET";
            return this;
        }

        /**
         * Post 请求
         * @return
         */
        public Builder post(){

            method="POST";
            return this;
        }

        /**
         * 带文件的Post请求
         * @return
         */
        public Builder postWithFiles(){

            method="POSTWITHFILES";
            return this;
        }


    }
}
