package com.example.rathana.retrofitdemo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RATHANA on 3/11/2018.
 */

public class ArticleResponse {

    @SerializedName("PAGINATION")
    private Pagination pagination;
    @SerializedName("DATA")
    private List<Article> data;
    @SerializedName("MESSAGE")
    private String message;
    @SerializedName("CODE")
    private String code;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
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
