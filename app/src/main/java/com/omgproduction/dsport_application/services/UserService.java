package com.omgproduction.dsport_application.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.Triple;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Florian on 07.11.2016.
 */

public class UserService extends AbstractService {

    private User localUser;
    public static final String UNKNOWN_VALUE = "unknown";

    public UserService(Context context) {
        super(context);
    }


    public void saveUser(final User user , final IRequestFuture<User> listener){

        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EDIT_USER)
                    .addParam(APPLICATION_USER_USERNAME, user.getUsername())
                    .addParam(APPLICATION_USER_FIRSTNAME, user.getFirstname())
                    .addParam(APPLICATION_USER_LASTNAME, user.getLastname())
                    .addParam(APPLICATION_USER_EMAIL, user.getEmail())
                    .addParam(APPLICATION_USER_PASSWORD, user.getPassword())
                    .addParam(APPLICATION_USER_USER_ID, user.getId())
                    .addParam(APPLICATION_USER_PICTURE, user.getPicture())
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToUserConverter()));
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


        //BackendRequest request = new BackendRequest(ROUTE_EDIT_USER)
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        })
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
//
        //                ResultWrapper result = new ResultWrapper(context,jsonObject);
//
        //                if(result.isOk()){
        //                    User user = result.extractValue(ConverterFactory.createJsonToUserConverter());
//
        //                    if(user == null){
        //                        listener.onFinishQuery();
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        saveLocalUser(user);
        //                        listener.onFinishQuery();
        //                        listener.onSuccess(user);
        //                    }
        //                }else{
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        })
        //        .param(APPLICATION_USER_USERNAME, user.getUsername())
        //        .param(APPLICATION_USER_FIRSTNAME, user.getFirstname())
        //        .param(APPLICATION_USER_LASTNAME, user.getLastname())
        //        .param(APPLICATION_USER_EMAIL, user.getEmail())
        //        .param(APPLICATION_USER_PASSWORD, user.getPassword())
        //        .param(APPLICATION_USER_USER_ID, user.getId())
        //        .param(APPLICATION_USER_PICTURE, user.getPicture());
//
        //executeRequest(request.build());
    }



    public void saveLocalUser(User user){
        Log.e("LOGIN", user.getUsername() + " " + user.getId());
            new Preferences(context)
                .putBoolean(APPLICATION_IS_LOGIN, true)
                .putString(APPLICATION_USER_USER_ID, user.getId())
                .putString(APPLICATION_USER_USERNAME, user.getUsername())
                .putString(APPLICATION_USER_EMAIL, user.getEmail())
                .putString(APPLICATION_USER_FIRSTNAME, user.getFirstname())
                .putString(APPLICATION_USER_LASTNAME,  user.getLastname())
                .putString(APPLICATION_USER_CREATED, user.getCreated())
                .putString(APPLICATION_USER_AGBVERSION, user.getAgbversion())
                .putString(APPLICATION_USER_PICTURE, user.getPicture())
                .commit();
        localUser = user;
    }

    public void synchronizeLocalUser(String userID, final IRequestFuture<User> listener){

        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_USER)
                    .addParam(APPLICATION_USER_USER_ID,userID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToUserConverter()));
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

        //final BackendRequest request = new BackendRequest(ROUTE_GET_USER)
        //        .param(APPLICATION_USER_USER_ID,userID)
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
        //                listener.onFinishQuery();
//
        //                ResultWrapper result = new ResultWrapper(context,jsonObject);
//
        //                if(result.isOk()){
        //                    User user = result.extractValue(ConverterFactory.createJsonToUserConverter());
        //                    if(user == null){
        //                        listener.onFinishQuery();
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        saveLocalUser(user);
        //                        listener.onFinishQuery();
        //                        listener.onSuccess(user);
        //                    }
        //                }else{
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
//
        //            }
        //        })
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        });
//
        //executeRequest(request.build());

    }

    public User getLocalUser(){
        if(!isAvailable(localUser)){
            Preferences preferences = new Preferences(context);
            localUser = new User(
                    preferences.getStringDetail(APPLICATION_USER_USER_ID, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_USERNAME, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_EMAIL, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_PICTURE, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_FIRSTNAME, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_LASTNAME, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_CREATED, UNKNOWN_VALUE),
                    preferences.getStringDetail(APPLICATION_USER_AGBVERSION, UNKNOWN_VALUE)
            );
        }

        return localUser;
    }



    public void getUser(String userID, final IRequestFuture<User> listener) {

        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_USER)
                    .addParam(APPLICATION_USER_USER_ID, userID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if (response.isOk()) {
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToUserConverter()));
                            } else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public boolean isAvailable(User user){
        return user != null && !user.getId().equals(UNKNOWN_VALUE) && !user.getId().trim().isEmpty();
    }

    public void getAllFriends(String localUserId, final RequestFuture<Triple<List<SearchUser>, List<SearchUser>, List<SearchUser>>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_FRIENDS_GET_ALL)
                    .addParam(APPLICATION_USER_USER_ID, localUserId)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){

                                List<SearchUser> friends = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS);
                                List<SearchUser> received = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS_RECEIVED);
                                List<SearchUser> sended = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS_SENDED);


                                listener.onSuccess(new Triple(friends,received, sended));
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


        //final BackendRequest request = new BackendRequest(ROUTE_FRIENDS_GET_ALL)
        //        .param(APPLICATION_USER_USER_ID, localUserId)
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        }).responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
        //                if(result.isOk()){
//
        //                    List<SearchUser> friends = result.extractArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS);
//
//
        //                    listener.onFinishQuery();
        //                    if(friends==null){
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        listener.onSuccess(friends);
        //                    }
//
        //                }else {
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        });
//
        //executeRequest(request.build());


    }

    public void declineFriendship(String localUserId, String friendId, final RequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_FRIENDS_DECLINE)
                    .addParam(APPLICATION_USER_USER_ID, localUserId)
                    .addParam(APPLICATION_FRIEND_ID, friendId)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
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
    }

    public void acceptFriendship(String localUserId, String friendId, final RequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_FRIENDS_ACCEPT)
                    .addParam(APPLICATION_USER_USER_ID, localUserId)
                    .addParam(APPLICATION_FRIEND_ID, friendId)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
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
    }

    public void sendFriendshipRequest(String localUserId, String friendId, final RequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_FRIENDS_SEND)
                    .addParam(APPLICATION_USER_USER_ID, localUserId)
                    .addParam(APPLICATION_FRIEND_ID, friendId)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
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
    }

    public void deleteFriendship(String localUserId, String friendId, final RequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_FRIENDS_DELETE)
                    .addParam(APPLICATION_USER_USER_ID, localUserId)
                    .addParam(APPLICATION_FRIEND_ID, friendId)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
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
    }
}
