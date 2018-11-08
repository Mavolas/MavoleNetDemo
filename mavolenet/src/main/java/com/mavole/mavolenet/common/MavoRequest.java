package com.mavole.mavolenet.common;

import com.mavole.mavolenet.model.MultipartStringBody;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Author by mavole, Email sgngqian@sina.com
 * Date on 2018/11/7.
 */
public class MavoRequest {

    private HashMap<String, String> mBodyParameterMap = new HashMap<>();
    private HashMap<String, String> mUrlEncodedFormBodyParameterMap = new HashMap<>();
    private HashMap<String, MultipartStringBody> mMultiPartParameterMap = new HashMap<>();

    private Type mType = null;
    private OkHttpClient mOkHttpClient = null;


    public MavoRequest(PostRequestBuilder builder) {

        this.mOkHttpClient = builder.mOkHttpClient;

    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static class PostRequestBuilder implements RequestBuilder{

        private OkHttpClient mOkHttpClient;

        @Override
        public RequestBuilder addPathParameter(String key, String value) {
            return null;
        }

        @Override
        public RequestBuilder addPathParameter(Map<String, String> pathParameterMap) {
            return null;
        }

        @Override
        public RequestBuilder addFormParameter(String key, Object value) {
            return null;
        }

        @Override
        public RequestBuilder addFormParameter(Map<String, Object> queryParameterMap) {
            return null;
        }

        @Override
        public RequestBuilder setOkHttpClient(OkHttpClient okHttpClient) {
            return null;
        }


        public MavoRequest build(){
            return new MavoRequest(this);
        }
    }


}
