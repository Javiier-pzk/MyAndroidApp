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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout reconfirmPassword;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText reconfirmPasswordEditText;
    private Button register;
    //boolean variable as a temp fix to a firebase bug where authStateListener is fired twice
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.textInputLayoutEmail);
        password = findViewById(R.id.textInputLayoutPassword);
        reconfirmPassword = findViewById(R.id.textInputLayoutReconfirmPassword);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        reconfirmPasswordEditText = findViewById(R.id.editTextReconfirmPassword);
        register = findViewById(R.id.register);

        validateEmail();
        validatePassword();
        validateReconfirmPassword();

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
       if (!isValidEmail(enteredEmail) || !isValidPassword(enteredPassword) ||
               !isValidReconfirmPassword(enteredPassword, enteredReconfirmPassword)) {
           Utils.showShortToast(this, "One of your inputs are invalid!");
           return;
       }
        //create user if the above 3 checks pass
        Auth.createNewUser(enteredEmail, enteredPassword, this);
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
                } else if (!passwordValidationRegex(s).matches()) {
                    password.setError("Password must be at least 8 - 20 characters long, "
                            + "contain no spaces and have at least "
                            + "1 upper case letter, 1 digit and 1 special character");
                } else {
                    password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //A text listener to see check if user entered reconfirm password is valid
    private void validateReconfirmPassword() {
        reconfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredReconfirmPassword = s.toString();
                String enteredPassword = password.getEditText().getText().toString().trim();
                if (s.length() <= 0) {
                    reconfirmPassword.setError("Please reconfirm your password");
                } else if (!enteredPassword.equals(enteredReconfirmPassword)) {
                    reconfirmPassword.setError("Your passwords do not match");
                } else {
                    reconfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Method to check password complexity requirements
    private Matcher passwordValidationRegex(CharSequence s) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(s);
        return matcher;
    }

    private boolean isValidPassword(String enteredPassword) {
        return !enteredPassword.isEmpty() && passwordValidationRegex(enteredPassword).matches();

    }

    private boolean isValidEmail(String enteredEmail) {
       return !enteredEmail.isEmpty();
    }

    private boolean isValidReconfirmPassword(String enteredPassword, String enteredReconfirmPassword) {
        return !enteredReconfirmPassword.isEmpty() && enteredPassword.equals(enteredReconfirmPassword);

    }

}
