package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;

import model.Company;
import model.Stocks;

public class CompanyActivity extends AppCompatActivity {
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        TextView companySymbolTextView = findViewById(R.id.companySymbolTextView);
        TextView companyCurrentPriceTextView = findViewById(R.id.companyCurrentPriceTextView);
        TextView companyPriceChangeTextView = findViewById(R.id.companyPriceChangeTextView);
        TextView companyDescriptionTextView = findViewById(R.id.companyDescriptionTextView);
        TextView companyAnalystTargetPriceTextView = findViewById(R.id.companyAnalystTargetPriceTextView);
        TextView companyLastDividendDateTextView = findViewById(R.id.companyLastDividendDateTextView);
        TextView companyPbRatioTextView = findViewById(R.id.companyPbRatioTextView);
        TextView companyPegRatioTextView = findViewById(R.id.companyPegRatioTextView);
        TextView companyPeRatioTextView = findViewById(R.id.companyPeRatioTextView);
        TextView companySectorTextView = findViewById(R.id.companySectorTextView);
        TextView companyPayoutRatioTextView = findViewById(R.id.companyPayoutRatioTextView);
        TextView companyEmployeesView = findViewById(R.id.companyEmployeesView);

        Stocks stocks = new Stocks();
        Intent intent = getIntent();
        Company company = intent.getParcelableExtra("company");

        mQueue = Volley.newRequestQueue(this);

        CandleStickChart companyChart = findViewById(R.id.companyChart);
        companyChart.setNoDataText("Loading data...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            companyDescriptionTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        companySymbolTextView.setText(company.getCompanySymbol());
        stocks.setCompanyData(this, mQueue, company, companyChart, companyCurrentPriceTextView, companyPriceChangeTextView,
                companyDescriptionTextView, companySectorTextView, companyPeRatioTextView,
                companyPegRatioTextView, companyLastDividendDateTextView, companyEmployeesView,
                companyAnalystTargetPriceTextView, companyPbRatioTextView, companyPayoutRatioTextView);
    }
}