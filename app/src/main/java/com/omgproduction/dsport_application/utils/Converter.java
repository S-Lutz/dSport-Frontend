package com.omgproduction.dsport_application.utils;

import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public class Converter {
    public static User convertUser(JSONObject jsonUser) throws JSONException {
        User user = new User(
                jsonUser.getString(ApplicationKeys.USER_ID),
                jsonUser.getString(ApplicationKeys.USERNAME),
                jsonUser.getString(ApplicationKeys.EMAIL),
                jsonUser.getString(ApplicationKeys.PICTURE),
                jsonUser.getString(ApplicationKeys.FIRSTNAME),
                jsonUser.getString(ApplicationKeys.LASTNAME),
                jsonUser.getString(ApplicationKeys.CREATED),
                jsonUser.getString(ApplicationKeys.AGB_VERSION)
        );
        return user;
    }

    public static Post convertPost(JSONObject jsonPost) throws JSONException {
        Post post = new Post(
                jsonPost.getString(ApplicationKeys.POST_ID),
                jsonPost.getString(ApplicationKeys.USERNAME),
                jsonPost.getString(ApplicationKeys.USER_ID),
                jsonPost.getString(ApplicationKeys.TITLE),
                jsonPost.getString(ApplicationKeys.PICTURE),
                jsonPost.getString(ApplicationKeys.POST_PICTURE),
                jsonPost.getString(ApplicationKeys.TEXT),
                jsonPost.getString(ApplicationKeys.CREATED),
                jsonPost.getString(ApplicationKeys.LIKE_COUNT),
                jsonPost.getString(ApplicationKeys.COMMENT_COUNT),
                jsonPost.getString(ApplicationKeys.SHARE_COUNT)
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
                jsonComment.getString(ApplicationKeys.COMMENT_ID),
                jsonComment.getString(ApplicationKeys.USERNAME),
                jsonComment.getString(ApplicationKeys.USER_ID),
                jsonComment.getString(ApplicationKeys.COMMENT_PICTURE),
                jsonComment.getString(ApplicationKeys.TEXT),
                jsonComment.getString(ApplicationKeys.CREATED),
                jsonComment.getString(ApplicationKeys.LIKE_COUNT)
        );
        return comment;
    }

    public static JSONObject convertJSON(Comment comment) throws JSONException {
        JSONObject jsonComment = new JSONObject();
        jsonComment.put(ApplicationKeys.COMMENT_ID, comment.getComment_id());
        jsonComment.put(ApplicationKeys.USERNAME, comment.getUsername());
        jsonComment.put(ApplicationKeys.USER_ID, comment.getUserid());
        jsonComment.put(ApplicationKeys.COMMENT_PICTURE, comment.getCommentPicture());
        jsonComment.put(ApplicationKeys.TEXT, comment.getText());
        jsonComment.put(ApplicationKeys.CREATED, comment.getCreated());
        jsonComment.put(ApplicationKeys.LIKE_COUNT, comment.getLikeCount());
        return jsonComment;
    }

    public static JSONObject convertJSON(Post post) throws JSONException {
        JSONObject jsonPost = new JSONObject();
        jsonPost.put(ApplicationKeys.POST_ID, post.getPost_id());
        jsonPost.put(ApplicationKeys.USERNAME, post.getUsername());
        jsonPost.put(ApplicationKeys.USER_ID, post.getUserid());
        jsonPost.put(ApplicationKeys.TITLE, post.getTitle());
        jsonPost.put(ApplicationKeys.PICTURE, post.getPicture());
        jsonPost.put(ApplicationKeys.POST_PICTURE, post.getPostPicture());
        jsonPost.put(ApplicationKeys.TEXT, post.getText());
        jsonPost.put(ApplicationKeys.CREATED, post.getCreated());
        jsonPost.put(ApplicationKeys.LIKE_COUNT, post.getLikeCount());
        jsonPost.put(ApplicationKeys.COMMENT_COUNT, post.getCommentCount());
        jsonPost.put(ApplicationKeys.SHARE_COUNT, post.getShareCount());
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
                jsonLike.getString(ApplicationKeys.USER_ID),
                jsonLike.getString(ApplicationKeys.USERNAME));
        return like;
    }
}
