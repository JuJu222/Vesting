package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import model.Companies;
import model.Company;

public class SearchActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener  {
    RecyclerViewAdapter adapter;
    Companies companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);

        companies = new Companies(new ArrayList<>());
        companies.getListedCompanies().add(new Company("MMM", null));
        companies.getListedCompanies().add(new Company("AXP", null));
        companies.getListedCompanies().add(new Company("AMGN", null));
        companies.getListedCompanies().add(new Company("AAPL", null));
        companies.getListedCompanies().add(new Company("BA", null));
        companies.getListedCompanies().add(new Company("CAT", null));
        companies.getListedCompanies().add(new Company("CVX", null));
        companies.getListedCompanies().add(new Company("CSCO", null));
        companies.getListedCompanies().add(new Company("KO", null));
        companies.getListedCompanies().add(new Company("DOW", null));
        companies.getListedCompanies().add(new Company("GS", null));
        companies.getListedCompanies().add(new Company("HD", null));
        companies.getListedCompanies().add(new Company("HON", null));
        companies.getListedCompanies().add(new Company("IBM", null));
        companies.getListedCompanies().add(new Company("INTC", null));
        companies.getListedCompanies().add(new Company("JNJ", null));
        companies.getListedCompanies().add(new Company("JPM", null));
        companies.getListedCompanies().add(new Company("MCD", null));
        companies.getListedCompanies().add(new Company("MRK", null));
        companies.getListedCompanies().add(new Company("MSFT", null));
        companies.getListedCompanies().add(new Company("NKE", null));
        companies.getListedCompanies().add(new Company("PG", null));
        companies.getListedCompanies().add(new Company("CRM", null));
        companies.getListedCompanies().add(new Company("TRV", null));
        companies.getListedCompanies().add(new Company("UNH", null));
        companies.getListedCompanies().add(new Company("VZ", null));
        companies.getListedCompanies().add(new Company("V", null));
        companies.getListedCompanies().add(new Company("WBA", null));
        companies.getListedCompanies().add(new Company("WMT", null));
        companies.getListedCompanies().add(new Company("DIS", null));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, companies.getListedCompanies());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        textInputLayout2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            private void filter(String text) {
                ArrayList<Company> filteredList = new ArrayList<>();

                for (Company item : companies.getListedCompanies()) {
                    if (item.getCompanySymbol().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(item);
                    }
                }

                adapter.filterList(filteredList);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(SearchActivity.this, CompanyActivity.class);
        intent.putExtra("company", adapter.getItem(position));
        startActivity(intent);
    }
}