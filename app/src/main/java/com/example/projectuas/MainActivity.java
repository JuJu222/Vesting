package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Companies;
import model.Stocks;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CandleStickChart chart = (CandleStickChart) findViewById(R.id.chart);

        ArrayList<CandleEntry> ceList = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Companies companies = new Companies(new ArrayList<>());
        Stocks stocks = new Stocks();

        executor.execute(() -> {
            //Background work here

            stocks.displayChart("IBM", chart);
            handler.post(() -> {
                //UI Thread work here
                chart.setBackgroundColor(Color.BLACK);
                chart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
                chart.getXAxis().setTextColor(Color.WHITE);
                chart.getLegend().setTextColor(Color.WHITE);
                chart.getDescription().setTextColor(Color.WHITE);
            });
        });
    }
}