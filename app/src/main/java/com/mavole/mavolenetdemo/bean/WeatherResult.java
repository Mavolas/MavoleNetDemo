package com.mavole.mavolenetdemo.bean;

import com.google.gson.JsonObject;

/**
 * Author by Andy
 * Date on 2018/12/13.
 */
public class WeatherResult {

    private int code;
    private String msg;
    private JsonObject data; // 数据部分也是一个bean，用JsonObject代替了

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
