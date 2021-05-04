package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import model.User;
import model.UserArray;

public class Register extends AppCompatActivity {

    private TextInputLayout signup_textInput_email, signup_textInput_name, signup_textInput_pass;
    private Button signup_button_Signup, signup_button_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

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
                    Intent intent = new Intent(getBaseContext(), Homepage.class);
                    User user = new User(email, nama, password);
                    for (int i = 0; i < UserArray.akunuser.size(); i++){
                        if(user.getEmail().equalsIgnoreCase(UserArray.akunuser.get(i).getEmail())){
                            Toast.makeText(getBaseContext(), "Email is already Registered!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    intent.putExtra("IDuser", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    UserArray.akunuser.add(user);
                    Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_SHORT).show();
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
}