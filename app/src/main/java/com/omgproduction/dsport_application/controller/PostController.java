package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.BackendConfig;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
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
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.GET_POSTS)
                .param(ApplicationKeys.USER_USER_ID,localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());

    }

    public void getPinboard(final Context context, final String localUserID, final String ownerUserID, final OnResultListener<ArrayList<Post>> listener){
        listener.onStartQuery();
        //TODO GET PINBOARD
        JSONRequest request = new JSONRequest(BackendConfig.GET_POSTS)
                .param(ApplicationKeys.USER_USER_ID,localUserID)
                .param(ApplicationKeys.POST_OWNER_ID,ownerUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());

    }

    public void createPost(final String localUserID, final String pinboardOwnerID, final String picture, final String text, final String title, final OnResultListener<Void> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.CREATE_POST)
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .param(ApplicationKeys.POST_OWNER_ID, pinboardOwnerID)
                .param(ApplicationKeys.POST_PICTURE, picture)
                .param(ApplicationKeys.POST_TEXT, text)
                .param(ApplicationKeys.POST_TITLE, title)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void getAllComments(final Context context, final String postID, final OnResultListener<ArrayList<Comment>> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.GET_COMMENTS)
                .param(ApplicationKeys.POST_POST_ID,postID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONArray jsonPosts = ConnectionUtils.extractJSONArray(jsonObject, ApplicationKeys.COMMENTS);
                            ArrayList<Comment> comments = new ArrayList<>();
                            try {
                                for(int i = 0; i< jsonPosts.length(); i++){
                                    comments.add(Converter.convertComment(jsonPosts.getJSONObject(i)));
                                }
                                listener.onSuccess(comments);
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void likePost(final String localUserID, final String post_id, final OnResultListener<LikeResult> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.LIKE_POST)
                .param(ApplicationKeys.POST_POST_ID,post_id)
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        if(ConnectionUtils.Success(jsonObject)){
                            try {
                                LikeResult likeResult = Converter.convertPostLikeResult(ConnectionUtils.extractJSONValue(jsonObject));
                                listener.onSuccess(likeResult);
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void likeComment(final String localUserID, final String comment_id, final OnResultListener<LikeResult> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.LIKE_COMMENT)
                .param(ApplicationKeys.COMMENT_ID,comment_id)
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        if(ConnectionUtils.Success(jsonObject)){
                            try {
                                LikeResult likeResult = Converter.convertCommentLikeResult(ConnectionUtils.extractJSONValue(jsonObject));
                                listener.onSuccess(likeResult);
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void getAllLikes(final Context context, final String postID, final OnResultListener<ArrayList<Like>> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.GET_LIKES)
                .param(ApplicationKeys.POST_POST_ID,postID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONArray jsonPosts = ConnectionUtils.extractJSONArray(jsonObject, ApplicationKeys.LIKES);
                            ArrayList<Like> likes = new ArrayList<>();
                            try {
                                for(int i = 0; i< jsonPosts.length(); i++){
                                    likes.add(Converter.convertLike(jsonPosts.getJSONObject(i)));
                                }
                                listener.onSuccess(likes);
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void createComment(final String localUserID, final String post_id, final String picture, final String text, final OnResultListener<Void> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.COMMENT_POST)
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .param(ApplicationKeys.POST_POST_ID, post_id)
                .param(ApplicationKeys.COMMENT_PICTURE, picture)
                .param(ApplicationKeys.COMMENT_TEXT, text)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }

    public void getPostDetail(final String localUserID, final String post_id, final OnResultListener<Post> listener) {
        listener.onStartQuery();
        JSONRequest request = new JSONRequest(BackendConfig.GET_POST_DETAIL)
                .param(ApplicationKeys.POST_POST_ID, post_id)
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        if(ConnectionUtils.Success(jsonObject)){
                            try {
                                listener.onSuccess(Converter.convertPost(ConnectionUtils.extractJSONValue(jsonObject)));
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
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(request.build());
    }
}
