package com.example.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.util.Auth;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.util.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private EditText email;
    private EditText password;
    private Button login;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            String enteredEmail = email.getText().toString();
            String enteredPassword = password.getText().toString();
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
        if (TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)) {
            Utils.showShortToast(this, "Email or password is empty!");
            return;
        }

        //Log user in if there are no empty fields
        Auth.loginUser(enteredEmail, enteredPassword, this);
    }
}
