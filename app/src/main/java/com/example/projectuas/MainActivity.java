package com.example.projectuas;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import model.Companies;
import model.ListedCompany;
import model.User;
import model.UserArray;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_portfolio, R.id.navigation_news, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        intent = getIntent();

        User user = intent.getParcelableExtra("IDuser");

        Companies.getListedCompanies().clear();
        Companies.getListedCompanies().add(new ListedCompany("MMM"));
        Companies.getListedCompanies().add(new ListedCompany("AXP"));
        Companies.getListedCompanies().add(new ListedCompany("AMGN"));
        Companies.getListedCompanies().add(new ListedCompany("AAPL"));
        Companies.getListedCompanies().add(new ListedCompany("BA"));
        Companies.getListedCompanies().add(new ListedCompany("CAT"));
        Companies.getListedCompanies().add(new ListedCompany("CVX"));
        Companies.getListedCompanies().add(new ListedCompany("CSCO"));
        Companies.getListedCompanies().add(new ListedCompany("KO"));
        Companies.getListedCompanies().add(new ListedCompany("DOW"));
        Companies.getListedCompanies().add(new ListedCompany("GS"));
        Companies.getListedCompanies().add(new ListedCompany("HD"));
        Companies.getListedCompanies().add(new ListedCompany("HON"));
        Companies.getListedCompanies().add(new ListedCompany("IBM"));
        Companies.getListedCompanies().add(new ListedCompany("INTC"));
        Companies.getListedCompanies().add(new ListedCompany("JNJ"));
        Companies.getListedCompanies().add(new ListedCompany("JPM"));
        Companies.getListedCompanies().add(new ListedCompany("MCD"));
        Companies.getListedCompanies().add(new ListedCompany("MRK"));
        Companies.getListedCompanies().add(new ListedCompany("MSFT"));
        Companies.getListedCompanies().add(new ListedCompany("NKE"));
        Companies.getListedCompanies().add(new ListedCompany("PG"));
        Companies.getListedCompanies().add(new ListedCompany("CRM"));
        Companies.getListedCompanies().add(new ListedCompany("TRV"));
        Companies.getListedCompanies().add(new ListedCompany("UNH"));
        Companies.getListedCompanies().add(new ListedCompany("VZ"));
        Companies.getListedCompanies().add(new ListedCompany("V"));
        Companies.getListedCompanies().add(new ListedCompany("WBA"));
        Companies.getListedCompanies().add(new ListedCompany("WMT"));
        Companies.getListedCompanies().add(new ListedCompany("DIS"));
        Companies.getListedCompanies().add(new ListedCompany("ABMD"));
        Companies.getListedCompanies().add(new ListedCompany("ABT"));
        Companies.getListedCompanies().add(new ListedCompany("ABBV"));
        Companies.getListedCompanies().add(new ListedCompany("ADBE"));
        Companies.getListedCompanies().add(new ListedCompany("AMD"));
        Companies.getListedCompanies().add(new ListedCompany("T"));
        Companies.getListedCompanies().add(new ListedCompany("BBY"));
        Companies.getListedCompanies().add(new ListedCompany("BAC"));
        Companies.getListedCompanies().add(new ListedCompany("BA"));
        Companies.getListedCompanies().add(new ListedCompany("KO"));
        Companies.getListedCompanies().add(new ListedCompany("EBAY"));
        Companies.getListedCompanies().add(new ListedCompany("EA"));
        Companies.getListedCompanies().add(new ListedCompany("EQIX"));
        Companies.getListedCompanies().add(new ListedCompany("FB"));
        Companies.getListedCompanies().add(new ListedCompany("MAR"));
        Companies.getListedCompanies().add(new ListedCompany("MCHP"));
        Companies.getListedCompanies().add(new ListedCompany("WRK"));
        Companies.getListedCompanies().add(new ListedCompany("XYL"));
        Companies.getListedCompanies().add(new ListedCompany("ZBRA"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Companies.getListedCompanies().sort(Comparator.comparing(ListedCompany::getCompanySymbol));
        }

        dataDB(this);
    }

    public void dataDB(Context context) {
        String url = "http://192.168.100.18/vesting_webservice/read_user_by_id.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject objUser = jsonObject.getJSONObject("user");

                    UserArray.currentUser.setBalance(objUser.getDouble("balance"));
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
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