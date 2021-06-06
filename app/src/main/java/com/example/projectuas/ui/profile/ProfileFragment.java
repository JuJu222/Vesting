package com.example.projectuas.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectuas.EditProfileActivity;
import com.example.projectuas.Login;
import com.example.projectuas.MainActivity;
import com.example.projectuas.PortfolioRecyclerViewAdapter;
import com.example.projectuas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.OwnedCompany;
import model.User;
import model.UserArray;

public class ProfileFragment extends Fragment {
    private TextView profileNamaTextView;
    private TextView profileEmailTextView;
    private TextView profileBalanceTextView;
    private TextView profilePhoneNumberTextView;
    private TextView profileAddressTextView;
    private Button profileEditProfileButton;
    private Button profileLogOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileNamaTextView = root.findViewById(R.id.profileNamaTextView);
        profileEmailTextView = root.findViewById(R.id.profileEmailTextView);
        profileBalanceTextView = root.findViewById(R.id.profileBalanceTextView);
        profilePhoneNumberTextView = root.findViewById(R.id.profilePhoneNumberTextView);
        profileAddressTextView = root.findViewById(R.id.profileAddressTextView);
        profileEditProfileButton = root.findViewById(R.id.profileEditProfileButton);
        profileLogOutButton = root.findViewById(R.id.profileLogOutButton);

        dataDB(getContext());

        profileEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        profileLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefs.edit().putBoolean("loggedIn", false).apply();

                startActivity(intent);
                getActivity().finish();

                Toast.makeText(getContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            dataDB(getContext());
        }
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

                    profileNamaTextView.setText(objUser.getString("name"));
                    profileEmailTextView.setText(objUser.getString("email"));
                    DecimalFormat df = new DecimalFormat("#.##");
                    String temp = "$" + df.format(objUser.getDouble("balance"));
                    profileBalanceTextView.setText(temp);
                    profilePhoneNumberTextView.setText(objUser.getString("phone_number"));
                    profileAddressTextView.setText(objUser.getString("address"));
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