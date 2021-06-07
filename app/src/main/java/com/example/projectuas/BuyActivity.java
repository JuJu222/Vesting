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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import model.ListedCompany;
import model.UserArray;
import model.VolleyCallback;

public class BuyActivity extends AppCompatActivity {
    String symbol;
    String temp;
    double currentPrice;
    TextInputLayout buyLotsTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        TextView buySymbolTextView = findViewById(R.id.buySymbolTextView);
        TextView buyCurrentPriceTextView = findViewById(R.id.buyCurrentPriceTextView);
        TextView buyBalanceTextView = findViewById(R.id.buyBalanceTextView);
        TextView buyTotalCostTextView = findViewById(R.id.buyTotalCostTextView);
        TextView buyRemainingBalanceTextView = findViewById(R.id.buyRemainingBalanceTextView);
        buyLotsTextInputLayout = findViewById(R.id.buyLotsTextInputLayout);
        Button buyButton = findViewById(R.id.buyButton);

        Intent intent = getIntent();
        symbol = intent.getStringExtra("symbol");
        currentPrice = intent.getDoubleExtra("currentPrice", 0);

        buyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        buyButton.setBackgroundColor(Color.parseColor("#808080"));
        buyButton.setEnabled(false);
        DecimalFormat df = new DecimalFormat("#.##");
        temp = "Buy " + symbol;
        buySymbolTextView.setText(temp);
        temp = "$" + df.format(currentPrice);
        buyCurrentPriceTextView.setText(temp);
        temp = "$" + df.format(UserArray.currentUser.getBalance());
        buyBalanceTextView.setText(temp);
        buyTotalCostTextView.setText("-");
        buyRemainingBalanceTextView.setText(temp);

        buyLotsTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = buyLotsTextInputLayout.getEditText().getText().toString().isEmpty();

                if (!isEmpty) {
                    boolean isAbove0 = Integer.parseInt(buyLotsTextInputLayout.getEditText().getText().toString()) > 0;
                    boolean isExceedBalance = currentPrice * Integer.parseInt(buyLotsTextInputLayout.getEditText().getText().toString()) > UserArray.currentUser.getBalance();

                    double totalCost = Integer.parseInt(buyLotsTextInputLayout.getEditText().getText().toString()) * currentPrice;
                    double remainingBalance = UserArray.currentUser.getBalance() - totalCost;

                    if (isAbove0 && !isExceedBalance) {
                        buyButton.getBackground().setColorFilter(null);
                        buyButton.setBackgroundColor(Color.parseColor("#41A03A"));
                        buyButton.setEnabled(true);

                        temp = "$" + df.format(totalCost);
                        buyTotalCostTextView.setText(temp);
                        temp = "$" + df.format(remainingBalance);
                        buyRemainingBalanceTextView.setText(temp);
                    } else {
                        buyButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                        buyButton.setBackgroundColor(Color.parseColor("#808080"));
                        buyButton.setEnabled(false);

                        if (Integer.parseInt(buyLotsTextInputLayout.getEditText().getText().toString()) < 1) {
                            buyTotalCostTextView.setText("-");
                        } else {
                            buyTotalCostTextView.setText("Not Enough Balance");
                        }
                        buyRemainingBalanceTextView.setText("-");
                    }
                } else {
                    temp = "$" + df.format(UserArray.currentUser.getBalance());
                    buyRemainingBalanceTextView.setText(temp);
                    buyTotalCostTextView.setText("-");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() - (currentPrice * Double.parseDouble(buyLotsTextInputLayout.getEditText().getText().toString())));

                buyPortfolioDB(getApplicationContext());
            }
        });
    }

    private void buyPortfolioDB(Context context) {
        String url = "http://192.168.0.146/vesting_webservice/buy_portfolio.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Toast.makeText(getApplicationContext(), "Bought " + buyLotsTextInputLayout.getEditText().getText().toString() + " lot of " + symbol + " for " + currentPrice, Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(buyLotsTextInputLayout.getWindowToken(), 0);
                finish();
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
                params.put("lots", buyLotsTextInputLayout.getEditText().getText().toString());
                params.put("price", String.valueOf(currentPrice));
                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));
                params.put("balance", String.valueOf(UserArray.currentUser.getBalance()));

                return params;
            }
        };

        mQueue.add(request);
    }
}