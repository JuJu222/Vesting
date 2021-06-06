package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import model.User;
import model.UserArray;

public class Login extends AppCompatActivity {

    private TextInputLayout login_textInput_email, login_textInput_pass;
    private Button login_button_Login, login_button_Signup;
    private Boolean validateEmail, validatePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_textInput_email = findViewById(R.id.login_textInput_email);
        login_textInput_pass = findViewById(R.id.login_textInput_pass);
        login_button_Login = findViewById(R.id.login_button_Login);
        login_button_Signup= findViewById(R.id.login_button_Signup);
        validateEmail = false;
        validatePass = false;

        login_button_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        login_button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail && validatePass){
                    loginDB(getApplicationContext());
                }else {
                    login_textInput_email.setError("Please correct the email column");
                    login_textInput_pass.setError("Please correct the password column");
                }
            }
        });

        login_textInput_email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = login_textInput_email.getEditText().getText().toString().trim();

                Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

                if(email.isEmpty()){
                    login_textInput_email.setError("Please fill the email column");
                    validateEmail = false;
                }else{
                    if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()){
                        login_textInput_email.setError("Wrong format email");
                        validateEmail = false;
                    }else{
                        login_textInput_email.setError("");
                        validateEmail = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login_textInput_pass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = login_textInput_pass.getEditText().getText().toString().trim();

                Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{0,20}");

                if (password.isEmpty()){
                    login_textInput_pass.setError("Please fill the password column");
                    validatePass = false;
                }else{
                    if (password.length() < 4 || password.length() > 20){
                        login_textInput_pass.setError("Password must be 4 to 20 characters");
                        validatePass = false;
                    }else if (!PASSWORD_PATTERN.matcher(password).matches()){
                        login_textInput_pass.setError("Must contain a - z, A - Z, !, @, #, $");
                        validatePass = false;
                    }else{
                        login_textInput_pass.setError("");
                        validatePass = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loginDB(Context context) {
        String url = "http://192.168.100.18/vesting_webservice/read_user.php";

        RequestQueue mQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject objUser = jsonObject.getJSONObject("user");

                    User user = new User();
                    user.setId(objUser.getInt("user_id"));
                    user.setNama(objUser.getString("name"));
                    user.setEmail(objUser.getString("email"));
                    user.setBalance(objUser.getDouble("balance"));
                    user.setPhoneNumber(objUser.getString("phone_number"));
                    user.setAddress(objUser.getString("address"));

                    UserArray.currentUser = user;

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putBoolean("loggedIn", true).apply();
                    prefs.edit().putInt("id", objUser.getInt("user_id")).apply();
                    prefs.edit().putString("nama", objUser.getString("name")).apply();
                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                }catch (JSONException err){
                    Toast.makeText(getApplicationContext(), "Unabled to Login, Wrong email/password", Toast.LENGTH_SHORT).show();
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

                params.put("email", login_textInput_email.getEditText().getText().toString().trim());
                params.put("password", login_textInput_pass.getEditText().getText().toString().trim());

                return params;
            }
        };

        mQueue.add(request);
    }
}