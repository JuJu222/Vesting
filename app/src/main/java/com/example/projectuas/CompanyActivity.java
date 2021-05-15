package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;

import model.ListedCompany;
import model.OwnedCompany;
import model.Stocks;
import model.TinyDB;
import model.UserArray;
import model.VolleyCallback;

public class CompanyActivity extends AppCompatActivity {
    RequestQueue mQueue;
    boolean exists = false;

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
                companyBuyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                companyBuyButton.setBackgroundColor(Color.parseColor("#808080"));
                companyBuyButton.setEnabled(false);
                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                stocks.getCurrentPrice(mQueue, listedCompany.getCompanySymbol(), new VolleyCallback(){
                    @Override
                    public void onSuccess(String result){
                        companyBuyButton.getBackground().setColorFilter(null);
                        companyBuyButton.setBackgroundColor(Color.parseColor("#41A03A"));
                        companyBuyButton.setEnabled(true);
                        UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() - Double.parseDouble(result));

                        for (OwnedCompany ownedCompany : UserArray.currentUser.getOwnedCompanies()) {
                            if (ownedCompany.getCompanySymbol().equalsIgnoreCase(listedCompany.getCompanySymbol())) {
                                exists = true;

                                ownedCompany.setLots(ownedCompany.getLots() + 1);
                                ownedCompany.setAverageBoughtPrice((ownedCompany.getAverageBoughtPrice() + listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose()) / 2);
                            }
                        }

                        if (!exists) {
                            OwnedCompany temp = new OwnedCompany();
                            temp.setCompanySymbol(listedCompany.getCompanySymbol());
                            temp.setLots(1);
                            temp.setAverageBoughtPrice(listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose());

                            UserArray.currentUser.getOwnedCompanies().add(temp);
                        }

                        TinyDB tinydb = new TinyDB(getApplicationContext());

                        UserArray.akunuser = tinydb.getListUser("akunuser");
                        for (int i = 0; i < UserArray.akunuser.size(); i++) {
                            if (UserArray.currentUser.getEmail().equalsIgnoreCase(UserArray.akunuser.get(i).getEmail())) {
                                UserArray.akunuser.set(i, UserArray.currentUser);
                            }
                        }
                        tinydb.putListUser("akunuser", UserArray.akunuser);

                        Toast.makeText(getApplicationContext(), "Bought 1 lot of " + listedCompany.getCompanySymbol() + " for " + listedCompany.getCompanyStockPrices().get(listedCompany.getCompanyStockPrices().size() - 1).getPriceClose(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}