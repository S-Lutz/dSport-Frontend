package com.omgproduction.dsport_application.aaRefactored.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.aaRefactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.aaRefactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.aaRefactored.fragments.FriendsFragment;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.PostNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.models.protocols.RegistrationProtocol;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.UserResultNode;
import com.omgproduction.dsport_application.models.backendModels.RegistrationNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class UserService {

    public static final String USER_REF = "dsport_user", TOKEN_REF = "jwt";

    public boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferencesService.getSharedPreferences(context);
        return preferences.contains(USER_REF)
                && preferences.contains(TOKEN_REF);
    }

    public void login(final Context context, final UserNode user, final BackendCallback<UserNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateLoginRoute(), UserNode.class, user, new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {

                String jsonUser = new Gson().toJson(result);
                PreferencesService.getSharedPreferences(context).edit()
                        .putString(USER_REF, jsonUser)
                        .putString(TOKEN_REF, responseHeader.get(TOKEN_REF))
                        .apply();
                callback.onSuccess(result, responseHeader);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                callback.onFailure(error);
            }
        });
    }

    public void register(final Context context, RegistrationProtocol registrationProtocol, BackendCallback<RegistrationNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateRegisterRoute(), RegistrationNode.class, registrationProtocol, callback);
    }


    public void update(final Context context, final UserNode userNode, final BackendCallback<UserNode> callback) {
        BackendRequestExecutor.executePutRequest(RouteGenerator.generateUpdateUserRoute(), UserNode.class, userNode, PreferencesService.getToken(context), new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                String jsonUpdatedUser = new Gson().toJson(result);
                PreferencesService.getSharedPreferences(context).edit()
                        .putString(USER_REF, jsonUpdatedUser)
                        .apply();
                callback.onSuccess(result, responseHeader);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                callback.onFailure(error);
            }
        });
    }

    public void uploadPicture(final Context context, final String url, final File imgFile, final UserNode userNode, final BackendCallback<UserNode> callback) {
        BackendRequestExecutor.executePutPictureRequest(url, UserNode.class, imgFile, userNode, PreferencesService.getToken(context), new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                String jsonUpdatedUser = new Gson().toJson(result);
                PreferencesService.getSharedPreferences(context).edit()
                        .putString(USER_REF, jsonUpdatedUser)
                        .apply();
                callback.onSuccess(result, responseHeader);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                callback.onFailure(error);
            }
        });
    }

    public void findUsers(Context context, Object body, BackendCallback<ArrayList<UserResultNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateSearchUsersRoute(), UserResultNode.class, body, PreferencesService.getToken(context), callback);
    }

    public void findEvents(Context context, Object body, BackendCallback<ArrayList<EventNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateSearchEventsRoute(), EventNode.class, body, PreferencesService.getToken(context), callback);
    }

    public void findPosts(Context context, Object body, BackendCallback<ArrayList<PostNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateSearchPostsRoute(), PostNode.class, body, PreferencesService.getToken(context), callback);
    }

    public void requestFriendship(Context context, UserResultNode userResultNode, BackendCallback<String> callback) {
        BackendRequestExecutor.executeStringRequest(RouteGenerator.generateRequestFriendshipRoute(userResultNode.getId()), PreferencesService.getToken(context), callback);
    }

    public void acceptFriendship(Context context, UserResultNode userResultNode, BackendCallback<String> callback) {
        BackendRequestExecutor.executeStringRequest(RouteGenerator.generateAcceptFriendshipRoute(userResultNode.getId()), PreferencesService.getToken(context), callback);
    }

    public void deleteFriendship(Context context, UserResultNode userResultNode, BackendCallback<String> callback) {
        BackendRequestExecutor.executeStringRequest(RouteGenerator.generateDeleteFriendshipRoute(userResultNode.getId()), PreferencesService.getToken(context), callback);
    }

    public void declineFriendship(Context context, UserResultNode userResultNode, BackendCallback<String> callback) {
        BackendRequestExecutor.executeStringRequest(RouteGenerator.generateDeclineFriendshipRoute(userResultNode.getId()), PreferencesService.getToken(context), callback);
    }

    public void findRequests(Context context, BackendCallback<ArrayList<UserResultNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetRequestRoute(), UserResultNode.class, null, PreferencesService.getToken(context), callback);
    }

    public void findFriends(Context context, BackendCallback<ArrayList<UserResultNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetFriendsRoute(), UserResultNode.class, null, PreferencesService.getToken(context), callback);
    }
}
