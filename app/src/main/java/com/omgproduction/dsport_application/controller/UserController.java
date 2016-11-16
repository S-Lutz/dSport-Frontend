package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ConnectionUtils;
import com.omgproduction.dsport_application.utils.Converter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 07.11.2016.
 */

public class UserController {
    private static UserController instance;
    private UserController () {}
    public static synchronized UserController getInstance () {
        if (UserController.instance == null) {
            UserController.instance = new UserController ();
        }
        return UserController.instance;
    }

    public void saveUserDetail(final Context context, final String key, final String value, final OnResultListener<String> listener){

        listener.onStart();
        String userID = Preferences.getInstance(context)
                .getStringDetail(Keys.USERID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(BackendFunctions.EDIT_USER)
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onResult();
                            listener.onConnectionError(volleyError);
                        }
                    })
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if(ConnectionUtils.Success(jsonObject)){
                                listener.onResult();
                                try {
                                    String value = ConnectionUtils.extractJSONValue(jsonObject).getString(key);
                                    update(context,key,value);
                                    listener.onSuccess(value);
                                } catch (JSONException e) {
                                    listener.onJSONError(e);
                                }
                            }else{
                                listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject),ConnectionUtils.extractErrorString(context,jsonObject));
                            }
                        }
                    })
                    .param(Keys.USERID, userID)
                    .param(key,value);
            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onResult();
            listener.onUserNotFound();
        }

    }

    public void getUser(final Context context, final OnResultListener<User> listener){

        listener.onStart();

        String userID = Preferences.getInstance(context)
                .getStringDetail(Keys.USERID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(BackendFunctions.GET_USER)
                    .param(Keys.USERID,userID)
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listener.onResult();
                            if(ConnectionUtils.Success(jsonObject)){
                                JSONObject jsonUser = ConnectionUtils.extractJSONValue(jsonObject);
                                User user = Converter.convertUser(jsonUser);
                                listener.onSuccess(user);
                            }else{
                                listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject),ConnectionUtils.extractErrorString(context,jsonObject));
                            }

                        }
                    })
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onResult();
                            listener.onConnectionError(volleyError);
                        }
                    });

            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onResult();
            listener.onUserNotFound();
        }

    }

    private void update(Context context, String key, String value){
        Preferences.getInstance(context)
                .putString(key,value)
                .commit();
    }
}
