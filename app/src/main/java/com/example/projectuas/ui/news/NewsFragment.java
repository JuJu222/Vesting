package com.example.projectuas.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.NewsRVAdapter;
import com.example.projectuas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.News;
import model.NewsAPI;

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private ArrayList<News> newsSource;
    private NewsRVAdapter newsRVAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        loadsources();

        newsRecyclerView = root.findViewById(R.id.newsRecyclerView);
        newsRVAdapter = new NewsRVAdapter(getActivity(), newsSource);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsRVAdapter);

        return root;
    }
    private void loadsources() {

        newsSource = new ArrayList<>();
        newsSource.clear();

        String url = "https://newsapi.org/v2/everything?q=stocks&pageSize=20&apiKey=" + NewsAPI.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String name = jsonObject1.getString("name");
                        String author = jsonObject1.getString("author");
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String publishedAt = jsonObject1.getString("publishedAt");
                        String url = jsonObject1.getString("url");
                        String urlToImage = jsonObject1.getString("urlToImage");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                        String formatDate = "";
                        try {
                            Date date = dateFormat.parse(publishedAt);
                            formatDate = dateFormat.format(date);
                        }catch (Exception e){
                            formatDate = publishedAt;
                        }

                        News model = new News(
                                ""+name,
                                ""+author,
                                ""+title,
                                ""+description,
                                ""+url,
                                ""+urlToImage,
                                ""+formatDate
                        );
                        newsSource.add(model);
                    }
                }
                catch (Exception e){


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}