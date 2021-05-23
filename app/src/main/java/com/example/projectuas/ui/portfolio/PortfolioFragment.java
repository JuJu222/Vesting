package com.example.projectuas.ui.portfolio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.Login;
import com.example.projectuas.MainActivity;
import com.example.projectuas.PortfolioRecyclerViewAdapter;
import com.example.projectuas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.OwnedCompany;
import model.User;
import model.UserArray;

public class PortfolioFragment extends Fragment {
    private PortfolioRecyclerViewAdapter portfolioRecyclerViewAdapter;
    private ArrayList<OwnedCompany> dataOwnedCompany = new ArrayList<>();
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
//                dataOwnedCompany = UserArray.currentUser.getOwnedCompanies();
                loadDataDB(getContext());

                portfolioRecyclerView = root.findViewById(R.id.portfolioRecyclerView);
                portfolioBalanceTextView = root.findViewById(R.id.portfolioBalanceTextView);
                portfolioEquityTextView = root.findViewById(R.id.portfolioEquityTextView);

                portfolioRecyclerViewAdapter = new PortfolioRecyclerViewAdapter(dataOwnedCompany, portfolioBalanceTextView, portfolioEquityTextView);

                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                portfolioRecyclerView.setLayoutManager(manager);
                portfolioRecyclerView.setAdapter(portfolioRecyclerViewAdapter);
            }
        });
        return root;
    }

    private void loadDataDB(Context context) {
        String url = "http://192.168.100.18/vesting_webservice/read_all_portfolio.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    double balance = jsonObject.getDouble("balance");
                    UserArray.currentUser.setBalance(balance);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String temp = "$" + df.format(UserArray.currentUser.getBalance());
                    if (dataOwnedCompany.size() == 0) {
                        portfolioEquityTextView.setText(temp);
                    }
                    portfolioBalanceTextView.setText(temp);

                    JSONArray jsonOwnedCompany = jsonObject.getJSONArray("portfolio");

                    for (int i = 0; i < jsonOwnedCompany.length(); i++) {
                        JSONObject objPortfolio = jsonOwnedCompany.getJSONObject(i);
                        OwnedCompany ownedCompany = new OwnedCompany();
                        ownedCompany.setCompanySymbol(objPortfolio.getString("symbol"));
                        ownedCompany.setLots(objPortfolio.getInt("lots"));
                        ownedCompany.setAverageBoughtPrice(objPortfolio.getDouble("price"));

                        dataOwnedCompany.add(ownedCompany);
                    }

                    portfolioRecyclerViewAdapter.notifyDataSetChanged();
                }catch (JSONException err){
                    err.printStackTrace();
                }
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

                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));

                return params;
            }
        };

        mQueue.add(request);
    }
}