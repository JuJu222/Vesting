package com.example.projectuas.ui.portfolio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectuas.PortfolioRecyclerViewAdapter;
import com.example.projectuas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import model.OwnedCompany;
import model.UserArray;

public class PortfolioFragment extends Fragment {
    private PortfolioRecyclerViewAdapter portfolioRecyclerViewAdapter;
    private ArrayList<OwnedCompany> dataOwnedCompany;
    private RecyclerView portfolioRecyclerView;
    private TextView portfolioBalanceTextView;
    private TextView portfolioEquityTextView;

    private PortfolioViewModel portfolioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        portfolioViewModel =
                new ViewModelProvider(this).get(PortfolioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_portfolio, container, false);
        portfolioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                dataOwnedCompany = UserArray.currentUser.getOwnedCompanies();

                portfolioRecyclerView = root.findViewById(R.id.portfolioRecyclerView);
                portfolioBalanceTextView = root.findViewById(R.id.portfolioBalanceTextView);
                portfolioEquityTextView = root.findViewById(R.id.portfolioEquityTextView);

                DecimalFormat df = new DecimalFormat("#.##");

                portfolioRecyclerViewAdapter = new PortfolioRecyclerViewAdapter(dataOwnedCompany, portfolioBalanceTextView, portfolioEquityTextView);

                String temp = "$" + df.format(UserArray.currentUser.getBalance());
                portfolioBalanceTextView.setText(temp);
                if (dataOwnedCompany.size() == 0) {
                    portfolioEquityTextView.setText(temp);
                }

                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                portfolioRecyclerView.setLayoutManager(manager);
                portfolioRecyclerView.setAdapter(portfolioRecyclerViewAdapter);
            }
        });
        return root;
    }
}