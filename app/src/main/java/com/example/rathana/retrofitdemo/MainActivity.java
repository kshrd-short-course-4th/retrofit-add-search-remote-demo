package com.example.rathana.retrofitdemo;

import android.content.Context;
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
import com.example.rathana.retrofitdemo.entity.Pagination;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ArticleAdapter.ArticleCallback,
Paginate.Callbacks{
    RecyclerView recyclerView;
    ArticleAdapter articleAdapter;
    List<Article> articles=new ArrayList<>();
    private static final String TAG = "MainActivity";
    ArticleService articleService;
    EditText search;

    //paginate
    boolean loading =false;
    int page=1;
    int itemPerPage=5;
    int totalPage;
    Paginate paginate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articleService= ServiceGenerator.generateService(ArticleService.class);
        setupRV();
        //ArticleRequest articleRequest=new ArticleRequest();
        //articleRequest.getArticle(1,15);
        //getArticle(page,itemPerPage);
    }
    class WrapContentLinearLayoutManager extends LinearLayoutManager {
        //... constructor
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reversed){
            super(context,orientation,reversed);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }
    public void  setupRV(){
        if(paginate!=null){
            paginate.unbind();
        }

        search=findViewById(R.id.search);
        recyclerView=findViewById(R.id.rvArticle);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        articleAdapter=new ArticleAdapter(this,articles);
        recyclerView.setAdapter(articleAdapter);

        page=1;
        loading=false;
        paginate=Paginate.with(recyclerView,this)
                .setLoadingTriggerThreshold(3)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(null)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 3;
                    }
                })
                .build();

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
                //articles.addAll(arts);
                /*for (Article article : articles) {
                    Log.e(TAG, "onResponse: " + article.toString());
                    this.articles.add(article);
                }*/
                //articleAdapter.notifyDataSetChanged();
                //get Total page from api
                totalPage=articleResponse.getPagination().getTotalPages();
                articleAdapter.add(arts);
                loading=false;

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

    @Override
    public void getAdapterPositionWhenUpdate(int position) {
        Article article=articles.get(position);
        //Log.e(TAG, "getAdapterPositionWhenUpdate: "+ article.toString());
        Intent intent=new Intent(this,UpdateArticleActivity.class);
        /*Bundle bundle= new Bundle();
        bundle.putParcelable("ARTICLE",article);
        intent.putExtras(bundle);*/
        intent.putExtra("articleId",article.getId());
        intent.putExtra("catId",article.getCategory().getId());
        intent.putExtra("authorId",article.getAuthor().getId());
        intent.putExtra("title",article.getTitle());
        intent.putExtra("content",article.getDescription());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getArticle(1,15);
    }

    private void removeArticle(final Article article, final int position) {
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
            case R.id.uploadImage:
                startActivity(new Intent(this,UploadImageActivity.class));

        }
        return true;
    }


    //paginate CallBack implement
    @Override
    public void onLoadMore() {
        loading=true;
        Log.e(TAG, "onLoadMore: "+page);
        page++;
        //get new data
        getArticle(page,itemPerPage);

    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page==totalPage;
    }
}
