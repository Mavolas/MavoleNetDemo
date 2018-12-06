package com.mavole.mavolenet.common;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by 宋棋安
 * on 2018/9/25.
 */
public interface RestService {


    @GET
    Call<String> get(@Url String url);

    @POST
    Call<String> postJson(@Url String url, @Body RequestBody body);

    @Multipart
    @POST
    Call<String> postWithFile(@Url String url, @Body RequestBody body);


}
