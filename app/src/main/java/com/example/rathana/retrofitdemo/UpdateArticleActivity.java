package com.example.rathana.retrofitdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rathana.retrofitdemo.data.remote.ServiceGenerator;
import com.example.rathana.retrofitdemo.data.remote.service.ArticleService;
import com.example.rathana.retrofitdemo.entity.Article;
import com.example.rathana.retrofitdemo.entity.ArticleInsertResponse;
import com.example.rathana.retrofitdemo.entity.Author;
import com.example.rathana.retrofitdemo.entity.Category;
import com.example.rathana.retrofitdemo.entity.form.ArticleInsert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateArticleActivity extends AppCompatActivity {
    EditText catId,authorId,title,content;
    ArticleService articleService;
    Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_article);
        articleService= ServiceGenerator.generateService(ArticleService.class);
        setupUI();
        Intent intent=getIntent();
        if(null !=intent){
            article =new Article();
            article.setId(intent.getIntExtra("articleId",0));
            article.setTitle(intent.getStringExtra("title"));
            article.setDescription(intent.getStringExtra("content"));
            Author author=new Author();
            author.setId(intent.getIntExtra("catId",0));
            Category cat=new Category();
            cat.setId(intent.getIntExtra("authorId",0));
            article.setCategory(cat);
            article.setAuthor(author);
            if(null!=article){
                setFormData(article);
            }else
                Toast.makeText(this, "No data for Home screen!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUI() {
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

    private static final String TAG = "UpdateArticleActivity";
    private void setFormData(Article article){
        Log.e(TAG, "setFormData: "+article );
        catId.setText(article.getCategory().getId()+"");
        authorId.setText(article.getAuthor().getId()+"");
        title.setText(article.getTitle());
        content.setText(article.getDescription());
    }

    public void onSaveChangeArticle(View view){
        ArticleInsert article=getFormData();
        //request to api
        Call<ArticleInsertResponse> call=articleService.editArticle(this.article.getId(),article);
        call.enqueue(new Callback<ArticleInsertResponse>() {
            @Override
            public void onResponse(Call<ArticleInsertResponse> call, Response<ArticleInsertResponse> response) {
                ArticleInsertResponse articleInsertResponse=response.body();
                Toast.makeText(UpdateArticleActivity.this, ""+articleInsertResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArticleInsertResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
            }
        });

    }
}
