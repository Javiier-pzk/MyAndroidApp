package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

/**
 * This class represents common Util methods for the app
 */
public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
