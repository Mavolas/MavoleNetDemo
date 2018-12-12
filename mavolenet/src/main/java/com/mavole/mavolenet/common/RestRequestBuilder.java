package com.mavole.mavolenet.common;

import com.google.gson.Gson;
import com.mavole.mavolenet.RestClient;
import com.mavole.mavolenet.model.FileBean;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author by mavole, Email sgngqian@sina.com
 * Date on 2018/11/7.
 */
public class RestRequestBuilder {

    private String mUrl = null;
    private HttpMethod mMethod = null;
    private Map<String,Object> POST_PARAMS = RestCreator.getPostParams();
    private Map<String,Object> QUERY_PARAMS = RestCreator.getQueryParams();
    private Map<String,Object> HEADER_PARAMS = RestCreator.getHeaderParams();
    private RequestBody mBody = null;
    private String mDownLoad_Dir = null;
    private String mDownLoad_File_FullName = null;
    Gson gs = new Gson();

    public RestRequestBuilder(String url){
        this.mUrl = url;
    }

    public final RestRequestBuilder addQueryParams(WeakHashMap<String,Object> params){

        this.QUERY_PARAMS.putAll( params );
        return this;
    }

    public final RestRequestBuilder addQueryParams(String key,Object value){

        this.QUERY_PARAMS.put( key,value );
        return this;
    }

    public final RestRequestBuilder addPostParams(WeakHashMap<String,Object> params){

        this.POST_PARAMS.putAll( params );
        return this;
    }

    public final RestRequestBuilder addPostParams(String key,Object value){

        this.POST_PARAMS.put( key,value );
        return this;
    }

    public final RestRequestBuilder addHeaders(WeakHashMap<String,Object> params){

        this.HEADER_PARAMS.putAll( params );
        return this;
    }

    public final RestRequestBuilder addHeaders(String key,Object value){

        this.HEADER_PARAMS.put( key,value );
        return this;
    }

    //get
    public final RestClient get(){
        mMethod = HttpMethod.GET;
        RestClient client = build();
        return client;
    }

    //post with urlencoded
    public final RestClient post(){
        mMethod = HttpMethod.POST;
        this.mBody = buildFormEncodingBody();
        RestClient client = build();
        return client;
    }

    //put with json
    public final RestClient put(){
        mMethod = HttpMethod.PUT;
        this.mBody = buidJsonBody();
        RestClient client = build();
        return client;
    }

    //delete
    public final RestClient delete(){
        mMethod = HttpMethod.DELETE;
        RestClient client = build();
        return client;
    }

    //post file or mutipart
    public final RestClient postWithFiles(){
        mMethod = HttpMethod.POST_WITH_FILES;
        this.mBody = buildMultiPostRequestBody();
        RestClient client = build();
        return client;
    }

    //post json raw
    public final RestClient postJson(){
        mMethod = HttpMethod.POST_JSON;
        this.mBody = buidJsonBody();
        RestClient client = build();
        return client;
    }

    public final RestClient downloadConfig(){
        this.mDownLoad_Dir = RequestConstant.DOWNLOAD_DIR;
        this.mDownLoad_File_FullName = RequestConstant.DOWNLOAD_APK_FULLNAME;
        mMethod = HttpMethod.DOWNLOAD;
        RestClient client = build();
        return client;
    }

    public final RestClient downloadConfig(String download_dir, String fullName){
        this.mDownLoad_Dir = download_dir;
        this.mDownLoad_File_FullName = fullName;
        mMethod = HttpMethod.DOWNLOAD;
        RestClient client = build();
        return client;
    }


    private String buildQueryUrl(){

        StringBuilder urlBuilder = new StringBuilder(mUrl).append("?");
        if ( QUERY_PARAMS != null) {
            for (Map.Entry<String, Object> entry : QUERY_PARAMS.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return urlBuilder.substring(0, urlBuilder.length() - 1);
    }

    private RequestBody buidJsonBody(){
        String jsonStr = gs.toJson(POST_PARAMS);
        return  RequestBody.create( MediaType.parse( "application/json;charset=UTF-8" ) ,jsonStr);
    }

    private RequestBody buildFormEncodingBody(){

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : POST_PARAMS.keySet()) {
            builder.add(key, POST_PARAMS.get(key).toString());
        }
        return builder.build();

    }

    private  RequestBody buildMultiPostRequestBody() {

        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if ( POST_PARAMS != null) {

            for (Map.Entry<String, Object> entry : POST_PARAMS.entrySet()) {

                if (entry.getValue() instanceof File) {

                    String fileType = getMimeType(entry.getKey());
                    requestBody.addFormDataPart(entry.getKey(),entry.getKey(), RequestBody.create(MediaType.parse(fileType), (File) entry.getValue()));

                }else if (entry.getValue() instanceof FileBean) {

                    String fileType = getMimeType(entry.getKey());
                    requestBody.addFormDataPart( ((FileBean)entry.getValue()).key ,((FileBean)entry.getValue()).fileName, RequestBody.create(MediaType.parse(fileType), ((FileBean) entry.getValue()).file));

                } else if (entry.getValue() instanceof String) {

                    requestBody.addFormDataPart( entry.getKey(), (String) entry.getValue());
                }
            }
        }
        return requestBody.build();
    }

    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,
        }
        return contentType;
    }

    private final RestClient build(){

        mUrl = buildQueryUrl();
        return new RestClient(mUrl, mMethod, mBody, HEADER_PARAMS, mDownLoad_Dir, mDownLoad_File_FullName);
    }

}
