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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import model.User;
import model.UserArray;

public class Register extends AppCompatActivity {

    private TextInputLayout signup_textInput_email, signup_textInput_name, signup_textInput_pass, signup_textInput_phone, signup_textInput_address;
    private Button signup_button_Signup, signup_button_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signup_textInput_email = findViewById(R.id.signup_textInput_email);
        signup_textInput_name = findViewById(R.id.signup_textInput_name);
        signup_textInput_pass = findViewById(R.id.signup_textInput_pass);
        signup_button_Login = findViewById(R.id.signup_button_Login);
        signup_button_Signup = findViewById(R.id.signup_button_Signup);
        signup_textInput_phone = findViewById(R.id.signup_textInput_phone);
        signup_textInput_address = findViewById(R.id.signup_textInput_address);

        signup_button_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_textInput_email.getEditText().getText().toString().trim();
                String nama = signup_textInput_name.getEditText().getText().toString().trim();
                String password = signup_textInput_pass.getEditText().getText().toString().trim();
                String phone = signup_textInput_phone.getEditText().getText().toString().trim();
                String address = signup_textInput_address.getEditText().getText().toString().trim();

                if(!email.isEmpty() && !nama.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !address.isEmpty()){
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    User user = new User(email, nama, password, phone, address, new ArrayList<>());
                    for (int i = 0; i < UserArray.akunuser.size(); i++){
                        if(user.getEmail().equalsIgnoreCase(UserArray.akunuser.get(i).getEmail())){
                            Toast.makeText(getBaseContext(), "Email is already Registered!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    addDataDB(getApplicationContext());
                }
            }
        });

        signup_button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signup_textInput_email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = signup_textInput_email.getEditText().getText().toString().trim();

                Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

                if(email.isEmpty()){
                    signup_textInput_email.setError("Please fill the email column");
                }else{
                    if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
                        signup_textInput_email.setError("Wrong format email");
                    }else{
                        signup_textInput_email.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_textInput_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nama = signup_textInput_name.getEditText().getText().toString().trim();

                if(nama.isEmpty()){
                    signup_textInput_name.setError("please fill the name column");
                }else{
                    if(nama.length() < 4 || nama.length() > 20){
                        signup_textInput_name.setError("Username must be 4 to 20 characters");
                    }else{
                        signup_textInput_name.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_textInput_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signup_textInput_pass.getEditText().getText().toString().trim();

                Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{0,20}");

                if (password.isEmpty()){
                    signup_textInput_pass.setError("Please fill the password column");
                }else{
                    if (password.length() < 4 || password.length() > 20){
                        signup_textInput_pass.setError("Password must be 4 to 20 characters");
                    }else if (!PASSWORD_PATTERN.matcher(password).matches()){
                        signup_textInput_pass.setError("Must contain a - z, A - Z, !, @, #, $");
                    }else{
                        signup_textInput_pass.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_textInput_phone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = signup_textInput_phone.getEditText().getText().toString().trim();

                if(phone.isEmpty()){
                    signup_textInput_phone.setError("please fill the phone column");
                }else{
                    if(phone.length()<7 || phone.length() >15){
                        signup_textInput_phone.setError("Phone number must be 7 to 15 digits");
                    }else{
                        signup_textInput_phone.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup_textInput_address.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = signup_textInput_address.getEditText().getText().toString().trim();

                if(address.isEmpty()){
                    signup_textInput_address.setError("please fill the Address column");
                }else{
                    signup_textInput_address.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addDataDB(Context context) {
        String url = "http://192.168.0.146/vesting_webservice/create_user.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                if (response.equalsIgnoreCase("{\"message\":\"Failed to save\"}")) {
                    Toast.makeText(getApplicationContext(), "Email Address Already Exists!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Account Created! Please Log In", Toast.LENGTH_SHORT).show();
                    finish();
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

                params.put("name", signup_textInput_name.getEditText().getText().toString().trim());
                params.put("email", signup_textInput_email.getEditText().getText().toString().trim());
                params.put("password", signup_textInput_pass.getEditText().getText().toString().trim());
                params.put("phone_number", signup_textInput_phone.getEditText().getText().toString().trim());
                params.put("address", signup_textInput_address.getEditText().getText().toString().trim());

                return params;
            }
        };

        mQueue.add(request);
    }
}