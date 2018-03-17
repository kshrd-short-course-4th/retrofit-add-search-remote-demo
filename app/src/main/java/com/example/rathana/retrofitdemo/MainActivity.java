package com.example.rathana.retrofitdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rathana.retrofitdemo.adapter.ArticleAdapter;
import com.example.rathana.retrofitdemo.data.remote.ServiceGenerator;
import com.example.rathana.retrofitdemo.data.remote.request.ArticleRequest;
import com.example.rathana.retrofitdemo.data.remote.service.ArticleService;
import com.example.rathana.retrofitdemo.entity.Article;
import com.example.rathana.retrofitdemo.entity.ArticleInsertResponse;
import com.example.rathana.retrofitdemo.entity.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.ArticleCallback{
    RecyclerView recyclerView;
    ArticleAdapter articleAdapter;
    List<Article> articles=new ArrayList<>();
    private static final String TAG = "MainActivity";
    ArticleService articleService;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articleService= ServiceGenerator.generateService(ArticleService.class);
        setupRV();
        //ArticleRequest articleRequest=new ArticleRequest();
        //articleRequest.getArticle(1,15);
        getArticle(1,15);

    }
    public void  setupRV(){
        search=findViewById(R.id.search);
        recyclerView=findViewById(R.id.rvArticle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter=new ArticleAdapter(this,articles);
        recyclerView.setAdapter(articleAdapter);

        //event when we click search key
        /*search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e(TAG, "onEditorAction: "+textView.getText().toString());
                String text=textView.getText().toString();
                    searchResult(text,1,15);
                return false;
            }
        });*/
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                final String text=search.getText().toString();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "onKey: "+ text);
                        searchResult(text,1,15);
                    }
                }, 500);
                return false;
            }
        });
    }

    private void searchResult(String text, int page, int limit) {
        Call<ArticleResponse> call=articleService.searchArticle(text,page,limit);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                List<Article> arts=response.body().getData();
                articles.clear();
                for(Article article : arts){
                    articles.add(article);
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
    }


    public void getArticle(int page,int limit) {
        Call<ArticleResponse> call = articleService.getArticles(page, limit);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                ArticleResponse articleResponse = response.body();
                Log.e(TAG, "onResponse: " + articleResponse.getCode());
                List<Article> arts = articleResponse.getData();
                articles.addAll(arts);
                /*for (Article article : articles) {
                    Log.e(TAG, "onResponse: " + article.toString());
                    this.articles.add(article);
                }*/
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void optionClick(int position) {

    }

    @Override
    public void getAdapterPositionWhenRemove(int position) {
        Article article=articles.get(position);
        Log.e(TAG, "getAdapterPositionWhenRemove: "+article.toString() );
        removeArticle(article,position);


    }

    private void removeArticle(final Article article,final int position) {
        Call<ArticleInsertResponse> call=articleService.removeArticle(article.getId());
        call.enqueue(new Callback<ArticleInsertResponse>() {
            @Override
            public void onResponse(Call<ArticleInsertResponse> call, Response<ArticleInsertResponse> response) {
                ArticleInsertResponse articleInsertResponse=response.body();
                Toast.makeText(MainActivity.this, articleInsertResponse.getMessage(), Toast.LENGTH_SHORT).show();
                articles.remove(article);
                articleAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<ArticleInsertResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.article:
                startActivity(new Intent(this,AddArticleActivity.class));
        }
        return true;
    }
}
