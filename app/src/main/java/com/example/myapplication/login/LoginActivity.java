package com.example.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.example.myapplication.util.Auth;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button login;
    //boolean variable as a temp fix to a firebase bug where authStateListener is fired twice
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.textInputLayoutEmail);
        password = findViewById(R.id.textInputLayoutPassword);
        login = findViewById(R.id.login);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        validateEmail();
        validatePassword();

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
       if (!isEmailNonEmpty(enteredEmail) || !isPasswordNonEmpty(enteredPassword)) {
           Utils.showShortToast(this, "Please fill in the empty fields!");
           return;
        }

        //Log user in if there are no empty fields
        Auth.loginUser(enteredEmail, enteredPassword, this);
    }

    //A text listener to see check if user entered email is valid
    private void validateEmail() {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    email.setError("Email cannot be empty");
                } else {
                    email.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //A text listener to see check if user entered password is valid
    private void validatePassword() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    password.setError("Password cannot be empty");
                } else if (s.length() > 20) {
                    password.setError("Password should only be at most 20 characters long");
                } else {
                    password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isEmailNonEmpty(String enteredEmail) {
        return !enteredEmail.isEmpty();
    }

    private boolean isPasswordNonEmpty(String enteredPassword) {
        return !enteredPassword.isEmpty();
    }
}
