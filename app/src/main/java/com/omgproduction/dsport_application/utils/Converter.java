package com.omgproduction.dsport_application.utils;

import android.app.IntentService;

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

public class Converter {
    public static User convertUser(JSONObject jsonUser) throws JSONException {
        User user = new User(
                jsonUser.getString(ApplicationKeys.USER_USER_ID),
                jsonUser.getString(ApplicationKeys.USER_USERNAME),
                jsonUser.getString(ApplicationKeys.USER_EMAIL),
                jsonUser.getString(ApplicationKeys.USER_PICTURE),
                jsonUser.getString(ApplicationKeys.USER_FIRSTNAME),
                jsonUser.getString(ApplicationKeys.USER_LASTNAME),
                jsonUser.getString(ApplicationKeys.USER_CREATED),
                jsonUser.getString(ApplicationKeys.USER_AGBVERSION)
        );
        return user;
    }

    public static Post convertPost(JSONObject jsonPost) throws JSONException {
        Post post = new Post(
                jsonPost.getString(ApplicationKeys.POST_POST_ID),
                jsonPost.getString(ApplicationKeys.USER_USERNAME),
                jsonPost.getString(ApplicationKeys.USER_USER_ID),
                jsonPost.getString(ApplicationKeys.POST_TITLE),
                jsonPost.getString(ApplicationKeys.USER_PICTURE),
                jsonPost.getString(ApplicationKeys.POST_PICTURE),
                jsonPost.getString(ApplicationKeys.POST_TEXT),
                jsonPost.getString(ApplicationKeys.POST_CREATED),
                jsonPost.getString(ApplicationKeys.POST_LIKECOUNT),
                jsonPost.getString(ApplicationKeys.POST_COMMENTCOUNT),
                jsonPost.getString(ApplicationKeys.POST_SHARECOUNT),
                Integer.parseInt(jsonPost.getString(ApplicationKeys.POST_LIKED))==1
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
                jsonComment.getString(ApplicationKeys.USER_USERNAME),
                jsonComment.getString(ApplicationKeys.USER_USER_ID),
                jsonComment.getString(ApplicationKeys.COMMENT_PICTURE),
                jsonComment.getString(ApplicationKeys.COMMENT_TEXT),
                jsonComment.getString(ApplicationKeys.COMMENT_CREATED),
                jsonComment.getString(ApplicationKeys.COMMENT_LIKECOUNT),
                Integer.parseInt(jsonComment.getString(ApplicationKeys.COMMENT_LIKED))==1
        );
        return comment;
    }

    public static JSONObject convertJSON(Comment comment) throws JSONException {
        JSONObject jsonComment = new JSONObject();
        jsonComment.put(ApplicationKeys.COMMENT_ID, comment.getComment_id());
        jsonComment.put(ApplicationKeys.USER_USERNAME, comment.getUsername());
        jsonComment.put(ApplicationKeys.USER_USER_ID, comment.getUserid());
        jsonComment.put(ApplicationKeys.COMMENT_PICTURE, comment.getCommentPicture());
        jsonComment.put(ApplicationKeys.COMMENT_TEXT, comment.getText());
        jsonComment.put(ApplicationKeys.COMMENT_CREATED, comment.getCreated());
        jsonComment.put(ApplicationKeys.COMMENT_LIKECOUNT, comment.getLikeCount());
        return jsonComment;
    }

    public static JSONObject convertJSON(Post post) throws JSONException {
        JSONObject jsonPost = new JSONObject();
        jsonPost.put(ApplicationKeys.POST_POST_ID, post.getPost_id());
        jsonPost.put(ApplicationKeys.USER_USERNAME, post.getUsername());
        jsonPost.put(ApplicationKeys.USER_USER_ID, post.getUserid());
        jsonPost.put(ApplicationKeys.POST_TITLE, post.getTitle());
        jsonPost.put(ApplicationKeys.USER_PICTURE, post.getPicture());
        jsonPost.put(ApplicationKeys.POST_PICTURE, post.getPostPicture());
        jsonPost.put(ApplicationKeys.POST_TEXT, post.getText());
        jsonPost.put(ApplicationKeys.POST_CREATED, post.getCreated());
        jsonPost.put(ApplicationKeys.POST_LIKECOUNT, post.getLikeCount());
        jsonPost.put(ApplicationKeys.POST_COMMENTCOUNT, post.getCommentCount());
        jsonPost.put(ApplicationKeys.POST_SHARECOUNT, post.getShareCount());
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
                jsonLike.getString(ApplicationKeys.USER_USER_ID),
                jsonLike.getString(ApplicationKeys.USER_USERNAME));
        return like;
    }

    public static LikeResult convertPostLikeResult(JSONObject jsonLikeResult) throws JSONException {
        LikeResult likeResult = new LikeResult(
                Integer.parseInt(jsonLikeResult.getString(ApplicationKeys.POST_LIKED))==1,
                jsonLikeResult.getString(ApplicationKeys.POST_LIKECOUNT));
        return likeResult;
    }

    public static LikeResult convertCommentLikeResult(JSONObject jsonLikeResult) throws JSONException {
        LikeResult likeResult = new LikeResult(
                Integer.parseInt(jsonLikeResult.getString(ApplicationKeys.COMMENT_LIKED))==1,
                jsonLikeResult.getString(ApplicationKeys.COMMENT_LIKECOUNT));
        return likeResult;
    }
}
