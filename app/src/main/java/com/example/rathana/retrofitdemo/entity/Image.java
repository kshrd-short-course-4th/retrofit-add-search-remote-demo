package com.example.rathana.retrofitdemo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RATHANA on 3/18/2018.
 */

public class Image {

    @SerializedName("DATA")
    private String data;
    @SerializedName("MESSAGE")
    private String message;
    @SerializedName("CODE")
    private String code;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
