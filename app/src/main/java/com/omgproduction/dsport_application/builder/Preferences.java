package com.omgproduction.dsport_application.builder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Florian on 21.10.2016.
 */
public class Preferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;
    public static final String PREF_NAME = "dsport";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_CREATED = "created";
    public static final String KEY_AGBVERSION = "agbversion";
    public static final String KEY_PICTURE = "picture";

    private Preferences(){};

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public static Preferences getInstance(Context context){
        return new Preferences(context);
    }

    public Preferences putString(String key, String value){
        editor.putString(key,value);
        return this;
    }

    public Preferences putBoolean(String key, Boolean value){
        editor.putBoolean(key,value);
        return this;
    }

    public Preferences putInt(String key, int value){
        editor.putInt(key,value);
        return this;
    }

    public Preferences putFloat(String key, float value){
        editor.putFloat(key,value);
        return this;
    }

    public Preferences putLong(String key, long value){
        editor.putLong(key,value);
        return this;
    }

    public Preferences clear(){
        editor.clear();
        return this;
    }

    public void commit(){
        editor.commit();
    }

    public String getStringDetail(String key, String defaultValue){
        return sharedPreferences.getString(key,defaultValue);
    }

    public int getIntDetail(String key, int defaultValue){
        return sharedPreferences.getInt(key,defaultValue);
    }

    public float getFloatDetail(String key, float defaultValue){
        return sharedPreferences.getFloat(key,defaultValue);
    }

    public long getLongDetail(String key, long defaultValue){
        return sharedPreferences.getLong(key,defaultValue);
    }

    public boolean getBooleanDetail(String key, boolean defaultValue){
        return sharedPreferences.getBoolean(key,defaultValue);
    }

}
