package com.omgproduction.dsport_application.controller;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.activities.LoginActivity;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;
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

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.REGISTER)
                .param(Keys.USERNAME, username)
                .param(Keys.FIRSTNAME, firstname)
                .param(Keys.LASTNAME, lastname)
                .param(Keys.EMAIL, email)
                .param(Keys.PASSWORD, password)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            onResultListener.onFinish();
                            try {
                                onResultListener.onSuccess(ConnectionUtils.extractJSONValue(jsonObject).getString(Keys.USERNAME));
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
        onResultListener.onStart();
        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.LOGIN)
                .param(Keys.USERNAME, username)
                .param(Keys.PASSWORD, password)
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
                    .putBoolean(Keys.IS_LOGIN, true)
                    .putString(Keys.USER_ID, user.getString(Keys.USER_ID))
                    .putString(Keys.USERNAME, user.getString(Keys.USERNAME))
                    .putString(Keys.EMAIL, user.getString(Keys.EMAIL))
                    .putString(Keys.FIRSTNAME, user.getString(Keys.FIRSTNAME))
                    .putString(Keys.LASTNAME,  user.getString(Keys.LASTNAME))
                    .putString(Keys.CREATED, user.getString(Keys.CREATED))
                    .putString(Keys.AGB_VERSION, user.getString(Keys.AGB_VERSION))
                    .putString(Keys.PICTURE, user.getString(Keys.PICTURE));

            // commit changes
            preferencesController.commit();
            onResultListener.onFinish();
            onResultListener.onSuccess(Converter.convertUser(user));
        } catch (JSONException e) {
            onResultListener.onFinish();
            onResultListener.onJSONException(e);
        }
    }

    public void registerToken(String token) {
        //Send Token to the Server and Save it into User Table...
        //We will need it to send Notifications to the Client

        //Tutorial Videos:
        //https://www.youtube.com/watch?v=LiKCEa5_Cs8
        //https://www.youtube.com/watch?v=MYZVhs6T_W8

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin(Context context) {
        return Preferences.getInstance(context).getBooleanDetail(Keys.IS_LOGIN,false);

    }

    /**
     * Clear session details
     */
    public void logout(Context context) {
        Preferences.getInstance(context)
                .clear()
                .commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(i);

    }
}
