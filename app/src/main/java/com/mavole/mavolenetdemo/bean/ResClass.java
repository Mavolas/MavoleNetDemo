package com.mavole.mavolenetdemo.bean;

/**
 * Author by Andy
 * Date on 2018/12/13.
 */
public class ResClass<T> {

    int code;

    String msg;

    T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
