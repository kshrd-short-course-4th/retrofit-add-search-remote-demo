package com.example.rathana.retrofitdemo.data.remote.service;

import com.example.rathana.retrofitdemo.entity.ArticleInsertResponse;
import com.example.rathana.retrofitdemo.entity.ArticleResponse;
import com.example.rathana.retrofitdemo.entity.Image;
import com.example.rathana.retrofitdemo.entity.form.ArticleInsert;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by RATHANA on 3/11/2018.
 */

public interface ArticleService {
    @GET("/v1/api/articles")
    Call<ArticleResponse> getArticles(@Query("page") int page, @Query("limit") int limit);

    @POST("/v1/api/articles")
    Call<ArticleInsertResponse> addNewArticle(@Body ArticleInsert articleInsert);
    @GET("/v1/api/articles")
    Call<ArticleResponse> searchArticle(@Query("title") String search,@Query("page") int page, @Query("limit") int limit);

    @DELETE("/v1/api/articles/{id}")
    Call<ArticleInsertResponse> removeArticle(@Path("id") int id);

    @PUT("/v1/api/articles/{id}")
    Call<ArticleInsertResponse> editArticle(@Path("id") int id,@Body ArticleInsert article);

    @Multipart
    @POST("/v1/api/uploadfile/single")
    Call<Image> uploadImage(@Part MultipartBody.Part file);

}
