package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import model.ListedCompany;
import model.Stocks;
import model.UserArray;
import model.VolleyCallback;

public class CompanyActivity extends AppCompatActivity {
    RequestQueue mQueue;
    boolean textFilled = false;
    boolean ongoingReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView companySymbolTextView = findViewById(R.id.companySymbolTextView);
        TextView companyNameTextView = findViewById(R.id.companyNameTextView);
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
        Button companyBuyButton = findViewById(R.id.companyBuyButton);

        Stocks stocks = new Stocks();
        Intent intent = getIntent();
        ListedCompany listedCompany = intent.getParcelableExtra("company");

        mQueue = Volley.newRequestQueue(this);

        CandleStickChart companyChart = findViewById(R.id.companyChart);
        companyChart.setNoDataText("Loading data...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            companyDescriptionTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        companySymbolTextView.setText(listedCompany.getCompanySymbol());
        stocks.setCompanyChart(this, mQueue, listedCompany, companyChart, companyNameTextView, companyCurrentPriceTextView, companyPriceChangeTextView,
                companyDescriptionTextView, companySectorTextView, companyPeRatioTextView,
                companyPegRatioTextView, companyLastDividendDateTextView, companyEmployeesView,
                companyAnalystTargetPriceTextView, companyPbRatioTextView, companyPayoutRatioTextView, companyBuyButton);

        companyBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CompanyActivity.this, BuyActivity.class);
                intent1.putExtra("symbol", listedCompany.getCompanySymbol());
                intent1.putExtra("currentPrice", listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose());
                startActivity(intent1);

            }
        });
    }
}