package com.omgproduction.dsport_application.services;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.Routes;
import com.omgproduction.dsport_application.controller.App;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.utils.ConverterFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 21.10.2016.
 */
public class SessionService extends AbstractService<SessionService>{


    public SessionService(Context context) {
        super(context);
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
    public void registerUser(String username, String firstname, String lastname, String email, String password, final IRequestFuture<String> onResultListener) {

        BackendRequest requestBuilder = new BackendRequest(Routes.ROUTE_REGISTER)
                .param(ApplicationKeys.APPLICATION_USER_USERNAME, username)
                .param(ApplicationKeys.APPLICATION_USER_FIRSTNAME, firstname)
                .param(ApplicationKeys.APPLICATION_USER_LASTNAME, lastname)
                .param(ApplicationKeys.APPLICATION_USER_EMAIL, email)
                .param(ApplicationKeys.APPLICATION_USER_PASSWORD, password)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ResultWrapper.isOk(jsonObject)){
                            onResultListener.onFinishQuery();
                            try {
                                onResultListener.onSuccess(ResultWrapper.extractValue(jsonObject).getString(ApplicationKeys.APPLICATION_USER_USERNAME));
                            } catch (JSONException e) {
                                onResultListener.onJSONException(e);
                            }
                        }else{
                            onResultListener.onFinishQuery();
                            onResultListener.onFailure(ResultWrapper.extractErrorCode(jsonObject));
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinishQuery();
                        onResultListener.onConnectionError(volleyError);
                    }
                });
        App.getInstance().addToRequestQueue(requestBuilder.build());

    }

    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param onResultListener Listener to do something after recieve response
     */
    public void loginUser(String username, String password, final IRequestFuture<JSONObject> onResultListener) {
        final String token = Preferences.getInstance(App.getInstance().getApplicationContext())
                .getStringDetail(ApplicationKeys.TOKEN,"");

        //Log.e("TOKEN","LOAD-LOCAL: "+token);

        onResultListener.onStartQuery();
        BackendRequest requestBuilder = new BackendRequest(Routes.ROUTE_LOGIN)
                .param(ApplicationKeys.APPLICATION_USER_USERNAME, username)
                .param(ApplicationKeys.APPLICATION_USER_PASSWORD, password)
                .param(ApplicationKeys.TOKEN, token)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinishQuery();
                        onResultListener.onConnectionError(volleyError);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ResultWrapper.isOk(jsonObject)){
                            onResultListener.onFinishQuery();
                            onResultListener.onSuccess(ResultWrapper.extractValue(jsonObject));
                        }else{
                            onResultListener.onFinishQuery();
                            onResultListener.onFailure(ResultWrapper.extractErrorCode(jsonObject));
                        }
                    }
                });

        App.getInstance().addToRequestQueue(requestBuilder.build());
    }

    public void saveLocalUser(JSONObject user, IRequestFuture<User> onResultListener){
        onResultListener.onStartQuery();
        try {
            Preferences preferencesController = Preferences.getInstance(context)
                    .putBoolean(ApplicationKeys.APPLICATION_IS_LOGIN, true)
                    .putString(ApplicationKeys.APPLICATION_USER_USER_ID, user.getString(ApplicationKeys.APPLICATION_USER_USER_ID))
                    .putString(ApplicationKeys.APPLICATION_USER_USERNAME, user.getString(ApplicationKeys.APPLICATION_USER_USERNAME))
                    .putString(ApplicationKeys.APPLICATION_USER_EMAIL, user.getString(ApplicationKeys.APPLICATION_USER_EMAIL))
                    .putString(ApplicationKeys.APPLICATION_USER_FIRSTNAME, user.getString(ApplicationKeys.APPLICATION_USER_FIRSTNAME))
                    .putString(ApplicationKeys.APPLICATION_USER_LASTNAME,  user.getString(ApplicationKeys.APPLICATION_USER_LASTNAME))
                    .putString(ApplicationKeys.APPLICATION_USER_CREATED, user.getString(ApplicationKeys.APPLICATION_USER_CREATED))
                    .putString(ApplicationKeys.APPLICATION_USER_AGBVERSION, user.getString(ApplicationKeys.APPLICATION_USER_AGBVERSION))
                    .putString(ApplicationKeys.APPLICATION_USER_PICTURE, user.getString(ApplicationKeys.APPLICATION_USER_PICTURE));

            // commit changes
            preferencesController.commit();
            onResultListener.onFinishQuery();
            onResultListener.onSuccess(ConverterFactory.convertUser(user));
        } catch (JSONException e) {
            onResultListener.onFinishQuery();
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
        Preferences.getInstance(App.getInstance().getApplicationContext())
                .putString(ApplicationKeys.TOKEN,token).commit();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        return Preferences.getInstance(context).getBooleanDetail(ApplicationKeys.APPLICATION_IS_LOGIN,false);

    }

    /**
     * Clear session details
     */
    public void logout() {

        discardToken(new RequestFuture<Void>(){
            @Override
            public void onSuccess(Void result) {

                Preferences.getInstance(context)
                        .putBoolean(ApplicationKeys.APPLICATION_IS_LOGIN, false)
                        .putString(ApplicationKeys.APPLICATION_USER_USER_ID, "")
                        .putString(ApplicationKeys.APPLICATION_USER_USERNAME, "")
                        .putString(ApplicationKeys.APPLICATION_USER_EMAIL, "")
                        .putString(ApplicationKeys.APPLICATION_USER_FIRSTNAME, "")
                        .putString(ApplicationKeys.APPLICATION_USER_LASTNAME,  "")
                        .putString(ApplicationKeys.APPLICATION_USER_CREATED, "")
                        .putString(ApplicationKeys.APPLICATION_USER_AGBVERSION, "")
                        .putString(ApplicationKeys.APPLICATION_USER_PICTURE, "")
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

    public void discardToken(final IRequestFuture<Void> onResultListener){
        final String token = Preferences.getInstance(App.getInstance().getApplicationContext())
                .getStringDetail(ApplicationKeys.TOKEN,"");
        //Log.e("TOKEN","DISCARD: "+token);

        onResultListener.onStartQuery();
        BackendRequest requestBuilder = new BackendRequest(Routes.ROUTE_DISCARD_TOKEN)
                .param(ApplicationKeys.TOKEN, token)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onResultListener.onFinishQuery();
                        onResultListener.onConnectionError(volleyError);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ResultWrapper.isOk(jsonObject)){
                            onResultListener.onFinishQuery();
                            onResultListener.onSuccess(null);
                        }else{
                            onResultListener.onFinishQuery();
                            onResultListener.onFailure(ResultWrapper.extractErrorCode(jsonObject));
                        }
                    }
                });

        App.getInstance().addToRequestQueue(requestBuilder.build());
    }
}
