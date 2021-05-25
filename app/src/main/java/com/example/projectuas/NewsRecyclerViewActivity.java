package com.example.projectuas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.News;

public class NewsRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private ArrayList<News> newsContent;
    private NewsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news);
        initView();
        setupRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setupRecyclerView(){
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        newsRecyclerView.setLayoutManager(manager);
        newsRecyclerView.setAdapter(adapter);
    }

    private void initView(){
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsContent = new ArrayList<News>();
        adapter = new NewsRVAdapter(newsContent);
    }
}
