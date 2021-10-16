package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = new Auth();

        login.setOnClickListener(view -> {
            String enteredEmail = email.getText().toString();
            String enteredPassword = password.getText().toString();
            validateAndLogin(enteredEmail, enteredPassword);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void validateAndLogin(String enteredEmail, String enteredPassword) {
        if (TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)) {
            Utils.showToast(this, "Email or password is empty!");
        } else if (enteredPassword.length() < 6) {
            Utils.showToast(this, "Password must be more than 6 characters long");
        } else {
            auth.loginUser(enteredEmail, enteredPassword, this);
        }
    }
}
