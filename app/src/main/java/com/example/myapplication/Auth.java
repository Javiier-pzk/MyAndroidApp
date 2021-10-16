package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


/**
 * Represents a util class for firebase auth operations
 */
public class Auth {

    private static final String R_TAG = "Register Activity";
    private static final String L_TAG = "Login Activity";
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseUser user;
    private static boolean isUserLoggedIn = false;

    public static void createNewUser(String email, String password, Context context) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendUserVerificationEmail();
                        Utils.showShortToast(context, "Success! User created!");
                    } else {
                        onErrorCreateUser(task, context);
                    }
                });
    }

    public static void loginUser(String email, String password, Context context) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utils.showShortToast(context, "Logged in successfully!");
                    } else {
                        onErrorLoginUser(task, context);
                    }
                });
    }

    public static void signOut(Context context) {
        auth.signOut();
        Utils.showShortToast(context, "Logged out successfully!");
    }

    private static void sendUserVerificationEmail() {
        user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification();
        }
    }

    private static void onErrorCreateUser(Task<AuthResult> task, Context context) {
        try {
            if (task.getException() != null) {
                throw task.getException();
            }
        } catch (FirebaseAuthWeakPasswordException | FirebaseAuthUserCollisionException e) {
            Utils.showLongToast(context, e.getMessage());
        } catch (FirebaseAuthInvalidCredentialsException e) {
            Utils.showLongToast(context, e.getMessage());
        } catch (Exception e) {
            Log.e(R_TAG, e.getMessage());
        }
    }

    private static void onErrorLoginUser(Task<AuthResult> task, Context context) {
        try {
            if (task.getException() != null) {
                throw task.getException();
            }
        } catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException e) {
            Utils.showLongToast(context, e.getMessage());
        } catch (Exception e) {
            Log.e(L_TAG, e.getMessage());
        }
    }
}

