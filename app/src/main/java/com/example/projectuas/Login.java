package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

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

        getSupportActionBar().hide();

        login_textInput_email = findViewById(R.id.login_textInput_email);
        login_textInput_pass = findViewById(R.id.login_textInput_pass);
        login_button_Login = findViewById(R.id.login_button_Login);
        login_button_Signup= findViewById(R.id.login_button_Signup);
        validateEmail = false;
        validatePass = false;

        login_button_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });

        login_button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_textInput_email.getEditText().getText().toString().trim();
                String password = login_textInput_pass.getEditText().getText().toString().trim();

                if(validateEmail && validatePass){
                    Boolean login = false;
                    for (int i = 0; i < UserArray.akunuser.size(); i++){
                        User tempUser = UserArray.akunuser.get(i);
                        if(tempUser.getEmail().equalsIgnoreCase(email) && tempUser.getPassword().equals(password)){
                            Intent intent = new Intent(getBaseContext(), Homepage.class);
                            intent.putExtra("IDuser", tempUser);
                            finish();
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                            login=true;
                        }
                    }
                    if(!login){
                        Toast.makeText(getApplicationContext(), "Unabled to Login, Wrong email/password", Toast.LENGTH_SHORT).show();
                    }
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
}