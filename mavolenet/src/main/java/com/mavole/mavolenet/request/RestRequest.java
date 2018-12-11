package com.mavole.mavolenet.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.common.HttpMethod;
import com.mavole.mavolenet.common.RestCreator;
import com.mavole.mavolenet.common.RestService;
import com.mavole.mavolenet.exception.CommonHttpException;
import com.mavole.mavolenet.json.JsonDateDeserializer_FixFormat;
import com.mavole.mavolenet.json.JsonDateDeserializer_SlashDataNumberSlash;
import com.mavole.mavolenet.json.JsonDateType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author by Andy
 * Date on 2018/12/6.
 */
public class RestRequest {

    private static String DateTimeFormatString_Android2Service = "yyyy-MM-dd HH:mm:ss.SSS";
    private static JsonDateType _jsonDateType = JsonDateType.fixFormatStr;
    private static String DateTimeFormatString_Service2Android = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final int NETWORK_ERROR = -1; // the network relative error
    private static final int JSON_ERROR = -2; // the JSON relative error
    private static Gson mGs = new Gson();

    public static void request(HttpMethod method, String url
            ,RequestBody body, Map<String, Object> header
            ,final DisposeDataListener callback){

        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        switch (method){
            case GET:
                call = service.get( url, header );
                break;
            case POST_JSON:
                call = service.postJson( url, body, header );
                break;
            case POST_WITH_FILES:
                call = service.postWithFile( url, body, header );
                break;
            case POST:
                call = service.post( url, body, header );
                break;
            case PUT:
                call = service.put( url, body, header );
                break;
            case DELETE:
                call = service.delete( url, header );
                break;
            default:
                break;
        }

        if (call == null )
            throw new RuntimeException("retrofit call is null, please set a call method!");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if ( response.isSuccessful() ){
                    if ( call.isExecuted() ){
                        if(callback.mType == null || callback.mType == String.class){
                            callback.onSuccess(response.body());
                        }else {
                            try {
                                //C# mvc 时间格式转换
                                if (_jsonDateType == JsonDateType.slash_Date_Number_slash)
                                    mGs = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer_SlashDataNumberSlash()).create();
                                else if (_jsonDateType == JsonDateType.fixFormatStr)
                                    mGs = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer_FixFormat()).create();
                                //判断解析是否正确
                                JSONObject result = new JSONObject(response.body());
                                callback.onSuccess(mGs.fromJson(response.body(), callback.mType));
                            }catch (JSONException e){
                                callback.onFailure(new CommonHttpException(JSON_ERROR, e.getMessage()));
                            }
                        }
                    }else {
                        callback.onFailure(new CommonHttpException(NETWORK_ERROR, response.message()+" code:"+ response.code()));
                    }

                }else {
                    callback.onFailure(new CommonHttpException(NETWORK_ERROR, response.message()+" code:"+ response.code()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(new CommonHttpException(NETWORK_ERROR, t.getMessage()));
            }
        });

    }

    //region 获取解析日期Json字符串
    public static String GetJsonDateParseStr(){
        return DateTimeFormatString_Service2Android;
    }
}
