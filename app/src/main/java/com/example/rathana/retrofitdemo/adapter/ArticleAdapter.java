package com.example.rathana.retrofitdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.rathana.retrofitdemo.R;
import com.example.rathana.retrofitdemo.entity.Article;

import java.util.List;

/**
 * Created by RATHANA on 3/10/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    List<Article> articles;
    Article article;
    ArticleCallback callback;
    public void setArticle(Article article) {
        this.article = article;
        this.articles.add(this.article);
        notifyItemInserted(articles.size()-1);
    }

    Context context;
    public ArticleAdapter(Context context, List<Article> articles){
        this.articles=articles; this.context=context;
        this.callback= (ArticleCallback) context;
    }

    public void add(List<Article> articles){
        int previousSize=this.articles.size();
        this.articles.addAll(articles);
        notifyItemRangeInserted(previousSize,this.articles.size());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.article_item_layout,parent,false);
        return new ViewHolder(view);
    }

    private static final String TAG = "ArticleAdapter";
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article=articles.get(position);
        Log.e(TAG, "onBindViewHolder: "+articles.size());
        holder.title.setText(article.getTitle());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView btnOption;
        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            btnOption=itemView.findViewById(R.id.btnOption);

            btnOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //callback.optionClick(getAdapterPosition());
                    PopupMenu popupMenu=new PopupMenu(context,view);
                    popupMenu.inflate(R.menu.context_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.remove:
                                    callback.getAdapterPositionWhenRemove(getAdapterPosition());
                                    return true;
                                case R.id.update:
                                    callback.getAdapterPositionWhenUpdate(getAdapterPosition());
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();

                }
            });
        }
    }

    public interface ArticleCallback{
        void optionClick(int position);
        void getAdapterPositionWhenRemove(int position);
        void getAdapterPositionWhenUpdate(int position);

    }
}
