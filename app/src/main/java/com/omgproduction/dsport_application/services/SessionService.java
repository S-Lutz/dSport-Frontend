package com.omgproduction.dsport_application.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.utils.ConverterFactory;

import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by Florian on 21.10.2016.
 */
public class SessionService extends AbstractService{


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
     * @param listener Listener to do something after recieve response
     */
    public void registerUser(String username, String firstname, final String lastname, String email, String password, final IRequestFuture<String> listener) {

        try {
            JSONRequest request = new JSONRequest(ROUTE_REGISTER)
                    .addParam(APPLICATION_USER_USERNAME, username)
                    .addParam(APPLICATION_USER_FIRSTNAME, firstname)
                    .addParam(APPLICATION_USER_LASTNAME, lastname)
                    .addParam(APPLICATION_USER_EMAIL, email)
                    .addParam(APPLICATION_USER_PASSWORD, password)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getString(APPLICATION_USER_USERNAME));
                            }else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }

        //final BackendRequest requestBuilder = new BackendRequest(ROUTE_REGISTER)
        //        .param(APPLICATION_USER_USERNAME, username)
        //        .param(APPLICATION_USER_FIRSTNAME, firstname)
        //        .param(APPLICATION_USER_LASTNAME, lastname)
        //        .param(APPLICATION_USER_EMAIL, email)
        //        .param(APPLICATION_USER_PASSWORD, password)
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
//
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                if(result.isOk()){
//
        //                    String username = result.extractString(APPLICATION_USER_USERNAME);
//
//
        //                    onResultListener.onFinishQuery();
        //                    if(username == null){
        //                        onResultListener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        onResultListener.onSuccess(username);
        //                    }
        //                }else{
        //                    onResultListener.onFinishQuery();
        //                    onResultListener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        })
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                onResultListener.onFinishQuery();
        //                onResultListener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        });
        //executeRequest(requestBuilder.build());

    }

    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param onResultListener Listener to do something after recieve response
     */
    public void validateUser(String username, String password, final IRequestFuture<User> onResultListener) {
        onResultListener.onStartQuery();

        final String token =
                new Preferences(context).getStringDetail(APPLICATION_TOKEN,"");


        try {
            JSONRequest request = new JSONRequest(ROUTE_LOGIN)
                    .addParam(APPLICATION_USER_USERNAME, username)
                    .addParam(APPLICATION_USER_PASSWORD, password)
                    .addParam(APPLICATION_TOKEN, token)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            onResultListener.onFinishQuery();
                            if(response.isOk()){
                                onResultListener.onSuccess(response.getValue(ConverterFactory.createJsonToUserConverter()));
                            }else {
                                onResultListener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onResultListener.onFinishQuery();
        }


        //final BackendRequest requestBuilder = new BackendRequest(ROUTE_LOGIN)
        //        .param(APPLICATION_USER_USERNAME, username)
        //        .param(APPLICATION_USER_PASSWORD, password)
        //        .param(APPLICATION_TOKEN, token)
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                onResultListener.onFinishQuery();
        //                onResultListener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        })
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
//
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                if(result.isOk()){
        //                    User user = result.extractValue(ConverterFactory.createJsonToUserConverter());
//
        //                    onResultListener.onFinishQuery();
        //                    if(user == null){
        //                        onResultListener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        onResultListener.onSuccess(user);
        //                    }
        //                }else{
        //                    onResultListener.onFinishQuery();
        //                    onResultListener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        });
//
        //executeRequest(requestBuilder.build());
    }

    public void saveLocalToken(final String token) {
        Preferences preferences = new Preferences(context);
        preferences.putString(APPLICATION_TOKEN, token).commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        return
                new Preferences(context).getBooleanDetail(APPLICATION_IS_LOGIN,false);

    }

    /**
     * Clear session details
     */
    public void logout() {

        Log.e("LOGOUT", "LOGOUT");

        discardToken(new RequestFuture<Void>());

        new Preferences(context)
                .putBoolean(APPLICATION_IS_LOGIN, false)
                .putString(APPLICATION_USER_USER_ID, "")
                .putString(APPLICATION_USER_USERNAME, "")
                .putString(APPLICATION_USER_EMAIL, "")
                .putString(APPLICATION_USER_FIRSTNAME, "")
                .putString(APPLICATION_USER_LASTNAME,  "")
                .putString(APPLICATION_USER_CREATED, "")
                .putString(APPLICATION_USER_AGBVERSION, "")
                .putString(APPLICATION_USER_PICTURE, "")
                .commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(i);
    }

    public void discardToken(final IRequestFuture<Void> onResultListener){

        final String token = new Preferences(context).getStringDetail(APPLICATION_TOKEN,"");

        onResultListener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_DISCARD_TOKEN)
                    .addParam(APPLICATION_TOKEN, token)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            onResultListener.onFinishQuery();
                            if(response.isOk()){
                                onResultListener.onSuccess(null);
                            }else {
                                onResultListener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onResultListener.onFinishQuery();
        }

        //BackendRequest requestBuilder = new BackendRequest(ROUTE_DISCARD_TOKEN)
        //        .param(APPLICATION_TOKEN, token)
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                onResultListener.onFinishQuery();
        //                onResultListener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        })
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
//
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                onResultListener.onFinishQuery();
        //                if(result.isOk()){
        //                    onResultListener.onSuccess(null);
        //                }else{
        //                    onResultListener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        });
//
        //executeRequest(requestBuilder.build());
    }
}
