package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;
import java.util.Map;

public class PreferencesService {

    private static final int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "dsport";
    public static final String PREF_USER = "dsport_user";
    public static final String DEFAULT = "UNKNOWN";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public static String getSharedPreferencesUser(Context context) {
        return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE).getString(PREF_USER, DEFAULT);
    }

    public static Map<String, String> getToken(final Context context) {
        return new HashMap<String, String>() {{
            put("jwt",getSharedPreferences(context).getString("jwt", "UNKNOWN"));
        }};
    }

    public static String getUserId(final Context context) {
        return getSharedPreferences(context).getString("id", "UNKNOWN");

    }
}