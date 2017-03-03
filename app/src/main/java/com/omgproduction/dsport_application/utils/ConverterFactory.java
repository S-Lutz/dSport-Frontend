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

public class ConverterFactory {

    public static Converter<JSONObject, User> createJsonToUserConverter(){
        return new Converter<JSONObject, User>() {
            @Override
            public User convert(JSONObject input){
                try {
                    return new User(
                            input.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                            input.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                            input.getString(ApplicationKeys.APPLICATION_USER_EMAIL),
                            input.getString(ApplicationKeys.APPLICATION_USER_PICTURE),
                            input.getString(ApplicationKeys.APPLICATION_USER_FIRSTNAME),
                            input.getString(ApplicationKeys.APPLICATION_USER_LASTNAME),
                            input.getString(ApplicationKeys.APPLICATION_USER_CREATED),
                            input.getString(ApplicationKeys.APPLICATION_USER_AGBVERSION)
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
                            input.getString(ApplicationKeys.APPLICATION_POST_POST_ID),
                            input.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                            input.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                            input.getString(ApplicationKeys.APPLICATION_POST_TITLE),
                            input.getString(ApplicationKeys.APPLICATION_USER_PICTURE),
                            input.getString(ApplicationKeys.APPLICATION_POST_PICTURE),
                            input.getString(ApplicationKeys.APPLICATION_POST_TEXT),
                            input.getString(ApplicationKeys.APPLICATION_POST_CREATED),
                            input.getString(ApplicationKeys.APPLICATION_POST_LIKECOUNT),
                            input.getString(ApplicationKeys.APPLICATION_POST_COMMENTCOUNT),
                            input.getString(ApplicationKeys.APPLICATION_POST_SHARECOUNT),
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_POST_LIKED))==1
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
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_ID),
                            input.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                            input.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_PICTURE),
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_TEXT),
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_CREATED),
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_LIKECOUNT),
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_COMMENT_LIKED))==1
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
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_POST_LIKED))==1,
                            input.getString(ApplicationKeys.APPLICATION_POST_LIKECOUNT));
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
                            Integer.parseInt(input.getString(ApplicationKeys.APPLICATION_COMMENT_LIKED))==1,
                            input.getString(ApplicationKeys.APPLICATION_COMMENT_LIKECOUNT));
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
                            input.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                            input.getString(ApplicationKeys.APPLICATION_USER_USERNAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }






    public static User convertUser(JSONObject jsonUser) throws JSONException {
        User user = new User(
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_EMAIL),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_PICTURE),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_FIRSTNAME),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_LASTNAME),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_CREATED),
                jsonUser.getString(ApplicationKeys.APPLICATION_USER_AGBVERSION)
        );
        return user;
    }

    public static Post convertPost(JSONObject jsonPost) throws JSONException {
        Post post = new Post(
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_POST_ID),
                jsonPost.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                jsonPost.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_TITLE),
                jsonPost.getString(ApplicationKeys.APPLICATION_USER_PICTURE),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_PICTURE),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_TEXT),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_CREATED),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_LIKECOUNT),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_COMMENTCOUNT),
                jsonPost.getString(ApplicationKeys.APPLICATION_POST_SHARECOUNT),
                Integer.parseInt(jsonPost.getString(ApplicationKeys.APPLICATION_POST_LIKED))==1
        );
        return post;
    }

    public static Post convertPost(String postString) throws JSONException {
        return convertPost(new JSONObject(postString));
    }

    public static Comment convertComment(String commentString) throws JSONException {
        return convertComment(new JSONObject(commentString));
    }

    public static Comment convertComment(JSONObject jsonComment) throws JSONException {
        Comment comment = new Comment(
                jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_ID),
                jsonComment.getString(ApplicationKeys.APPLICATION_USER_USERNAME),
                jsonComment.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_PICTURE),
                jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_TEXT),
                jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_CREATED),
                jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_LIKECOUNT),
                Integer.parseInt(jsonComment.getString(ApplicationKeys.APPLICATION_COMMENT_LIKED))==1
        );
        return comment;
    }

    public static JSONObject convertJSON(Comment comment) throws JSONException {
        JSONObject jsonComment = new JSONObject();
        jsonComment.put(ApplicationKeys.APPLICATION_COMMENT_ID, comment.getComment_id());
        jsonComment.put(ApplicationKeys.APPLICATION_USER_USERNAME, comment.getUsername());
        jsonComment.put(ApplicationKeys.APPLICATION_USER_USER_ID, comment.getUserid());
        jsonComment.put(ApplicationKeys.APPLICATION_COMMENT_PICTURE, comment.getCommentPicture());
        jsonComment.put(ApplicationKeys.APPLICATION_COMMENT_TEXT, comment.getText());
        jsonComment.put(ApplicationKeys.APPLICATION_COMMENT_CREATED, comment.getCreated());
        jsonComment.put(ApplicationKeys.APPLICATION_COMMENT_LIKECOUNT, comment.getLikeCount());
        return jsonComment;
    }

    public static JSONObject convertJSON(Post post) throws JSONException {
        JSONObject jsonPost = new JSONObject();
        jsonPost.put(ApplicationKeys.APPLICATION_POST_POST_ID, post.getPost_id());
        jsonPost.put(ApplicationKeys.APPLICATION_USER_USERNAME, post.getUsername());
        jsonPost.put(ApplicationKeys.APPLICATION_USER_USER_ID, post.getUserid());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_TITLE, post.getTitle());
        jsonPost.put(ApplicationKeys.APPLICATION_USER_PICTURE, post.getPicture());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_PICTURE, post.getPostPicture());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_TEXT, post.getText());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_CREATED, post.getCreated());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_LIKECOUNT, post.getLikeCount());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_COMMENTCOUNT, post.getCommentCount());
        jsonPost.put(ApplicationKeys.APPLICATION_POST_SHARECOUNT, post.getShareCount());
        return jsonPost;
    }

    public static String convertString(Comment comment) throws JSONException {
        return convertJSON(comment).toString();
    }

    public static String convertString(Post post) throws JSONException {
        return  convertJSON(post).toString();
    }

    public static Like convertLike(JSONObject jsonLike) throws JSONException {
        Like like = new Like(
                jsonLike.getString(ApplicationKeys.APPLICATION_USER_USER_ID),
                jsonLike.getString(ApplicationKeys.APPLICATION_USER_USERNAME));
        return like;
    }

    public static LikeResult convertPostLikeResult(JSONObject jsonLikeResult) throws JSONException {
        LikeResult likeResult = new LikeResult(
                Integer.parseInt(jsonLikeResult.getString(ApplicationKeys.APPLICATION_POST_LIKED))==1,
                jsonLikeResult.getString(ApplicationKeys.APPLICATION_POST_LIKECOUNT));
        return likeResult;
    }

    public static LikeResult convertCommentLikeResult(JSONObject jsonLikeResult) throws JSONException {
        LikeResult likeResult = new LikeResult(
                Integer.parseInt(jsonLikeResult.getString(ApplicationKeys.APPLICATION_COMMENT_LIKED))==1,
                jsonLikeResult.getString(ApplicationKeys.APPLICATION_COMMENT_LIKECOUNT));
        return likeResult;
    }
}
