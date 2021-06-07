package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        editProfileBalanceTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String balance = editProfileBalanceTextInputLayout.getEditText().getText().toString().trim();

                if(balance.isEmpty()){
                    editProfileBalanceTextInputLayout.setError("please fill the balance column");
                }else{
                    if(balance.length()<1 || balance.length()>15){
                        editProfileBalanceTextInputLayout.setError("Balance number must be 1 to 15 digits");
                    }else{
                        editProfileBalanceTextInputLayout.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editProfilePhoneNumberTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = editProfilePhoneNumberTextInputLayout.getEditText().getText().toString().trim();

                if(phone.isEmpty()){
                    editProfilePhoneNumberTextInputLayout.setError("please fill the phone column");
                }else{
                    if(phone.length()<7 || phone.length() >15){
                        editProfilePhoneNumberTextInputLayout.setError("Phone number must be 7 to 15 digits");
                    }else{
                        editProfilePhoneNumberTextInputLayout.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editProfileAddressTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = editProfileAddressTextInputLayout.getEditText().getText().toString().trim();

                if(address.isEmpty()){
                    editProfileAddressTextInputLayout.setError("please fill the Address column");
                }else{
                    editProfileAddressTextInputLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateUserDB(Context context) {
        String url = "http://192.168.100.18/vesting_webservice/update_user.php";

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