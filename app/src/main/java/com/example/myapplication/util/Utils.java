package com.example.myapplication.util;

import android.content.Context;
import android.widget.Toast;

/**
 * This class represents common Util methods for the app
 */
public class Utils {

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
