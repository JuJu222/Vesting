package com.example.projectuas;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
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
    private Fragment fragment;
    private ArrayList<OwnedCompany> ownedCompanyArrayList;
    private TextView portfolioEquityTextView;
    private double totalCurrentValue;
    private boolean buttonClicked;
    double priceChangeDouble;

    public PortfolioRecyclerViewAdapter(Fragment fragment, ArrayList<OwnedCompany> dataListedCompany, TextView portfolioEquityTextView) {
        this.fragment = fragment;
        ownedCompanyArrayList = dataListedCompany;
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

            DecimalFormat df = new DecimalFormat("#.##");

            stocks.getCurrentPrice(mQueue, ownedCompanyArrayList.get(position).getCompanySymbol(), new VolleyCallback(){
                @Override
                public void onSuccess(String result){
                    holder.portfolioRowSellButton.getBackground().setColorFilter(null);
                    holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#980000"));
                    holder.portfolioRowSellButton.setEnabled(true);
                    try {
                        double priceOld = ownedCompanyArrayList.get(position).getAverageBoughtPrice();
                        double priceClose = Double.parseDouble(result);
                        priceChangeDouble = (priceClose - priceOld) / priceOld * 100;
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
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            holder.portfolioRowSellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked = true;

                    Intent intent = new Intent(holder.portfolioRowAveragePriceTextView.getContext(), SellActivity.class);
                    intent.putExtra("symbol", ownedCompanyArrayList.get(position).getCompanySymbol());
                    intent.putExtra("lots", ownedCompanyArrayList.get(position).getLots());
                    intent.putExtra("currentPrice", Double.parseDouble(holder.portfolioRowCurrentPriceTextView.getText().toString()));
                    intent.putExtra("averagePrice", Double.parseDouble(holder.portfolioRowAveragePriceTextView.getText().toString()));
                    String temp = holder.portfolioRowPercentageChange.getText().toString();
                    temp = temp.replace("%", "");
                    temp = temp.replace("+", "");
                    intent.putExtra("percentage", Double.parseDouble(temp));
                    intent.putExtra("position", position);
                    fragment.startActivityForResult(intent, 1);
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

        public PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);
            portfolioRowTextView = itemView.findViewById(R.id.portfolioRowTextView);
            portfolioRowJumlahLotTextView = itemView.findViewById(R.id.portfolioRowJumlahLotTextView);
            portfolioRowAveragePriceTextView = itemView.findViewById(R.id.portfolioRowAveragePriceTextView);
            portfolioRowCurrentPriceTextView = itemView.findViewById(R.id.portfolioRowCurrentPriceTextView);
            portfolioRowPercentageChange = itemView.findViewById(R.id.portfolioRowPercentageChange);
            portfolioRowSellButton = itemView.findViewById(R.id.portfolioRowSellButton);
        }
    }
}
