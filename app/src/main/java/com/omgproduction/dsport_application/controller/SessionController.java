package com.omgproduction.dsport_application.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.BackendConfig;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ConnectionUtils;
import com.omgproduction.dsport_application.utils.Converter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 21.10.2016.
 */
public class SessionController {
    private static SessionController instance;
    private SessionController () {}
    public static synchronized SessionController getInstance () {
        if (SessionController.instance == null) {
            SessionController.instance = new SessionController ();
        }
        return SessionController.instance;
    }


    /**
     * Register user with the Backend System
     * @param username Users username
     * @param firstname Users real firstname
     * @param lastname Users real lastname
     * @param email Users email-address
     * @param password Users Password
     * @param onResultListener Listener to do something after recieve response
     */
    public void registerUser(String username, String firstname, String lastname, String email, String password, final OnResultListener<String> onResultListener) {

        JSONRequest requestBuilder = new JSONRequest(BackendConfig.REGISTER)
                .param(ApplicationKeys.USERNAME, username)
                .param(ApplicationKeys.FIRSTNAME, firstname)
                .param(ApplicationKeys.LASTNAME, lastname)
                .param(ApplicationKeys.EMAIL, email)
                .param(ApplicationKeys.PASSWORD, password)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            onResultListener.onFinish();
                            try {
                                onResultListener.onSuccess(ConnectionUtils.extractJSONValue(jsonObject).getString(ApplicationKeys.USERNAME));
                            } catch (JSONException e) {
                                onResultListener.onJSONException(e);
                            }
                        }else{
                            onResultListener.onFinish();
                            onResultListener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinish();
                        onResultListener.onConnectionError(volleyError);
                    }
                });
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());

    }

    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param onResultListener Listener to do something after recieve response
     */
    public void loginUser(final Context context, String username, String password, final OnResultListener<JSONObject> onResultListener) {
        final String token = Preferences.getInstance(ApplicationController.getInstance().getApplicationContext())
                .getStringDetail(ApplicationKeys.TOKEN,"");

        //Log.e("TOKEN","LOAD-LOCAL: "+token);

        onResultListener.onStart();
        JSONRequest requestBuilder = new JSONRequest(BackendConfig.LOGIN)
                .param(ApplicationKeys.USERNAME, username)
                .param(ApplicationKeys.PASSWORD, password)
                .param(ApplicationKeys.TOKEN, token)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinish();
                        onResultListener.onConnectionError(volleyError);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            onResultListener.onFinish();
                            onResultListener.onSuccess(ConnectionUtils.extractJSONValue(jsonObject));
                        }else{
                            onResultListener.onFinish();
                            onResultListener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                        }
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }

    public void saveLocalUser(Context context, JSONObject user, OnResultListener<User> onResultListener){
        onResultListener.onStart();
        try {
            Preferences preferencesController = Preferences.getInstance(context)
                    .putBoolean(ApplicationKeys.IS_LOGIN, true)
                    .putString(ApplicationKeys.USER_ID, user.getString(ApplicationKeys.USER_ID))
                    .putString(ApplicationKeys.USERNAME, user.getString(ApplicationKeys.USERNAME))
                    .putString(ApplicationKeys.EMAIL, user.getString(ApplicationKeys.EMAIL))
                    .putString(ApplicationKeys.FIRSTNAME, user.getString(ApplicationKeys.FIRSTNAME))
                    .putString(ApplicationKeys.LASTNAME,  user.getString(ApplicationKeys.LASTNAME))
                    .putString(ApplicationKeys.CREATED, user.getString(ApplicationKeys.CREATED))
                    .putString(ApplicationKeys.AGB_VERSION, user.getString(ApplicationKeys.AGB_VERSION))
                    .putString(ApplicationKeys.PICTURE, user.getString(ApplicationKeys.PICTURE));

            // commit changes
            preferencesController.commit();
            onResultListener.onFinish();
            onResultListener.onSuccess(Converter.convertUser(user));
        } catch (JSONException e) {
            onResultListener.onFinish();
            onResultListener.onJSONException(e);
        }
    }

    public void saveLocalToken(final String token) {
        //Send Token to the Server and Save it into User Table...
        //We will need it to send Notifications to the Client

        //Tutorial Videos:
        //https://www.youtube.com/watch?v=LiKCEa5_Cs8
        //https://www.youtube.com/watch?v=MYZVhs6T_W8
        //Log.e("TOKEN","SAVE: "+token);
        Preferences.getInstance(ApplicationController.getInstance().getApplicationContext())
                .putString(ApplicationKeys.TOKEN,token).commit();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin(Context context) {
        return Preferences.getInstance(context).getBooleanDetail(ApplicationKeys.IS_LOGIN,false);

    }

    /**
     * Clear session details
     */
    public void logout(final Context context) {

        discardToken(new OnResultAdapter<Void>(){
            @Override
            public void onSuccess(Void result) {

                Preferences.getInstance(context)
                        .putBoolean(ApplicationKeys.IS_LOGIN, false)
                        .putString(ApplicationKeys.USER_ID, "")
                        .putString(ApplicationKeys.USERNAME, "")
                        .putString(ApplicationKeys.EMAIL, "")
                        .putString(ApplicationKeys.FIRSTNAME, "")
                        .putString(ApplicationKeys.LASTNAME,  "")
                        .putString(ApplicationKeys.CREATED, "")
                        .putString(ApplicationKeys.AGB_VERSION, "")
                        .putString(ApplicationKeys.PICTURE, "")
                        .commit();

                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(i);
            }
        });

    }

    public void discardToken(final OnResultListener<Void> onResultListener){
        final String token = Preferences.getInstance(ApplicationController.getInstance().getApplicationContext())
                .getStringDetail(ApplicationKeys.TOKEN,"");
        //Log.e("TOKEN","DISCARD: "+token);

        onResultListener.onStart();
        JSONRequest requestBuilder = new JSONRequest(BackendConfig.DISCARD_TOKEN)
                .param(ApplicationKeys.TOKEN, token)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinish();
                        onResultListener.onConnectionError(volleyError);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            onResultListener.onFinish();
                            onResultListener.onSuccess(null);
                        }else{
                            onResultListener.onFinish();
                            onResultListener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                        }
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }
}
