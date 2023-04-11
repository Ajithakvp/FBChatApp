package com.example.fbchatapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class PreferenceManger {

    public static void setLoggedStatus(Activity activity, boolean isLogged) {
        SharedPreferences preferences;
        preferences = activity.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        preferences.edit().putBoolean("IS_LOGGED_STATUS", isLogged).apply();
    }

    public static boolean isLogged(Activity activity) {
        SharedPreferences preferences;
        preferences = activity.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        return preferences.getBoolean("IS_LOGGED_STATUS", false);


    }
}
