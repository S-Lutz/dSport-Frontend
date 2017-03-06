package com.omgproduction.dsport_application.utils;

import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class ConverterFactory implements ApplicationKeys{

    public static Converter<JSONObject, User> createJsonToUserConverter(){
        return new Converter<JSONObject, User>() {
            @Override
            public User convert(JSONObject input){
                try {
                    return new User(
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_EMAIL),
                            input.getString(APPLICATION_USER_PICTURE),
                            input.getString(APPLICATION_USER_FIRSTNAME),
                            input.getString(APPLICATION_USER_LASTNAME),
                            input.getString(APPLICATION_USER_CREATED),
                            input.getString(APPLICATION_USER_AGBVERSION)
                    );
                } catch (JSONException e) {
                    return null;
                }
            }
        };
    }

    public static Converter<JSONObject, Post> createJsonToPostConverter(){
        return new Converter<JSONObject, Post>() {
            @Override
            public Post convert(JSONObject input) {
                try {
                    return new Post(
                            input.getString(APPLICATION_POST_POST_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_POST_TITLE),
                            input.getString(APPLICATION_USER_PICTURE),
                            input.getString(APPLICATION_POST_PICTURE),
                            input.getString(APPLICATION_POST_TEXT),
                            input.getString(APPLICATION_POST_CREATED),
                            input.getString(APPLICATION_POST_LIKECOUNT),
                            input.getString(APPLICATION_POST_COMMENTCOUNT),
                            input.getString(APPLICATION_POST_SHARECOUNT),
                            Integer.parseInt(input.getString(APPLICATION_POST_LIKED))==1
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Converter<JSONObject, Comment> createJsonToCommentConverter(){
        return new Converter<JSONObject, Comment>() {
            @Override
            public Comment convert(JSONObject input) {
                try {
                    return new Comment(
                            input.getString(APPLICATION_COMMENT_ID),
                            input.getString(APPLICATION_USER_USERNAME),
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_COMMENT_PICTURE),
                            input.getString(APPLICATION_COMMENT_TEXT),
                            input.getString(APPLICATION_COMMENT_CREATED),
                            input.getString(APPLICATION_COMMENT_LIKECOUNT),
                            Integer.parseInt(input.getString(APPLICATION_COMMENT_LIKED))==1
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Converter<JSONObject, LikeResult> createJsonToPostLikeResultConverter(){
        return new Converter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_POST_LIKED))==1,
                            input.getString(APPLICATION_POST_LIKECOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Converter<JSONObject, LikeResult> createJsonToCommentLikeResultConverter(){
        return new Converter<JSONObject, LikeResult>() {
            @Override
            public LikeResult convert(JSONObject input) {
                try {
                    return new LikeResult(
                            Integer.parseInt(input.getString(APPLICATION_COMMENT_LIKED))==1,
                            input.getString(APPLICATION_COMMENT_LIKECOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Converter<JSONObject, Like> createJsonToLikeConverter(){
        return new Converter<JSONObject, Like>() {
            @Override
            public Like convert(JSONObject input) {
                try {
                    return new Like(
                            input.getString(APPLICATION_USER_USER_ID),
                            input.getString(APPLICATION_USER_USERNAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}
