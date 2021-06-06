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
import java.util.Random;

import model.News;
import model.NewsAPI;

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private ArrayList<News> newsSource;
    private NewsRVAdapter newsRVAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        newsSource = new ArrayList<>();
        loadsources();

        newsRecyclerView = root.findViewById(R.id.newsRecyclerView);
        newsRVAdapter = new NewsRVAdapter(getActivity(), newsSource);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsRVAdapter);

        return root;
    }
    private void loadsources() {

        String url = "https://newsapi.org/v2/top-headlines?category=business&country=us&pageSize=20&apiKey=" + NewsAPI.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String name = jsonObject1.getJSONObject("source").getString("name");
                        String author = jsonObject1.getString("author");
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String publishedAt = jsonObject1.getString("publishedAt");
                        String url = jsonObject1.getString("url");
                        String urlToImage = jsonObject1.getString("urlToImage");
                        String time = jsonObject1.getString("publishedAt");

                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMMM yyyy");
                        SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm");
                        String formatDate = "";
                        String formatDate1 = "";
                        try{
                            Date date = dateFormat1.parse(publishedAt);
                            formatDate = dateFormat2.format(date);
                            Date date1 = dateFormat1.parse(time);
                            formatDate1 = dateFormat3.format(date1);
                        }catch(Exception e){
                            formatDate = publishedAt;
                            formatDate1 = time;
                        }

                        News model = new News(
                                ""+name,
                                ""+author,
                                ""+title,
                                ""+description,
                                ""+url,
                                ""+urlToImage,
                                ""+formatDate,
                                ""+formatDate1
                        );
                        newsSource.add(model);
                        System.out.println(newsSource.get(0).getAuthor());
                        System.out.println(newsSource.size());
                    }
                    newsRVAdapter.notifyDataSetChanged();
                }
                catch (Exception e){
                    e.printStackTrace();
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