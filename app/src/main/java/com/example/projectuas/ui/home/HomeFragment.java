package com.example.projectuas.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.Login;
import com.example.projectuas.NewsRVAdapter;
import com.example.projectuas.R;
import com.example.projectuas.SearchActivity;
import com.example.projectuas.SplashActivity;
import com.example.projectuas.ui.news.NewsFragment;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.ListedCompany;
import model.News;
import model.NewsAPI;
import model.Stocks;
import model.UserArray;

public class HomeFragment extends Fragment {
    RequestQueue mQueue;

    private TextView newsExampleTitle, newsExampleAuthor, newsExampleDescription, newsExampleUrl, newsExampleSource, newsExamplePublishedAt, newsExampleTime;
    private ImageView newsExampleUrlToImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView homeSearchBarTextView = root.findViewById(R.id.homeSearchBarTextView);
        TextView homeWelcomeTextView = root.findViewById(R.id.homeWelcomeTextView);
        TextView homePriceTextView = root.findViewById(R.id.homePriceTextView);
        TextView homePercentageTextView = root.findViewById(R.id.homePercentageTextView);

        newsExampleTitle = root.findViewById(R.id.newsExampleTitle);
        newsExampleAuthor = root.findViewById(R.id.newsExampleAuthor);
        newsExampleDescription = root.findViewById(R.id.newsExampleDescription);
        newsExampleUrl = root.findViewById(R.id.newsExampleUrl);
        newsExampleUrlToImage = root.findViewById(R.id.newsExampleUrlToImage);
        newsExampleSource = root.findViewById(R.id.newsExampleSource);
        newsExamplePublishedAt = root.findViewById(R.id.newsExamplePublishedAt);
        newsExampleTime = root.findViewById(R.id.newsExampleTime);

        dataNews(getContext());

        mQueue = Volley.newRequestQueue(getContext());

        CandleStickChart chart = (CandleStickChart) root.findViewById(R.id.chart);
        chart.setNoDataText("Loading data...");

        Stocks stocks = new Stocks();

        ListedCompany listedCompany = new ListedCompany("SPY");
        stocks.setCompanyChartHome(getContext(), mQueue, listedCompany, chart, homePriceTextView, homePercentageTextView);

        String welcome = "Welcome back " + UserArray.currentUser.getNama() + "!";
        homeWelcomeTextView.setText(welcome);

        homeSearchBarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);

                startActivity(intent);
            }
        });

        return root;
    }

    private void dataNews(Context context) {
        String url = "https://newsapi.org/v2/everything?q=stocks&pageSize=20&apiKey=" + NewsAPI.API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");


                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

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

                    newsExampleSource.setText(name);
                    newsExampleTitle.setText(title);
                    newsExampleAuthor.setText(author);
                    newsExampleDescription.setText(description);
                    newsExampleUrl.setText(url);
                    newsExamplePublishedAt.setText(formatDate);
                    newsExampleTime.setText(formatDate1);
                    Picasso.get().load(urlToImage).into(newsExampleUrlToImage);

                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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