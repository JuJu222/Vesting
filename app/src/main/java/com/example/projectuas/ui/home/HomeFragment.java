package com.example.projectuas.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.Login;
import com.example.projectuas.R;
import com.example.projectuas.SearchActivity;
import com.example.projectuas.SplashActivity;
import com.github.mikephil.charting.charts.CandleStickChart;

import model.ListedCompany;
import model.Stocks;
import model.UserArray;

public class HomeFragment extends Fragment {
    RequestQueue mQueue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView homeSearchBarTextView = root.findViewById(R.id.homeSearchBarTextView);
        TextView homeWelcomeTextView = root.findViewById(R.id.homeWelcomeTextView);
        TextView homePriceTextView = root.findViewById(R.id.homePriceTextView);
        TextView homePercentageTextView = root.findViewById(R.id.homePercentageTextView);

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
}