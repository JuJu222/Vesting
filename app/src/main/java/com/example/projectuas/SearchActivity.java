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
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import model.Companies;
import model.ListedCompany;

public class SearchActivity extends AppCompatActivity implements SearchRecyclerViewAdapter.ItemClickListener  {
    SearchRecyclerViewAdapter adapter;
    Companies companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout2.getEditText().requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchRecyclerViewAdapter(this, companies.getListedCompanies());
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
                ArrayList<ListedCompany> filteredList = new ArrayList<>();

                for (ListedCompany item : companies.getListedCompanies()) {
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