package com.example.rathana.retrofitdemo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chhean.daravuth on 17/03/2018.
 */

public class ArticleInsertResponse {
    @SerializedName("DATA")
    private Article data;
    @SerializedName("MESSAGE")
    private String message;
    @SerializedName("CODE")
    private String code;

    public Article getData() {
        return data;
    }

    public void setData(Article data) {
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
