package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import model.UserArray;

public class SellActivity extends AppCompatActivity {
    String symbol;
    String temp;
    double currentPrice;
    double averagePrice;
    int position;
    int lots;
    double percentage;
    TextInputLayout sellLotsTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        TextView sellSymbolTextView = findViewById(R.id.sellSymbolTextView);
        TextView sellCurrentPriceTextView = findViewById(R.id.sellCurrentPriceTextView);
        TextView sellLotsOwnedTextView = findViewById(R.id.sellLotsOwnedTextView);
        TextView sellProceedAmountTextView = findViewById(R.id.sellProceedAmountTextView);
        TextView sellPlAmountTextView = findViewById(R.id.sellPlAmountTextView);
        TextView sellPlPercentageTextView = findViewById(R.id.sellPlPercentageTextView);
        TextView sellTotalBalanceTextView = findViewById(R.id.sellTotalBalanceTextView);
        sellLotsTextInputLayout = findViewById(R.id.sellLotsTextInputLayout);
        Button sellButton = findViewById(R.id.sellButton);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        lots = intent.getIntExtra("lots", 0);
        currentPrice = intent.getDoubleExtra("currentPrice", 0);
        averagePrice = intent.getDoubleExtra("averagePrice", 0);
        percentage = intent.getDoubleExtra("percentage", 0);
        position = intent.getIntExtra("position", -1);

        sellButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        sellButton.setBackgroundColor(Color.parseColor("#808080"));
        sellButton.setEnabled(false);
        DecimalFormat df = new DecimalFormat("#.##");
        temp = "Sell " + symbol;
        sellSymbolTextView.setText(temp);
        temp = "$" + currentPrice;
        sellCurrentPriceTextView.setText(temp);
        sellLotsOwnedTextView.setText(String.valueOf(lots));
        sellProceedAmountTextView.setText("-");
        sellPlAmountTextView.setText("-");
        String percentageString = df.format(percentage) + "%";
        if (percentage > 0) {
            percentageString = "+" + percentageString;
            sellPlPercentageTextView.setTextColor(Color.GREEN);
        } else if (percentage < 0) {
            sellPlPercentageTextView.setTextColor(Color.RED);
        } else {
            sellPlPercentageTextView.setTextColor(Color.WHITE);
        }
        sellPlPercentageTextView.setText(percentageString);
        temp = "$" + df.format(UserArray.currentUser.getBalance());
        sellTotalBalanceTextView.setText(temp);

        sellLotsTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = sellLotsTextInputLayout.getEditText().getText().toString().isEmpty();

                if (!isEmpty) {
                    boolean isAbove0 = Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) > 0;
                    boolean isExceedLots = Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) > lots;

                    double proceedAmount = Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) * currentPrice;
                    double plAmount = proceedAmount - (Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) * averagePrice);
                    double totalBalance = UserArray.currentUser.getBalance() + proceedAmount;

                    if (isAbove0 && !isExceedLots) {
                        sellButton.getBackground().setColorFilter(null);
                        sellButton.setBackgroundColor(Color.parseColor("#41A03A"));
                        sellButton.setEnabled(true);

                        String plAmountString  = "$" + df.format(plAmount);
                        if (plAmount > 0) {
                            plAmountString = "+" + plAmountString;
                            sellPlAmountTextView.setTextColor(Color.GREEN);
                        } else if (proceedAmount < 0) {
                            sellPlAmountTextView.setTextColor(Color.RED);
                        } else {
                            sellPlAmountTextView.setTextColor(Color.WHITE);
                        }

                        temp = "$" + df.format(proceedAmount);
                        sellProceedAmountTextView.setText(temp);
                        sellPlAmountTextView.setText(plAmountString);
                        temp = "$" + df.format(totalBalance);
                        sellTotalBalanceTextView.setText(temp);
                    } else {
                        sellButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                        sellButton.setBackgroundColor(Color.parseColor("#808080"));
                        sellButton.setEnabled(false);

                        if (Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) < 1) {
                            sellProceedAmountTextView.setText("-");
                        } else {
                            sellProceedAmountTextView.setText("Not Enough Lots");
                        }
                        sellTotalBalanceTextView.setText("-");
                        sellPlAmountTextView.setText("-");
                    }
                } else {
                    sellProceedAmountTextView.setText("-");
                    sellPlAmountTextView.setText("-");
                    temp = "$" + df.format(UserArray.currentUser.getBalance());
                    sellTotalBalanceTextView.setText(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() + (currentPrice * Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString())));

                sellPortfolioDB(position, getApplicationContext());
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("lots", Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()));
                setResult(1, intent);
                finish();
            }
        });
    }

    private void sellPortfolioDB(int position, Context context) {
        String url = "http://192.168.100.18/vesting_webservice/sell_portfolio.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Sold " + Integer.parseInt(sellLotsTextInputLayout.getEditText().getText().toString()) + " lot of " + symbol + " for " + currentPrice, Toast.LENGTH_SHORT).show();

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

                params.put("symbol", symbol);
                params.put("lots", sellLotsTextInputLayout.getEditText().getText().toString());
                params.put("price", String.valueOf(currentPrice));
                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));
                params.put("balance", String.valueOf(UserArray.currentUser.getBalance()));

                return params;
            }
        };

        mQueue.add(request);
    }
}