package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.utils.ConverterFactory;

import org.json.JSONObject;

/**
 * Created by Florian on 07.11.2016.
 */

public class UserService extends AbstractService<UserService> {

    private User localUser;
    private static final String unknownValue = "unknown";

    public UserService(Context context) {
        super(context);
    }


    public void saveUser(final User user , final IRequestFuture<User> listener){

        listener.onStartQuery();

        if(isAvailable(user)){

            BackendRequest request = new BackendRequest(ROUTE_EDIT_USER)
                    .errorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            listener.onFinishQuery();
                            listener.onFailure(SOMETHING_WENT_WRONG);
                        }
                    })
                    .responseListener(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            ResultWrapper result = new ResultWrapper(context,jsonObject);

                            if(result.isOk()){
                                User user = result.extractValue(ConverterFactory.createJsonToUserConverter());

                                if(user == null){
                                    listener.onFinishQuery();
                                    listener.onFailure(SOMETHING_WENT_WRONG);
                                }else {
                                    saveLocalUser(user);
                                    listener.onFinishQuery();
                                    listener.onSuccess(user);
                                }
                            }else{
                                listener.onFailure(result.extractErrorCode());
                            }
                        }
                    })
                    .param(APPLICATION_USER_USERNAME, user.getUsername())
                    .param(APPLICATION_USER_FIRSTNAME, user.getFirstname())
                    .param(APPLICATION_USER_LASTNAME, user.getLastname())
                    .param(APPLICATION_USER_EMAIL, user.getEmail())
                    .param(APPLICATION_USER_PASSWORD, user.getPassword())
                    .param(APPLICATION_USER_USER_ID, user.getId())
                    .param(APPLICATION_USER_PICTURE, user.getPicture());

            executeRequest(request.build());
        }else{
            listener.onFinishQuery();
            listener.onUserNotFound();
        }
    }



    public void saveLocalUser(User user){
            Preferences preferences = new Preferences(context);
            preferences
                .putBoolean(ApplicationKeys.APPLICATION_IS_LOGIN, true)
                .putString(ApplicationKeys.APPLICATION_USER_USER_ID, user.getId())
                .putString(ApplicationKeys.APPLICATION_USER_USERNAME, user.getUsername())
                .putString(ApplicationKeys.APPLICATION_USER_EMAIL, user.getEmail())
                .putString(ApplicationKeys.APPLICATION_USER_FIRSTNAME, user.getFirstname())
                .putString(ApplicationKeys.APPLICATION_USER_LASTNAME,  user.getLastname())
                .putString(ApplicationKeys.APPLICATION_USER_CREATED, user.getCreated())
                .putString(ApplicationKeys.APPLICATION_USER_AGBVERSION, user.getAgbversion())
                .putString(ApplicationKeys.APPLICATION_USER_PICTURE, user.getPicture())
                .commit();
    }

    //public void getGlobalUser(final IRequestFuture<User> listener){
//
    //    listener.onStartQuery();
//
    //    final Preferences preferences = new Preferences(context);
    //    String userID = preferences
    //            .getStringDetail(ApplicationKeys.APPLICATION_USER_USER_ID,"");
//
    //    if(!userID.trim().isEmpty()){
    //        BackendRequest request = new BackendRequest(Routes.ROUTE_GET_USER)
    //                .param(ApplicationKeys.APPLICATION_USER_USER_ID,userID)
    //                .responseListener(new Response.Listener<JSONObject>() {
    //                    @Override
    //                    public void onResponse(JSONObject jsonObject) {
    //                        listener.onFinishQuery();
    //                        if(ResultWrapper.isOk(jsonObject)){
    //                            JSONObject jsonUser = ResultWrapper.extractValue(jsonObject);
    //                            User user = null;
    //                            try {
    //                                user = ConverterFactory.convertUser(jsonUser);
    //                                SessionService.getInstance().saveLocalUser(context,jsonUser,listener);
    //                            } catch (JSONException e) {
    //                                listener.onJSONException(e);
    //                            }
    //                            listener.onSuccess(user);
    //                        }else{
    //                            listener.onFailure(ResultWrapper.extractErrorCode(jsonObject));
    //                        }
//
    //                    }
    //                })
    //                .errorListener(new Response.ErrorListener() {
    //                    @Override
    //                    public void onErrorResponse(VolleyError volleyError) {
    //                        listener.onFinishQuery();
    //                        listener.onConnectionError(volleyError);
    //                    }
    //                });
//
    //        App.getInstance().executeRequest(request.build());
    //    }else{
    //        listener.onFinishQuery();
    //        listener.onUserNotFound();
    //    }
//
    //}

    public User getLocalUser(){
        if(!isAvailable(localUser)){
            final Preferences preferences = new Preferences(context);
            localUser = new User(
                    preferences.getStringDetail(APPLICATION_USER_USER_ID,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_USERNAME,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_EMAIL,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_PICTURE,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_FIRSTNAME,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_LASTNAME,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_CREATED,unknownValue),
                    preferences.getStringDetail(APPLICATION_USER_AGBVERSION,unknownValue)
            );
        }

        return localUser;
    }

    public boolean isAvailable(User user){
        return user == null || user.getId().equals(unknownValue);
    }
}
