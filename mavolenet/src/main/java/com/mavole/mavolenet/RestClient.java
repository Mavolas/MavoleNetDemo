package com.mavole.mavolenet;

import com.mavole.mavolenet.callback.DisposeDataListener;
import com.mavole.mavolenet.common.HttpMethod;
import com.mavole.mavolenet.common.RestRequestBuilder;
import com.mavole.mavolenet.request.RestRequest;

import okhttp3.RequestBody;

/**
 * Author by Andy
 * Date on 2018/12/6.
 */
public class RestClient {

    private String mUrl = null;
    private HttpMethod mMethod = null;
    private RequestBody mBody = null;

    public RestClient(String url, HttpMethod method, RequestBody body) {
        mUrl = url;
        mMethod = method;
        mBody = body;
    }

    public static RestRequestBuilder builder(String url){

        return new RestRequestBuilder(url);
    }

    public void enqueue(DisposeDataListener callback) {

        switch (mMethod) {
            case GET:
                RestRequest.request(HttpMethod.GET, mUrl, mBody, callback);
                break;
            case POST_JSON:
                RestRequest.request(HttpMethod.POST_JSON, mUrl, mBody, callback);
                break;
            case POST_WITH_FILES:
                RestRequest.request(HttpMethod.POST_WITH_FILES, mUrl, mBody, callback);
                break;
            default:
                break;

        }
    }
}
