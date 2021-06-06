package com.example.projectuas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectuas.ui.news.NewsFragment;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import model.News;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.HolderSourceList>{

    private Context context;
    private ArrayList<News> newsSource;

    public NewsRVAdapter(Context context, ArrayList<News> newsSource) {
        this.context = context;
        this.newsSource = newsSource;
    }

    @NonNull
    @Override
    public NewsRVAdapter.HolderSourceList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_news_row, parent, false);
        return new HolderSourceList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRVAdapter.HolderSourceList holder, int position) {
        News news = newsSource.get(position);
        String name = news.getName();
        String author = news.getAuthor();
        String title = news.getTitle();
        String description = news.getDescription();
        String url = news.getUrl();
        String urlToImage = news.getUrlToImage();
        String publishedAt = news.getPublishedAt();
        String time = news.getTime();

        holder.newsRowSource.setText(name);
        holder.newsRowTitle.setText(title);
        holder.newsRowAuthor.setText(author);
        holder.newsRowDescription.setText(description);
        holder.newsRowPublishedAt.setText(publishedAt);
        holder.newsRowTime.setText(time);
        Picasso.get().load(urlToImage).into(holder.newsRowImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsSource.size();
    }

    public static class HolderSourceList extends RecyclerView.ViewHolder{

        TextView newsRowAuthor, newsRowTitle, newsRowDescription, newsRowSource, newsRowPublishedAt, newsRowTime;
        ImageView newsRowImage;

        public HolderSourceList(@NonNull View itemView) {
            super(itemView);

            newsRowAuthor = itemView.findViewById(R.id.newsRowAuthor);
            newsRowTitle = itemView.findViewById(R.id.newsRowTitle);
            newsRowDescription = itemView.findViewById(R.id.newsRowDescription);
            newsRowSource = itemView.findViewById(R.id.newsRowSource);
            newsRowPublishedAt = itemView.findViewById(R.id.newsRowPublishedAt);
            newsRowImage = itemView.findViewById(R.id.newsRowImage);
            newsRowTime = itemView.findViewById(R.id.newsRowTime);
        }
    }
}
