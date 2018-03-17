package com.example.rathana.retrofitdemo.data.remote.request;

import android.util.Log;

import com.example.rathana.retrofitdemo.data.remote.ServiceGenerator;
import com.example.rathana.retrofitdemo.data.remote.service.ArticleService;
import com.example.rathana.retrofitdemo.entity.Article;
import com.example.rathana.retrofitdemo.entity.ArticleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RATHANA on 3/11/2018.
 */

public class ArticleRequest {
    private static final String TAG = "ArticleRequest";
    private ArticleService articleService;
    public ArticleRequest(){
        articleService= ServiceGenerator.generateService(ArticleService.class);
    }

    public ArticleResponse getArticle(int page,int limit){
        Call<ArticleResponse> call=articleService.getArticles(page,limit);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                ArticleResponse articleResponse=response.body();
                Log.e(TAG, "onResponse: "+articleResponse.getCode() );
                List<Article> articles=articleResponse.getData();
                for (Article article: articles){
                    Log.e(TAG, "onResponse: "+article.toString() );
                }

            }
            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
        return null;
    }

}
