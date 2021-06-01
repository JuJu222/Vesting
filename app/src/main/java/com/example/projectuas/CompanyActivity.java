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
    TextInputLayout companyLotsTextInputLayout;

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
        companyLotsTextInputLayout = findViewById(R.id.companyLotsTextInputLayout);

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
                companyAnalystTargetPriceTextView, companyPbRatioTextView, companyPayoutRatioTextView, companyBuyButton, companyLotsTextInputLayout);

        companyLotsTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (companyLotsTextInputLayout.getEditText().getText().toString().isEmpty()) {
                    textFilled = false;
                } else {
                    textFilled = true;
                }

                if (textFilled && !ongoingReq) {
                    companyBuyButton.getBackground().setColorFilter(null);
                    companyBuyButton.setBackgroundColor(Color.parseColor("#41A03A"));
                    companyBuyButton.setEnabled(true);
                } else {
                    companyBuyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                    companyBuyButton.setBackgroundColor(Color.parseColor("#808080"));
                    companyBuyButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        companyBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyBuyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                companyBuyButton.setBackgroundColor(Color.parseColor("#808080"));
                companyBuyButton.setEnabled(false);
                ongoingReq = true;
                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                stocks.getCurrentPrice(mQueue, listedCompany.getCompanySymbol(), new VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        ongoingReq = false;
                        if (textFilled) {
                            companyBuyButton.getBackground().setColorFilter(null);
                            companyBuyButton.setBackgroundColor(Color.parseColor("#41A03A"));
                            companyBuyButton.setEnabled(true);
                        } else {
                            companyBuyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                            companyBuyButton.setBackgroundColor(Color.parseColor("#808080"));
                            companyBuyButton.setEnabled(false);
                        }
                        UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() - (Double.parseDouble(result) * Double.parseDouble(companyLotsTextInputLayout.getEditText().getText().toString())));
                        
                        buyPortfolioDB(listedCompany, getApplicationContext());

                        Toast.makeText(getApplicationContext(), "Bought " + companyLotsTextInputLayout.getEditText().getText().toString() + " lot of " + listedCompany.getCompanySymbol() + " for " + listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void buyPortfolioDB(ListedCompany listedCompany, Context context) {
        String url = "http://192.168.100.18/vesting_webservice/buy_portfolio.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("symbol", listedCompany.getCompanySymbol());
                params.put("lots", companyLotsTextInputLayout.getEditText().getText().toString());
                params.put("price", String.valueOf(listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose()));
                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));
                params.put("balance", String.valueOf(UserArray.currentUser.getBalance()));

                return params;
            }
        };

        mQueue.add(request);
    }
}