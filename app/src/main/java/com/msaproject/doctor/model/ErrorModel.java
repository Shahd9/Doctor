package com.msaproject.doctor.model;

import androidx.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ErrorModel extends Exception {

    @Retention(SOURCE)
    @IntDef({ErrorCodes.AUTH_ERROR, ErrorCodes.NETWORK_ERROR})
    public @interface ErrorCodes {
        int AUTH_ERROR = 401;
        int NETWORK_ERROR = 404;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private int code;

    public ErrorModel() {

    }

    public ErrorModel(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ErrorModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isAuthError() {
        return code == ErrorCodes.AUTH_ERROR;
    }

    public boolean isNetworkError() {
        return code == ErrorCodes.NETWORK_ERROR;
    }
}