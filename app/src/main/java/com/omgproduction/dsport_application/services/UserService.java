package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.models.backendModels.UserNode;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.utils.Triple;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Florian on 07.11.2016.
 */

public class UserService extends AbstractService {

    private UserNode localUser;
    public static final String UNKNOWN_VALUE = "unknown";
    public static final Long UNKNOWN_LONG_VALUE = -1L;

    public UserService(Context context) {
        super(context);
    }


    public void saveUser(final User user, final IRequestFuture<User> listener) {

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


    public void saveLocalUser(UserNode user, String token) {

        String jsonUser = new Gson().toJson(user);
        new Preferences(context)
                .putString("USER", jsonUser)
                .putString("TOKEN", token)
                .commit();
        localUser = user;

       // Log.e("LOGIN", user.getUsername() + " " + user.getId());
       // new Preferences(context)
       //         .putBoolean(APPLICATION_IS_LOGIN, true)
       //         .putLong(APPLICATION_USER_USER_ID, user.getId())
       //         .putString(APPLICATION_USER_USERNAME, user.getUsername())
       //         .putString(APPLICATION_USER_EMAIL, user.getEmail())
       //         .putString(APPLICATION_USER_FIRSTNAME, user.getFirstname())
       //         .putString(APPLICATION_USER_LASTNAME, user.getLastname())
       //         .putString(APPLICATION_USER_CREATED, user.getCreated())
       //         .putString(APPLICATION_USER_UPDATED, user.getUpdated())
       //         .putString(APPLICATION_USER_AGBVERSION, user.getAgbVersion())
       //         .putString(APPLICATION_USER_PICTURE, user.getPicture())
       //         .putString(APPLICATION_USER_JWT_TOKEN, token)
       //         .commit();
       // localUser = new User(user.getId(), user.getUsername(), user.getEmail(), user.getPicture(), user.getFirstname(), user.getLastname(), user.getCreated(), user.getUsername(), user.getAgbVersion());
    }

    public void synchronizeLocalUser(String userID, final IRequestFuture<User> listener) {

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

    public UserNode getUser() {
        Preferences preferences = new Preferences(context);
        if (isLoggedIn()) {
            return new Gson().fromJson(preferences.getStringDetail("USER", null), UserNode.class);
        }
        return null;
    }


    public User getLocalUser() {

        Preferences preferences = new Preferences(context);
        if (isLoggedIn()) {

              return new User(
                       preferences.getLongDetail(APPLICATION_USER_USER_ID, UNKNOWN_LONG_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_USERNAME, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_EMAIL, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_PICTURE, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_FIRSTNAME, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_LASTNAME, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_CREATED, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_UPDATED, UNKNOWN_VALUE),
                       preferences.getStringDetail(APPLICATION_USER_AGBVERSION, UNKNOWN_VALUE)
               );
        }

        return null;
    }

    public boolean isLoggedIn(){
        Preferences preferences = new Preferences(context);
        return preferences.isAvailable("USER") && preferences.isAvailable("TOKEN");
    }

    public void logout(){
        Preferences preferences = new Preferences(context);
        preferences.clear().commit();
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

    public boolean isAvailable(User user) {
        return user != null && !user.getId().equals(UNKNOWN_LONG_VALUE) && !user.getId().trim().isEmpty();
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
                            if (response.isOk()) {

                                List<SearchUser> friends = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS);
                                List<SearchUser> received = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS_RECEIVED);
                                List<SearchUser> sended = response.getArray(ConverterFactory.createJsonToSearchUserConverter(), APPLICATION_FRIENDS_SENDED);


                                listener.onSuccess(new Triple(friends, received, sended));
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
                            if (response.isOk()) {
                                listener.onSuccess(null);
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
                            if (response.isOk()) {
                                listener.onSuccess(null);
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
                            if (response.isOk()) {
                                listener.onSuccess(null);
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
                            if (response.isOk()) {
                                listener.onSuccess(null);
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
}
