package com.k002422.tochkatest.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static final String LOGGED_IN_MODE = "loginMode";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("tochkatest", Context.MODE_PRIVATE);
    }

    public static void setLoggedInMode(Context context, String mode) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LOGGED_IN_MODE, mode).apply();
    }

    public static String getLoggedInMode(Context context) {
        return getSharedPreferences(context).getString(LOGGED_IN_MODE, "");
    }
}
