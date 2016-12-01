package com.omgproduction.dsport_application.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.BackendConfig;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.utils.ConnectionUtils;
import com.omgproduction.dsport_application.utils.Converter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Florian on 21.10.2016.
 */
public class PostController {
    private static PostController instance;
    private PostController () {}
    public static synchronized PostController getInstance () {
        if (PostController.instance == null) {
            PostController.instance = new PostController ();
        } return PostController.instance;
    }

    public void getAllPosts(final Context context, final String localUserID, final OnResultListener<ArrayList<Post>> listener){
        listener.onStart();
        JSONRequest request = new JSONRequest(BackendConfig.GET_POSTS)
                .param(ApplicationKeys.USER_ID,localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinish();
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONArray jsonPosts = ConnectionUtils.extractJSONArray(jsonObject, ApplicationKeys.POSTS);
                            ArrayList<Post> posts = new ArrayList<>();
                                try {
                                    for(int i = 0; i< jsonPosts.length(); i++){
                                            posts.add(Converter.convertPost(jsonPosts.getJSONObject(i)));
                                    }
                                    listener.onSuccess(posts);
                                } catch (JSONException e) {
                                    listener.onJSONException(e);
                                }
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

    }

    public void getPinboard(final Context context, final String localUserID, final String ownerUserID, final OnResultListener<ArrayList<Post>> listener){
        listener.onStart();
        JSONRequest request = new JSONRequest(BackendConfig.GET_POSTS)
                .param(ApplicationKeys.USER_ID,localUserID)
                .param(ApplicationKeys.OWNER_ID,ownerUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinish();
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONArray jsonPosts = ConnectionUtils.extractJSONArray(jsonObject, ApplicationKeys.POSTS);
                            ArrayList<Post> posts = new ArrayList<>();
                            try {
                                for(int i = 0; i< jsonPosts.length(); i++){
                                    posts.add(Converter.convertPost(jsonPosts.getJSONObject(i)));
                                }
                                listener.onSuccess(posts);
                            } catch (JSONException e) {
                                listener.onJSONException(e);
                            }
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

    }

    public void createPost(final String localUserID, final String pinboardOwnerID, final String picture, final String text, final String title, final OnResultListener<Void> listener) {
        listener.onStart();
        Log.e("SEND",localUserID);
        Log.e("SEND",pinboardOwnerID);
        Log.e("SEND",picture);
        Log.e("SEND",text);
        Log.e("SEND",title);
        JSONRequest request = new JSONRequest(BackendConfig.CREATE_POST)
                .param(ApplicationKeys.USER_ID, localUserID)
                .param(ApplicationKeys.OWNER_ID, pinboardOwnerID)
                .param(ApplicationKeys.POST_PICTURE, picture)
                .param(ApplicationKeys.TEXT, text)
                .param(ApplicationKeys.TITLE, title)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinish();
                        if(ConnectionUtils.Success(jsonObject)){
                            listener.onSuccess(null);
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
    }
}
