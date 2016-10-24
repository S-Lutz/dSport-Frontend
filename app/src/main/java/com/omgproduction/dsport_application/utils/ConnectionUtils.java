package com.omgproduction.dsport_application.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 21.10.2016.
 */
public class ConnectionUtils {

    private ConnectionUtils(){};

    public static boolean Success(JSONObject jsonObject){
        try {
            return jsonObject.getString("error").equals("OK");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject extractJSONValue(JSONObject jsonObject){
        try {
            return jsonObject.getJSONObject("value");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractErrorCode(JSONObject jsonObject){
        try {
            return jsonObject.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractErrorString(Context context, JSONObject jsonObject){
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(extractErrorCode(jsonObject), "string", packageName);
        return context.getString(resId);
    }

}
