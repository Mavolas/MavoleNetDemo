package com.mavole.mavolenet;


import android.content.Context;

import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.common.Method;
import com.mavole.mavolenet.netconfig.ConfigType;
import com.mavole.mavolenet.netconfig.MavoHttp;
import com.mavole.mavolenet.request.RequestParams;
import com.mavole.mavolenet.response.CommonJsonResponse;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mavole
 * on 2018/7/4.
 */
public class MavoHttpClient {

    private Builder mBuilder;
    private OkHttpClient mOkHttpClient;
    private volatile static MavoHttpClient mInstance = null;

    public static MavoHttpClient initClient(OkHttpClient okHttpClient) {

        if (mInstance == null)
        {
            synchronized (MavoHttpClient.class)
            {
                if (mInstance == null)
                {
                    mInstance = new MavoHttpClient(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    private MavoHttpClient(OkHttpClient okHttpClient){
        this.mOkHttpClient=okHttpClient;
    }

    private MavoHttpClient(OkHttpClient okHttpClient, Builder builder){
        this.mOkHttpClient=okHttpClient;
        this.mBuilder = builder;
    }

    public Request buildRequest(){

       Request.Builder builder =  new Request.Builder();


        if(mBuilder.method==Method.GET) {

            builder.url(buildGetRequest());
            builder.get();
        }

        else if (mBuilder.method == Method.POST){

            try {
                builder.post(buildPostRequest());

            } catch (Exception e) {
                e.printStackTrace();
            }

            builder.url(mBuilder.url);
        }

        else if (mBuilder.method == Method.POSTWITHFILES){

            try {
                builder.post(buildMultiPostRequest());

            } catch (Exception e) {
                e.printStackTrace();
            }

            builder.url(buildGetRequest());
        }

        return builder.build();
    }

    private String buildGetRequest(){

        StringBuilder urlBuilder = new StringBuilder(mBuilder.url).append("?");
        if ( Builder.mRequestParams != null) {
            for (Map.Entry<String, String> entry : Builder.mRequestParams.mPathParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        return urlBuilder.substring(0, urlBuilder.length() - 1);

    }

    private RequestBody buildPostRequest()  {

        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if ( Builder.mRequestParams != null) {
            for (Map.Entry<String, String> entry : Builder.mRequestParams.mPathParams.entrySet()) {
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

            for (Map.Entry<String, Object> entry : Builder.mRequestParams.mFileParams.entrySet()) {

                if (entry.getValue() instanceof File) {

                    String fileType = getMimeType(entry.getKey());
                    requestBody.addFormDataPart(entry.getKey(),entry.getKey(), RequestBody.create(MediaType.parse(fileType), (File) entry.getValue()));

                } else if (entry.getValue() instanceof String) {

                    requestBody.addFormDataPart( entry.getKey(), (String) entry.getValue());
                }
            }
        }
        return requestBody.build();
    }


    /**
     *
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,
        }
        return contentType;
    }


    /**
     *
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
        private int method;
        private Context mContext;
        protected static RequestParams mRequestParams;

        public MavoHttpClient build(){

            synchronized (MavoHttpClient.class)
            {
                if (mInstance == null)
                {
                    mInstance = new MavoHttpClient(new OkHttpClient(),this);

                }else {
                    mInstance.mBuilder=this;
                }
            }
            return mInstance;
        }

        private Builder(){

            method = Method.GET;
        }


        /**
         *
         * @param context
         * @return
         */
        public Builder setContext(Context context){
             this.mContext= context;
             return this;
        }

        /**
         *
         * @param actionUrl
         * @return
         */
        public Builder url(String actionUrl){

            String fullUrl = "";

            if ( StringUtils.isBlank(fullUrl)) {
                 fullUrl = ( String ) MavoHttp.getConfigurations().get( ConfigType.API_HOST.name() );
                 if (! StringUtils.endsWith(fullUrl,"/")){
                     fullUrl += "/";
                 }
            }
            if ( StringUtils.startsWith(actionUrl, "/"))
                actionUrl = StringUtils.right(actionUrl, actionUrl.length()-1);

            fullUrl += actionUrl;
            this.url = fullUrl;
            return this;
        }

        /**
         *
         * @param requestParams
         * @return
         */
        public Builder addParams(RequestParams requestParams){

            this.mRequestParams=requestParams;
            return this;

        }

        /**
         *
         * @return
         */
        public Builder get(){

            method = Method.GET;
            return this;
        }

        /**
         *
         * @return
         */
        public Builder post(){

            method = Method.POST;
            return this;
        }

        /**
         *
         * @return
         */
        public Builder postWithFiles(){

            method = Method.POSTWITHFILES;
            return this;
        }



    }
}
