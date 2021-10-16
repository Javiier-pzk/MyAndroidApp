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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private EditText email;
    private EditText password;
    private EditText reconfirmPassword;
    private Button register;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        reconfirmPassword = findViewById(R.id.reconfirmPassword);
        register = findViewById(R.id.register);

        register.setOnClickListener(view -> {
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();
                String enteredReconfirmPassword = reconfirmPassword.getText().toString();
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
        if (TextUtils.isEmpty(enteredEmail) ||
            TextUtils.isEmpty(enteredPassword) ||
            TextUtils.isEmpty(enteredReconfirmPassword)) {
            Utils.showShortToast(this, "Email or password is empty!");
            return;
        }

        if (!isValidPassword(enteredPassword)) {
            Utils.showLongToast(this,
                    "Password must contain 8 - 20 characters and have at least "
                            + "1 upper case letter, 1 digit and 1 special character");
            return;
        }

        if (!enteredReconfirmPassword.equals(enteredPassword)) {
            Utils.showShortToast(this, "Your passwords do not match");
            return;
        }

        //create user if the above 3 checks pass
        Auth.createNewUser(enteredEmail, enteredPassword, this);
    }

    private boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
