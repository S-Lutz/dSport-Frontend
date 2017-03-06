package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.utils.ConverterFactory;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Florian on 21.10.2016.
 */
public class PostService extends AbstractService{

    public PostService(Context context) {
        super(context);
    }

    public void getAllPosts(final String localUserID, final IRequestFuture<List<Post>> listener){
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_GET_POSTS)
                .param(APPLICATION_USER_USER_ID,localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){
                            List<Post> posts = result.extractArray(ConverterFactory.createJsonToPostConverter(), APPLICATION_POSTS);
                            if(posts == null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(posts);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());

    }

    public void getPinboard(final String localUserID, final String ownerUserID, final IRequestFuture<List<Post>> listener){
        listener.onStartQuery();
        //TODO GET PINBOARD
        BackendRequest request = new BackendRequest(ROUTE_GET_PINBOARD)
                .param(APPLICATION_USER_USER_ID,localUserID)
                .param(APPLICATION_POST_OWNER_ID,ownerUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){
                            List<Post> posts = result.extractArray(ConverterFactory.createJsonToPostConverter(), APPLICATION_POSTS);
                            if(posts==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(posts);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());

    }

    public void createPost(final String localUserID, final String pinboardOwnerID, final String picture, final String text, final String title, final IRequestFuture<Void> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_CREATE_POST)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .param(APPLICATION_POST_OWNER_ID, pinboardOwnerID)
                .param(APPLICATION_POST_PICTURE, picture)
                .param(APPLICATION_POST_TEXT, text)
                .param(APPLICATION_POST_TITLE, title)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        ResultWrapper result = new ResultWrapper(context,  jsonObject);

                        listener.onFinishQuery();
                        if(result.isOk()){
                            listener.onSuccess(null);
                        }else{
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void getAllComments(final String postID, final IRequestFuture<List<Comment>> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_GET_COMMENTS)
                .param(APPLICATION_POST_POST_ID,postID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){

                            List<Comment> comments = result.extractArray(ConverterFactory.createJsonToCommentConverter(), APPLICATION_COMMENTS);
                            if(comments==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(comments);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void likePost(final String localUserID, final String post_id, final IRequestFuture<LikeResult> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_LIKE_POST)
                .param(APPLICATION_POST_POST_ID,post_id)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){
                            LikeResult likeResult = result.extractValue(ConverterFactory.createJsonToPostLikeResultConverter());

                            if(likeResult==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(likeResult);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void likeComment(final String localUserID, final String comment_id, final IRequestFuture<LikeResult> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_LIKE_COMMENT)
                .param(APPLICATION_COMMENT_ID,comment_id)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();

                        ResultWrapper result = new ResultWrapper(context,jsonObject);

                        if(result.isOk()){
                            LikeResult likeResult = result.extractValue(ConverterFactory.createJsonToCommentLikeResultConverter());
                            if(likeResult==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else{
                                listener.onFinishQuery();
                                listener.onSuccess(likeResult);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void getAllLikes(final String postID, final IRequestFuture<List<Like>> listener) {
        listener.onStartQuery();
        final BackendRequest request = new BackendRequest(ROUTE_GET_LIKES)
                .param(APPLICATION_POST_POST_ID,postID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){

                            List<Like> likes = result.extractArray(ConverterFactory.createJsonToLikeConverter(), APPLICATION_LIKES);

                            if(likes == null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else{
                                listener.onFinishQuery();
                                listener.onSuccess(likes);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void createComment(final String localUserID, final String post_id, final String picture, final String text, final IRequestFuture<Void> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_COMMENT_POST)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .param(APPLICATION_POST_POST_ID, post_id)
                .param(APPLICATION_COMMENT_PICTURE, picture)
                .param(APPLICATION_COMMENT_TEXT, text)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        listener.onFinishQuery();
                        if(result.isOk()){
                            listener.onSuccess(null);
                        }else{
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void getPostDetail(final String localUserID, final String post_id, final IRequestFuture<Post> listener) {
        listener.onStartQuery();
        final BackendRequest request = new BackendRequest(ROUTE_GET_POST_DETAIL)
                .param(APPLICATION_POST_POST_ID, post_id)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();
                        ResultWrapper result = new ResultWrapper(context, jsonObject);
                        if(result.isOk()){

                            Post post = result.extractValue(ConverterFactory.createJsonToPostConverter());
                            listener.onFinishQuery();
                            if(post==null){
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onSuccess(post);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }
}
