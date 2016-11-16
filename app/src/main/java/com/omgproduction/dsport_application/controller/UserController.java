package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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
                            listener.onFinish();
                            listener.onConnectionError(volleyError);
                        }
                    })
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if(ConnectionUtils.Success(jsonObject)){
                                listener.onFinish();
                                try {
                                    String value = ConnectionUtils.extractJSONValue(jsonObject).getString(key);
                                    update(context,key,value);
                                    listener.onSuccess(value);
                                } catch (JSONException e) {
                                    listener.onJSONException(e);
                                }
                            }else{
                                listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                            }
                        }
                    })
                    .param(Keys.USERID, userID)
                    .param(key,value);
            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onFinish();
            listener.onUserNotFound();
        }

    }

    public void getGlobalUser(final Context context, final OnResultListener<User> listener){

        listener.onStart();

        String userID = Preferences.getInstance(context)
                .getStringDetail(Keys.USERID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(BackendFunctions.GET_USER)
                    .param(Keys.USERID,userID)
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listener.onFinish();
                            if(ConnectionUtils.Success(jsonObject)){
                                JSONObject jsonUser = ConnectionUtils.extractJSONValue(jsonObject);
                                User user = Converter.convertUser(jsonUser);
                                listener.onSuccess(user);
                            }else{
                                listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                            }

                        }
                    })
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onFinish();
                            listener.onConnectionError(volleyError);
                        }
                    });

            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onFinish();
            listener.onUserNotFound();
        }

    }

    public void getLocalUser(final Context context, final OnResultListener<User> listener){

        listener.onStart();
        String userID = Preferences.getInstance(context).getStringDetail(Keys.USERID,"");
        if(!userID.trim().isEmpty()){
            User user = new User(
                    Preferences.getInstance(context).getStringDetail(Keys.USERID,""),
                    Preferences.getInstance(context).getStringDetail(Keys.USERNAME,""),
                    Preferences.getInstance(context).getStringDetail(Keys.EMAIL,""),
                    Preferences.getInstance(context).getStringDetail(Keys.PICTURE,""),
                    Preferences.getInstance(context).getStringDetail(Keys.FIRSTNAME,""),
                    Preferences.getInstance(context).getStringDetail(Keys.LASTNAME,""),
                    Preferences.getInstance(context).getStringDetail(Keys.CREATED,""),
                    Preferences.getInstance(context).getStringDetail(Keys.AGB_VERSION,"")
            );
            listener.onFinish();
            listener.onSuccess(user);
        }else{
            listener.onFinish();
            listener.onUserNotFound();
        }

    }

    private void update(Context context, String key, String value){
        Preferences.getInstance(context)
                .putString(key,value)
                .commit();
    }
}
