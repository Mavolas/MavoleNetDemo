package com.mavole.mavolenet.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.mavole.mavolenet.MavoHttpClient;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.exception.ZirukHttpException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by mavole
 * on 2018/7/4.
 */
public class CommonJsonResponse {

    private static final int NETWORK_ERROR = -1; // the network relative error
    private static final int JSON_ERROR = -2; // the JSON relative error
    private static final String EMPTY_MSG = "CONTENT_IS_NULL";
    private static final String NETWORK_ERROR_MSG = "NETWORK_CODE_FAIL"; // the network relative error

    private Handler mHandler ;
    private Gson mGson;
    private static CommonJsonResponse mInstance;

    private CommonJsonResponse(){

        mGson = new Gson();
        mHandler = new Handler( Looper.getMainLooper()) ;

    }

    public static synchronized CommonJsonResponse getInstance(){

        if(mInstance==null)
            mInstance = new CommonJsonResponse();

        return mInstance;
    }


    /**
     * OkHTTP
     * @param okHttpClient OkHttpClient
     * @param client
     * @param callback
     */
    public void request(OkHttpClient okHttpClient, MavoHttpClient client, final DisposeDataListener callback){

        if(okHttpClient == null){

            throw new NullPointerException("ZirukHttp okHttpClient is null");
        }

        if(callback == null){

            throw new NullPointerException("ZirukHttp callback is null");
        }

        if(client == null){

            throw new NullPointerException("ZirukHttp client is null");
        }

        okHttpClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException ioexception) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        callback.onFailure(new ZirukHttpException(NETWORK_ERROR, ioexception));
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(response.isSuccessful()){

                    final String result  =  response.body().string();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            handleResponse(callback,result);

                        }
                    });

                    if(response.body() !=null){
                        response.body().close();
                    }

                }else {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            callback.onFailure(new ZirukHttpException(NETWORK_ERROR, NETWORK_ERROR_MSG +" : "+ response.code()));
                        }
                    });
                }

            }
        });

    }

    /**
     *
     * @param callback
     * @param responseObj
     */
    private void handleResponse(final DisposeDataListener callback, final Object responseObj){

        if (responseObj == null) {
            callback.onFailure(new ZirukHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        if(callback.mType == null || callback.mType == String.class){

            callback.onSuccess(responseObj);
        }
        else {

            try {

                JSONObject result = new JSONObject(responseObj.toString());
                callback.onSuccess(mGson.fromJson(responseObj.toString(),callback.mType));

            } catch (JSONException eo) {

                try{

                    JSONArray result = new JSONArray(responseObj.toString());
                    callback.onSuccess(mGson.fromJson(responseObj.toString(),callback.mType));

                }catch (JSONException ea){

                    callback.onFailure(new ZirukHttpException(JSON_ERROR, ea.getMessage()));
                    ea.printStackTrace();

                }
            }

        }

    }
}
