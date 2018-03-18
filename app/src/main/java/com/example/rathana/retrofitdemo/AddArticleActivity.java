package com.example.rathana.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathana.retrofitdemo.data.remote.ServiceGenerator;
import com.example.rathana.retrofitdemo.data.remote.service.ArticleService;
import com.example.rathana.retrofitdemo.entity.ArticleInsertResponse;
import com.example.rathana.retrofitdemo.entity.form.ArticleInsert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArticleActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private EditText catId;
    private EditText authorId;
    private ArticleService articleService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        initUI();
        articleService= ServiceGenerator.generateService(ArticleService.class);
    }

    private void initUI() {
        catId=findViewById(R.id.catId);
        authorId=findViewById(R.id.authorId);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
    }

    private ArticleInsert getFormData(){
        ArticleInsert article=new ArticleInsert();
        article.setAuthor(Integer.parseInt(authorId.getText().toString()));
        article.setCategoryId(Integer.parseInt(catId.getText().toString()));
        article.setTitle(title.getText().toString());
        article.setDescription(content.getText().toString());

        return article;
    }

    public void onSaveArticle(View view){
        final ArticleInsert article =getFormData();
        Call<ArticleInsertResponse> call= articleService.addNewArticle(article);
        call.enqueue(new Callback<ArticleInsertResponse>() {
            @Override
            public void onResponse(Call<ArticleInsertResponse> call, Response<ArticleInsertResponse> response) {
                ArticleInsertResponse articleInsertResponse=response.body();
                Toast.makeText(AddArticleActivity.this, ""+articleInsertResponse.getMessage(), Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onFailure(Call<ArticleInsertResponse> call, Throwable t) {
                Toast.makeText(AddArticleActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
