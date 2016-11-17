package com.omgproduction.dsport_application.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
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

    public void getAllPosts(final Context context, final String localUserID, final String ownerUserID, final OnResultListener<ArrayList<Post>> listener){
        listener.onStart();
        JSONRequest request = new JSONRequest(BackendFunctions.GET_POSTS)
                .param(Keys.USER_ID,localUserID)
                .param(Keys.OWNER_ID,ownerUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinish();
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONArray jsonPosts = ConnectionUtils.extractJSONArray(jsonObject,Keys.POSTS);
                            ArrayList<Post> posts = new ArrayList<>();
                            Log.e("Response",String.valueOf(jsonPosts.length()));
                                try {
                                    for(int i = 0; i< jsonPosts.length(); i++){
                                            posts.add(Converter.convertPost(jsonPosts.getJSONObject(i)));
                                    }
                                    Log.e("Response",String.valueOf(posts.size()));
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
}
