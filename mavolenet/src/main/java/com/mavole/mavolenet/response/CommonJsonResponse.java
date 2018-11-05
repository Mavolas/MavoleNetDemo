package com.mavole.mavolenet.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.mavole.mavolenet.ZirukHttpClient;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.exception.ZirukHttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by 宋棋安
 * on 2018/7/4.
 */
public class CommonJsonResponse {


    protected final String RESULT_CODE = "RequestStatus"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it


    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final String NETWORK_ERROR_MSG = "network_code_fail"; // the network relative error


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
     * OkHTTP 请求逻辑
     * @param okHttpClient OkHttpClient
     * @param client 封装请求的全部数据
     * @param callback 回调方法
     */
    public void request(OkHttpClient okHttpClient, ZirukHttpClient client, final DisposeDataListener callback){

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
            public void onResponse(Call call, Response response) throws IOException {

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

                            callback.onFailure(new ZirukHttpException(NETWORK_ERROR, NETWORK_ERROR_MSG));
                        }
                    });
                }

            }
        });

    }

    /**
     * 处理返回结果
     * @param callback
     * @param responseObj 返回的body字符串数据
     */
    private void handleResponse(final DisposeDataListener callback, final Object responseObj){

        if (responseObj == null) {
            callback.onFailure(new ZirukHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        if(callback.mType==null || callback.mType==String.class){

            callback.onSuccess(responseObj);
        }
        else {

            try {
                JSONObject result = new JSONObject(responseObj.toString());

                callback.onSuccess(mGson.fromJson(responseObj.toString(),callback.mType));


            } catch (JSONException e) {

                callback.onFailure(new ZirukHttpException(JSON_ERROR, e.getMessage()));
                e.printStackTrace();
            }

        }

    }
}
