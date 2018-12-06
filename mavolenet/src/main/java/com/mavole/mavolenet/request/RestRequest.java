package com.mavole.mavolenet.request;

import com.google.gson.Gson;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.common.HttpMethod;
import com.mavole.mavolenet.common.RestCreator;
import com.mavole.mavolenet.common.RestService;
import com.mavole.mavolenet.exception.CommonHttpException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Andy
 * Date on 2018/12/6.
 */
public class RestRequest {


    private static final int NETWORK_ERROR = -1; // the network relative error
    private static final int JSON_ERROR = -2; // the JSON relative error
    private static Gson mGs = new Gson();

    public static void request(HttpMethod method, String url, RequestBody body, final DisposeDataListener callback){

        final RestService service = RestCreator.getRestService();

        Call<String> call = null;

        switch (method){
            case GET:
                call = service.get( url );
                break;
            case POST_JSON:
                call = service.postJson( url, body );
                break;
            case POST_WITH_FILES:
                call = service.postWithFile( url, body );
                break;
            default:
                break;

        }

        if ( call !=null ){
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if ( response.isSuccessful() ){
                        if ( call.isExecuted() ){

                            if(callback.mType == null || callback.mType == String.class){
                                callback.onSuccess(response.body());
                            }else {

                                try {
                                    //判断解析是否正确
                                    JSONObject result = new JSONObject(response.body());
                                    callback.onSuccess(mGs.fromJson(response.body(), callback.mType));
                                }catch (JSONException e){
                                    callback.onFailure(new CommonHttpException(JSON_ERROR, e.getMessage()));
                                }
                            }

                            callback.onSuccess( response.body());
                        }

                    }else {
                        callback.onFailure(new CommonHttpException(NETWORK_ERROR, response.code()));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    callback.onFailure(new CommonHttpException(NETWORK_ERROR, t));
                }
            });

        }

    }
}
