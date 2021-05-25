package com.example.projectuas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.News;

public class NewsRVAdapter extends RecyclerView.Adapter<NewsRVAdapter.NewsViewHolder>{

    private ArrayList<News> listNews;

    public NewsRVAdapter(ArrayList<News> listNews) {
        this.listNews = listNews;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_news_row, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        private TextView newsRowAuthor, newsRowTitle, newsRowDescription, newsRowSource;
        private ImageView newsRowImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsRowAuthor = itemView.findViewById(R.id.newsRowAuthor);
            newsRowTitle = itemView.findViewById(R.id.newsRowTitle);
            newsRowDescription = itemView.findViewById(R.id.newsRowDescription);
            newsRowSource = itemView.findViewById(R.id.newsRowSource);
            newsRowImage = itemView.findViewById(R.id.newsRowImage);
        }
    }
}
