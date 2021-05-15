package com.example.projectuas;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.ArrayList;

import model.OwnedCompany;
import model.Stocks;
import model.TinyDB;
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

                        holder.portfolioRowSellButton.getBackground().setColorFilter(null);
                        holder.portfolioRowSellButton.setBackgroundColor(Color.parseColor("#980000"));
                        holder.portfolioRowSellButton.setEnabled(true);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });

            holder.portfolioRowSellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked = true;
                    Toast.makeText(holder.portfolioRowAveragePriceTextView.getContext(), "Sold 1 lot of " + ownedCompanyArrayList.get(position).getCompanySymbol() + " for " + holder.portfolioRowCurrentPriceTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    ownedCompanyArrayList.get(position).setLots(ownedCompanyArrayList.get(position).getLots() - 1);
                    UserArray.currentUser.setBalance(UserArray.currentUser.getBalance() + Double.parseDouble(holder.portfolioRowCurrentPriceTextView.getText().toString()));
                    if (ownedCompanyArrayList.get(position).getLots() == 0) {
                        ownedCompanyArrayList.remove(position);
                    }

                    String temp = "$" + df.format(UserArray.currentUser.getBalance());
                    portfolioBalanceTextView.setText(temp);

                    TinyDB tinydb = new TinyDB(holder.portfolioRowTextView.getContext());

                    UserArray.akunuser = tinydb.getListUser("akunuser");
                    for (int i = 0; i < UserArray.akunuser.size(); i++) {
                        if (UserArray.currentUser.getEmail().equalsIgnoreCase(UserArray.akunuser.get(i).getEmail())) {
                            UserArray.akunuser.set(i, UserArray.currentUser);
                        }
                    }
                    tinydb.putListUser("akunuser", UserArray.akunuser);
                    notifyDataSetChanged();
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
