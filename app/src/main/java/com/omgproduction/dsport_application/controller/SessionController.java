package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendURL;
import com.omgproduction.dsport_application.utils.StringUtils;

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

        JSONRequest requestBuilder = new JSONRequest(BackendURL.REGISTER)
                .param("username", username)
                .param("firstname", firstname)
                .param("lastname", lastname)
                .param("email", email)
                .param("password", StringUtils.hash(password))
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

        JSONRequest requestBuilder = new JSONRequest(BackendURL.LOGIN)
                .param("username", username)
                .param("password", StringUtils.hash(password))
                .errorListener(errorListener)
                .responseListener(responseListener);

        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }

    public boolean startLocalSession(Context context, JSONObject user){
        try {
            Preferences preferencesController = Preferences.getInstance(context)
                    .putBoolean(Preferences.IS_LOGIN, true)
                    .putString(Preferences.KEY_USERID, user.getString("user_id"))
                    .putString(Preferences.KEY_USERNAME, user.getString("username"))
                    .putString(Preferences.KEY_EMAIL, user.getString("email"))
                    .putString(Preferences.KEY_FIRSTNAME, user.getString("firstname"))
                    .putString(Preferences.KEY_LASTNAME,  user.getString("lastname"))
                    .putString(Preferences.KEY_CREATED, user.getString("created"))
                    .putString(Preferences.KEY_AGBVERSION, user.getString("agbversion"))
                    .putString(Preferences.KEY_PICTURE, user.getString("picture"));

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
        return Preferences.getInstance(context).getBooleanDetail(Preferences.IS_LOGIN,false);

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
