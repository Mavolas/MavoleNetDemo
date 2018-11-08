package com.mavole.mavolenet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by mavole, Email sgngqian@sina.com
 * Date on 2018/11/7.
 */
public class ResponseCls<T> {
    private boolean status;
    private Integer code;
    private List<String> messages = new ArrayList();
    private T data;

    public ResponseCls() {
    }

    public ResponseCls(Boolean hasWebServerError) {
        if (hasWebServerError) {
            this.status = true;
        } else {
            this.status = false;
        }

    }

    public Boolean HasWebServerError() {
        return !this.status;
    }

    public ResponseCls(boolean requestStatus, Integer code, List<String> messages, T data) {
        this.status = requestStatus;
    }

    public Boolean getRequestStatus() {
        return this.status;
    }

    public ResponseCode getCode() {
        if (this.code == 0) {
            return ResponseCode.normal;
        } else if (this.code == 1) {
            return ResponseCode.authenFailed;
        } else {
            return this.code == 2 ? ResponseCode.invalidInput : ResponseCode.systemerror;
        }
    }

    public int getCodeValue() {
        return this.code;
    }

    public List<String> getErrorMessage() {
        return this.messages;
    }

    public T getData() {
        return this.data;
    }
}
