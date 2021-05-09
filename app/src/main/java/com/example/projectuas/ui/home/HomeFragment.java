package com.example.projectuas.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.R;
import com.example.projectuas.SearchActivity;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import model.Company;
import model.Stocks;

public class HomeFragment extends Fragment {
    RequestQueue mQueue;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mQueue = Volley.newRequestQueue(getContext());

                CandleStickChart chart = (CandleStickChart) root.findViewById(R.id.chart);

                Stocks stocks = new Stocks();

                Company company = new Company("IBM", new ArrayList<>());
//                stocks.setCompanyData(mQueue, company, chart);

                TextView homeSearchBarTextView = root.findViewById(R.id.homeSearchBarTextView);

                homeSearchBarTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);

                        startActivity(intent);
                    }
                });
            }
        });
        return root;
    }
}