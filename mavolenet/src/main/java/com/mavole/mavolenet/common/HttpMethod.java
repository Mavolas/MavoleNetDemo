package com.mavole.mavolenet.common;

import retrofit2.http.DELETE;
import retrofit2.http.PUT;

/**
 * Created by 宋棋安
 * on 2018/9/25.
 */
public enum HttpMethod {
    GET,
    POST,      //post urlencoded
    POST_JSON, //post raw json
    POST_WITH_FILES,
    PUT,
    DELETE,
    DOWNLOAD
}
