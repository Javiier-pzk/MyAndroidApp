package com.example.myapplication.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.util.Auth;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout reconfirmPassword;
    private Button register;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.textInputLayoutEmail);
        password = findViewById(R.id.textInputLayoutPassword);
        reconfirmPassword = findViewById(R.id.textInputLayoutReconfirmPassword);
        register = findViewById(R.id.register);

        register.setOnClickListener(view -> {
                //can ignore potential null pointer exception as we have an edit field.
                String enteredEmail = email.getEditText().getText().toString().trim();
                String enteredPassword = password.getEditText().getText().toString().trim();
                String enteredReconfirmPassword = reconfirmPassword.getEditText().getText().toString().trim();
                validateAndRegister(enteredEmail, enteredPassword, enteredReconfirmPassword);
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

    private void validateAndRegister(String enteredEmail, String enteredPassword,
                                     String enteredReconfirmPassword) {
       if (!isValidEmail(enteredEmail) | !isValidPassword(enteredPassword) |
               !isValidReconfirmPassword(enteredPassword, enteredReconfirmPassword)) {
           return;
       }
        //create user if the above 3 checks pass
        Auth.createNewUser(enteredEmail, enteredPassword, this);
    }

    private boolean isValidPassword(String enteredPassword) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(enteredPassword);
        if (enteredPassword.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else if (!matcher.matches()) {
            password.setError("Password must be at least 8 - 20 characters long, "
                    + "contain no spaces and have at least "
                    + "1 upper case letter, 1 digit and 1 special character");
            return false;
        } else {
            password.setError(null);
            return true;
        }
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

    private boolean isValidReconfirmPassword(String enteredPassword, String enteredReconfirmPassword) {
        if (enteredReconfirmPassword.isEmpty()) {
            reconfirmPassword.setError("Reconfirm password cannot be empty");
            return false;
        } else if (!enteredPassword.equals(enteredReconfirmPassword)) {
            reconfirmPassword.setError("Your passwords do not match!");
            return false;
        } else {
            reconfirmPassword.setError(null);
            return true;
        }
    }

}
