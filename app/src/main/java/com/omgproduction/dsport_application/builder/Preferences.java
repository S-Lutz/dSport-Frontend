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

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
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

    public boolean isAvailable(String key){
        return !getStringDetail(key, "UNKNOWN").equals("UNKNOWN");
    }

}
