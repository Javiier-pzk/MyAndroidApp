package com.example.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.util.Auth;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private TextInputLayout email;
    private TextInputLayout password;
    private Button login;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.textInputLayoutEmail);
        password = findViewById(R.id.textInputLayoutPassword);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            //can ignore potential null pointer exception as we have an edit field.
            String enteredEmail = email.getEditText().getText().toString().trim();
            String enteredPassword = password.getEditText().getText().toString().trim();
            validateAndLogin(enteredEmail, enteredPassword);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null && flag) {
            //A temp solution to a firebase bug where the authStateListener is fired twice
            flag = false;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void validateAndLogin(String enteredEmail, String enteredPassword) {
       if (!isValidEmail(enteredEmail) | !isValidPassword(enteredPassword)) {
            return;
        }

        //Log user in if there are no empty fields
        Auth.loginUser(enteredEmail, enteredPassword, this);
    }

    private boolean isValidEmail(String enteredEmail) {
        if (enteredEmail.isEmpty()) {
            email.setError("Email cannot be empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean isValidPassword(String enteredPassword) {
        if (enteredPassword.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
}
