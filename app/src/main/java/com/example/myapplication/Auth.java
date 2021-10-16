package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Represents a util class for firebase auth operations
 */
public class Auth {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void createNewUser(String email, String password, Context context) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utils.showToast(context, "Success! User created!");
                    } else {
                        Utils.showToast(context, "Failed to create user!");
                    }
                });
    }

    public void loginUser(String email, String password, Context context) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Utils.showToast(context, "Login Successful!");
        });
    }

    public void signOut(Context context) {
        auth.signOut();
        Utils.showToast(context, "Logged out successfully!");
    }
}

