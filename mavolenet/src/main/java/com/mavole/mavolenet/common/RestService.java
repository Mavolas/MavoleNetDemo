package com.mavole.mavolenet.common;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 宋棋安
 * on 2018/9/25.
 */
public interface RestService {

    @GET
    Call<String> get(@Url String url, @HeaderMap Map<String, Object> header);

    @POST
    Call<String> post(@Url String url , @Body RequestBody body, @HeaderMap Map<String, Object> header );

    @POST
    Call<String> postJson(@Url String url, @Body RequestBody body, @HeaderMap Map<String, Object> header);

    @POST
    Call<String> postWithFile(@Url String url, @Body RequestBody body, @HeaderMap Map<String, Object> header);

    @PUT
    Call<String> put(@Url String url , @Body RequestBody body, @HeaderMap Map<String, Object> header);

    @DELETE
    Call<String> delete(@Url String url, @HeaderMap Map<String, Object> header);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);

}
