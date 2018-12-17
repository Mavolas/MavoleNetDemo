package com.mavole.mavolenet.request;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.common.HttpMethod;
import com.mavole.mavolenet.common.RequestConstant;
import com.mavole.mavolenet.common.RestCreator;
import com.mavole.mavolenet.common.RestService;
import com.mavole.mavolenet.configure.ConfigType;
import com.mavole.mavolenet.configure.RestHttpConfigure;
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


    private static JsonDateType _jsonDateType = JsonDateType.fixFormatStr;
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
                                if ( RestHttpConfigure.getConfigurations().get( ConfigType.IS_SHOW_MESSAGE_FAIL.name() ) != null ){
                                    Boolean showMessageWhenFail = (Boolean) RestHttpConfigure
                                            .getConfigurations().get( ConfigType.IS_SHOW_MESSAGE_FAIL.name());
                                    if (showMessageWhenFail){
                                        int code = result.getInt("code");
                                        switch (code) {
                                            case 1:
                                                Toast.makeText(RestHttpConfigure.getApplicationContext(),
                                                        "账号或者密码不正确！请重新登陆系统后再次操作！", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 2:
                                                Toast.makeText(RestHttpConfigure.getApplicationContext(),
                                                        "账号或者密码不正确！请重新登陆系统后再次操作！", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 9:
                                                Toast.makeText(RestHttpConfigure.getApplicationContext(),
                                                        "系统出现未知错误，请联系系统管理员！",Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }

                                callback.onSuccess(  mGs.fromJson(response.body(), callback.mType));
                            }catch (JSONException e){
                                callback.onFailure(new CommonHttpException(RequestConstant.JSON_ERROR, e.getMessage()));
                            }
                        }
                    }else {
                        callback.onFailure(new CommonHttpException(response.code(), response.message()));
                    }

                }else {
                    callback.onFailure(new CommonHttpException(response.code(), response.message()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(new CommonHttpException(RequestConstant.NETWORK_ERROR, t.getMessage()));
            }
        });

    }

    //region 获取解析日期Json字符串
    public static String GetJsonDateParseStr(){
        return RequestConstant.DateTimeFormatString_Service2Android;
    }
}
