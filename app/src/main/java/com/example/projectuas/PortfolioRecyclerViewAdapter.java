package com.example.projectuas;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.OwnedCompany;
import model.Stocks;
import model.UserArray;
import model.VolleyCallback;

public class PortfolioRecyclerViewAdapter extends RecyclerView.Adapter<PortfolioRecyclerViewAdapter.PortfolioViewHolder> {
    private ArrayList<OwnedCompany> ownedCompanyArrayList;
    private TextView portfolioBalanceTextView;
    private TextView portfolioEquityTextView;
    private double totalCurrentValue;
    private boolean buttonClicked;

    public PortfolioRecyclerViewAdapter(ArrayList<OwnedCompany> dataListedCompany, TextView portfolioBalanceTextView, TextView portfolioEquityTextView) {
        ownedCompanyArrayList = dataListedCompany;
        this.portfolioBalanceTextView = portfolioBalanceTextView;
        this.portfolioEquityTextView = portfolioEquityTextView;
        totalCurrentValue = 0;
        this.buttonClicked = false;
    }

    @NonNull
    @Override
    public PortfolioRecyclerViewAdapter.PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_portfolio_row, parent, false);
        return new PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioRecyclerViewAdapter.PortfolioViewHolder holder, int position) {
        holder.portfolioRowTextView.setText(ownedCompanyArrayList.get(position).getCompanySymbol());
        holder.portfolioRowJumlahLotTextView.setText(String.valueOf(ownedCompanyArrayList.get(position).getLots()));
        holder.portfolioRowAveragePriceTextView.setText(String.valueOf(ownedCompanyArrayList.get(position).getAverageBoughtPrice()));
        Stocks stocks = new Stocks();
        RequestQueue mQueue = Volley.newRequestQueue(holder.portfolioRowTextView.getContext());
        if (ownedCompanyArrayList.size() != 0) {
            holder.portfolioRowSellButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#808080"));
            holder.portfolioRowSellButton.setEnabled(false);
            holder.portfolioRowLotsTextInputLayout.setEnabled(false);

            DecimalFormat df = new DecimalFormat("#.##");

            stocks.getCurrentPrice(mQueue, ownedCompanyArrayList.get(position).getCompanySymbol(), new VolleyCallback(){
                @Override
                public void onSuccess(String result){
                    try {
                        double priceOld = ownedCompanyArrayList.get(position).getAverageBoughtPrice();
                        double priceClose = Double.parseDouble(result);
                        double priceChangeDouble = (priceClose - priceOld) / priceOld * 100;
                        String priceChangeString = df.format(priceChangeDouble) + "%";
                        if (priceChangeDouble > 0) {
                            priceChangeString = "+" + priceChangeString;
                            holder.portfolioRowPercentageChange.setTextColor(Color.GREEN);
                        } else if (priceChangeDouble < 0) {
                            holder.portfolioRowPercentageChange.setTextColor(Color.RED);
                        } else {
                            holder.portfolioRowPercentageChange.setTextColor(Color.WHITE);
                        }
                        holder.portfolioRowPercentageChange.setText(priceChangeString);
                        holder.portfolioRowCurrentPriceTextView.setText(df.format(priceClose));

                        if (!buttonClicked) {
                            totalCurrentValue += priceClose * ownedCompanyArrayList.get(position).getLots();
                            String temp = "$" + df.format(totalCurrentValue + UserArray.currentUser.getBalance());
                            portfolioEquityTextView.setText(temp);
                        }

                        holder.portfolioRowLotsTextInputLayout.setEnabled(true);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            holder.portfolioRowLotsTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString().isEmpty()) {
                        holder.portfolioRowSellButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                        holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#808080"));
                        holder.portfolioRowSellButton.setEnabled(false);
                    } else {
                        if (Integer.parseInt(holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString()) <= ownedCompanyArrayList.get(position).getLots() && Integer.parseInt(holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString()) > 0) {
                            holder.portfolioRowSellButton.getBackground().setColorFilter(null);
                            holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#980000"));
                            holder.portfolioRowSellButton.setEnabled(true);
                        } else {
                            holder.portfolioRowSellButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                            holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#808080"));
                            holder.portfolioRowSellButton.setEnabled(false);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.portfolioRowSellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked = true;
                    UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() + (Double.parseDouble(holder.portfolioRowCurrentPriceTextView.getText().toString()) * Integer.parseInt(holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString())));
                    sellPortfolioDB(holder, position, holder.portfolioRowAveragePriceTextView.getContext());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ownedCompanyArrayList.size();
    }

    public static class PortfolioViewHolder extends RecyclerView.ViewHolder {
        TextView portfolioRowTextView;
        TextView portfolioRowJumlahLotTextView;
        TextView portfolioRowAveragePriceTextView;
        TextView portfolioRowCurrentPriceTextView;
        TextView portfolioRowPercentageChange;
        Button portfolioRowSellButton;
        TextInputLayout portfolioRowLotsTextInputLayout;

        public PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);
            portfolioRowTextView = itemView.findViewById(R.id.portfolioRowTextView);
            portfolioRowJumlahLotTextView = itemView.findViewById(R.id.portfolioRowJumlahLotTextView);
            portfolioRowAveragePriceTextView = itemView.findViewById(R.id.portfolioRowAveragePriceTextView);
            portfolioRowCurrentPriceTextView = itemView.findViewById(R.id.portfolioRowCurrentPriceTextView);
            portfolioRowPercentageChange = itemView.findViewById(R.id.portfolioRowPercentageChange);
            portfolioRowSellButton = itemView.findViewById(R.id.portfolioRowSellButton);
            portfolioRowLotsTextInputLayout = itemView.findViewById(R.id.portfolioRowLotsTextInputLayout);
        }
    }

    private void sellPortfolioDB(@NonNull PortfolioRecyclerViewAdapter.PortfolioViewHolder holder, int position, Context context) {
        String url = "http://192.168.100.18/vesting_webservice/sell_portfolio.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(holder.portfolioRowAveragePriceTextView.getContext(), "Sold " + Integer.parseInt(holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString()) + " lot of " + ownedCompanyArrayList.get(position).getCompanySymbol() + " for " + holder.portfolioRowCurrentPriceTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                ownedCompanyArrayList.get(position).setLots(ownedCompanyArrayList.get(position).getLots() - Integer.parseInt(holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString()));
                holder.portfolioRowLotsTextInputLayout.getEditText().setText("");
                DecimalFormat df = new DecimalFormat("#.##");
                String temp = "$" + df.format(UserArray.currentUser.getBalance());
                portfolioBalanceTextView.setText(temp);
                if (ownedCompanyArrayList.get(position).getLots() == 0) {
                    ownedCompanyArrayList.remove(position);
                }
                notifyDataSetChanged();
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

                params.put("symbol", ownedCompanyArrayList.get(position).getCompanySymbol());
                params.put("lots", holder.portfolioRowLotsTextInputLayout.getEditText().getText().toString());
                params.put("price", holder.portfolioRowCurrentPriceTextView.getText().toString());
                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));
                params.put("balance", String.valueOf(UserArray.currentUser.getBalance()));

                return params;
            }
        };

        mQueue.add(request);
    }
}
