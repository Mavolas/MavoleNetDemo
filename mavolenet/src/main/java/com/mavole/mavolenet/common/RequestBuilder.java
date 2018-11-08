package com.mavole.mavolenet.common;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Author by mavole, Email sgngqian@sina.com
 * Date on 2018/11/7.
 */
public interface RequestBuilder {

    RequestBuilder addPathParameter(String key, String value);

    RequestBuilder addPathParameter(Map<String, String> pathParameterMap);

    RequestBuilder addFormParameter(String key, Object value);

    RequestBuilder addFormParameter(Map<String, Object> queryParameterMap);

    RequestBuilder setOkHttpClient(OkHttpClient okHttpClient);

}
