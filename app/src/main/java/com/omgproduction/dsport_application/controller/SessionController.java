package com.omgproduction.dsport_application.controller;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.exceptions.UserNotFoundException;

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
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    public void registerUser(String username, String firstname, String lastname, String email, String password, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.REGISTER)
                .param(Keys.USERNAME, username)
                .param(Keys.FIRSTNAME, firstname)
                .param(Keys.LASTNAME, lastname)
                .param(Keys.EMAIL, email)
                .param(Keys.PASSWORD, password)
                .responseListener(responseListener)
                .errorListener(errorListener);
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());

    }

    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    public void loginUser(final Context context, String username, String password, final Response.Listener<JSONObject> responseListener, final Response.ErrorListener errorListener) {

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.LOGIN)
                .param(Keys.USERNAME, username)
                .param(Keys.PASSWORD, password)
                .errorListener(errorListener)
                .responseListener(responseListener);

        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }

    public boolean saveLocalUser(Context context, JSONObject user){
        try {
            Preferences preferencesController = Preferences.getInstance(context)
                    .putBoolean(Keys.IS_LOGIN, true)
                    .putString(Keys.USERID, user.getString(Keys.USERID))
                    .putString(Keys.USERNAME, user.getString(Keys.USERNAME))
                    .putString(Keys.EMAIL, user.getString(Keys.EMAIL))
                    .putString(Keys.FIRSTNAME, user.getString(Keys.FIRSTNAME))
                    .putString(Keys.LASTNAME,  user.getString(Keys.LASTNAME))
                    .putString(Keys.CREATED, user.getString(Keys.CREATED))
                    .putString(Keys.AGB_VERSION, user.getString(Keys.AGB_VERSION))
                    .putString(Keys.PICTURE, user.getString(Keys.PICTURE));

            // commit changes
            preferencesController.commit();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  false;
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
    public void logoutUser(Context context) {
        Preferences.getInstance(context)
                .clear()
                .commit();

    }
}
