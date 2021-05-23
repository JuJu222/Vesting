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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.User;
import model.UserArray;

public class Register extends AppCompatActivity {

    private TextInputLayout signup_textInput_email, signup_textInput_name, signup_textInput_pass;
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

        signup_button_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = signup_textInput_email.getEditText().getText().toString().trim();
                String nama = signup_textInput_name.getEditText().getText().toString().trim();
                String password = signup_textInput_pass.getEditText().getText().toString().trim();
                if(email.isEmpty()){
                    signup_textInput_email.setError("Please fill the email column");
                }else{
                    signup_textInput_email.setError("");
                }
                if(nama.isEmpty()){
                    signup_textInput_name.setError("please fill the name column");
                }else{
                    signup_textInput_name.setError("");
                }
                if(password.isEmpty()){
                    signup_textInput_pass.setError("please fill the password column");
                }else{
                    signup_textInput_pass.setError("");
                }

                if(!email.isEmpty() && !nama.isEmpty() && !password.isEmpty()){
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    User user = new User(email, nama, password, new ArrayList<>());
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
    }

    private void addDataDB(Context context) {
        String url = "http://192.168.100.18/vesting_webservice/create_user.php";

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

                return params;
            }
        };

        mQueue.add(request);
    }
}