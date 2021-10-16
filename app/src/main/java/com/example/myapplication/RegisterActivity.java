package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText reconfirmPassword;
    private Button register;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        reconfirmPassword = findViewById(R.id.reconfirmPassword);
        register = findViewById(R.id.register);
        auth = new Auth();

        register.setOnClickListener(view -> {
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();
                String enteredReconfirmPassword = reconfirmPassword.getText().toString();
                validateAndRegister(enteredEmail, enteredPassword, enteredReconfirmPassword);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
    }

    private void validateAndRegister(String enteredEmail, String enteredPassword, String enteredReconfirmPassword) {
        if (TextUtils.isEmpty(enteredEmail) ||
            TextUtils.isEmpty(enteredPassword) ||
            TextUtils.isEmpty(enteredReconfirmPassword)) {
            Utils.showToast(this, "Email or password is empty!");
        } else if (enteredPassword.length() < 6) {
            Utils.showToast(this, "Password must be more than 6 characters long");
        } else if (!enteredReconfirmPassword.equals(enteredPassword)) {
            Utils.showToast(this, "Your passwords do not match");
        } else {
            auth.createNewUser(enteredEmail, enteredPassword, this);
        }
    }
}
