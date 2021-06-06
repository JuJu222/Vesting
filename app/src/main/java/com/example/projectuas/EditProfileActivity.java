package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import model.UserArray;

public class EditProfileActivity extends AppCompatActivity {
    TextInputLayout editProfileBalanceTextInputLayout;
    TextInputLayout editProfilePhoneNumberTextInputLayout;
    TextInputLayout editProfileAddressTextInputLayout;
    Button editProfileSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileBalanceTextInputLayout = findViewById(R.id.editProfileBalanceTextInputLayout);
        editProfilePhoneNumberTextInputLayout = findViewById(R.id.editProfilePhoneNumberTextInputLayout);
        editProfileAddressTextInputLayout = findViewById(R.id.editProfileAddressTextInputLayout);
        editProfileSubmitButton = findViewById(R.id.editProfileSubmitButton);

        editProfileSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDB(getApplicationContext());

                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });
    }

    private void updateUserDB(Context context) {
        String url = "http://192.168.0.146/vesting_webservice/update_user.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                DecimalFormat df = new DecimalFormat("#.##");
                String temp = df.format(Double.parseDouble(editProfileBalanceTextInputLayout.getEditText().getText().toString()));
                UserArray.currentUser.setBalance(Double.parseDouble(temp));

                params.put("user_id", String.valueOf(UserArray.currentUser.getId()));
                params.put("balance", temp);
                params.put("phone_number", editProfilePhoneNumberTextInputLayout.getEditText().getText().toString());
                params.put("address", editProfileAddressTextInputLayout.getEditText().getText().toString());

                return params;
            }
        };

        mQueue.add(request);
    }
}