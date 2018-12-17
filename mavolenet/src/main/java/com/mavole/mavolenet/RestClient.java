package com.mavole.mavolenet;

import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.callback.DownloadListener;
import com.mavole.mavolenet.common.HttpMethod;
import com.mavole.mavolenet.common.RestRequestBuilder;
import com.mavole.mavolenet.download.DownloadHandler;
import com.mavole.mavolenet.request.RestRequest;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author by Andy
 * Date on 2018/12/6.
 */
public class RestClient {

    private String mUrl = null;
    private HttpMethod mMethod = null;
    private RequestBody mBody = null;
    private Map<String, Object> mHeader = null;
    private String mDownLoad_Dir = null;
    private String mDownLoad_File_FullName = null;

    public RestClient(String url, HttpMethod method, RequestBody body, Map<String, Object> header , String downloadDir, String fullName) {
        this.mUrl = url;
        this.mMethod = method;
        this.mBody = body;
        this.mHeader = header;
        this.mDownLoad_Dir = downloadDir;
        this.mDownLoad_File_FullName = fullName;
    }

    public static RestRequestBuilder builder(String url){

        return new RestRequestBuilder(url);
    }

    public void enqueue( DisposeDataListener callback) {

        switch (mMethod) {
            case GET:
                RestRequest.request(HttpMethod.GET, mUrl, mBody, mHeader, callback);
                break;
            case POST_JSON:
                RestRequest.request(HttpMethod.POST_JSON, mUrl, mBody, mHeader, callback);
                break;
            case POST_WITH_FILES:
                RestRequest.request(HttpMethod.POST_WITH_FILES, mUrl, mBody, mHeader, callback);
                break;
            case POST:
                RestRequest.request(HttpMethod.POST, mUrl, mBody, mHeader, callback);
                break;
            case PUT:
                RestRequest.request(HttpMethod.PUT, mUrl, mBody, mHeader, callback);
                break;
            case DELETE:
                RestRequest.request(HttpMethod.DELETE, mUrl, mBody, mHeader, callback);
                break;
            default:
                break;
        }
    }

    public void download( DownloadListener callback) {

        new DownloadHandler(mUrl, mDownLoad_Dir, mDownLoad_File_FullName, callback).HandleDownload();
    }
}
