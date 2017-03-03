package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.Routes;
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

        listener.onStartQuery();
        String userID = Preferences.getInstance(context)
                .getStringDetail(ApplicationKeys.USER_USER_ID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(Routes.EDIT_USER)
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onFinishQuery();
                            listener.onConnectionError(volleyError);
                        }
                    })
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if(ConnectionUtils.Success(jsonObject)){
                                listener.onFinishQuery();
                                try {
                                    String value = "";
                                    if(!key.equals(ApplicationKeys.USER_PASSWORD)){
                                        value = ConnectionUtils.extractJSONValue(jsonObject).getString(key);
                                    }
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
                    .param(ApplicationKeys.USER_USER_ID, userID)
                    .param(key,value);
            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onFinishQuery();
            listener.onUserNotFound();
        }

    }

    public void getGlobalUser(final Context context, final OnResultListener<User> listener){

        listener.onStartQuery();

        String userID = Preferences.getInstance(context)
                .getStringDetail(ApplicationKeys.USER_USER_ID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(Routes.GET_USER)
                    .param(ApplicationKeys.USER_USER_ID,userID)
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listener.onFinishQuery();
                            if(ConnectionUtils.Success(jsonObject)){
                                JSONObject jsonUser = ConnectionUtils.extractJSONValue(jsonObject);
                                User user = null;
                                try {
                                    user = Converter.convertUser(jsonUser);
                                    SessionController.getInstance().saveLocalUser(context,jsonUser,listener);
                                } catch (JSONException e) {
                                    listener.onJSONException(e);
                                }
                                listener.onSuccess(user);
                            }else{
                                listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                            }

                        }
                    })
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onFinishQuery();
                            listener.onConnectionError(volleyError);
                        }
                    });

            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            listener.onFinishQuery();
            listener.onUserNotFound();
        }

    }

    public void getLocalUser(final Context context, final OnResultListener<User> listener){

        listener.onStartQuery();
        String userID = Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_USER_ID,"");
        if(!userID.trim().isEmpty()){
            User user = new User(
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_USER_ID,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_USERNAME,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_EMAIL,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_PICTURE,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_FIRSTNAME,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_LASTNAME,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_CREATED,""),
                    Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_AGBVERSION,"")
            );
            listener.onFinishQuery();
            listener.onSuccess(user);
        }else{
            listener.onFinishQuery();
            listener.onUserNotFound();
        }

    }

    public void getLocalUserID(final Context context, final OnResultListener<String> listener){

        listener.onStartQuery();
        String userID = Preferences.getInstance(context).getStringDetail(ApplicationKeys.USER_USER_ID,"");
        if(!userID.trim().isEmpty()){
            listener.onFinishQuery();
            listener.onSuccess(userID);
        }else{
            listener.onFinishQuery();
            listener.onUserNotFound();
        }

    }

    private void update(Context context, String key, String value){
        Preferences.getInstance(context)
                .putString(key,value)
                .commit();
    }
}
