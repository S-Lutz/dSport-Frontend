package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.App;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;

/**
 * Created by Florian on 17.11.2016.
 */

public class Post  implements Serializable {

    private String post_id,username , userid, picture, postPicture, text, created, likeCount, commentCount, shareCount, title;
    private boolean liked;

    public Post(String post_id, String username, String userid, String title, String picture, String postPicture, String text, String created, String likeCount, String commentCount, String shareCount, boolean liked) {
        this.username = username;
        this.userid = userid;
        this.picture = picture;
        this.postPicture = postPicture;
        this.text = text;
        this.created = created;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.title = title;
        this.post_id = post_id;
        this.liked = liked;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public String getPicture() {
        return picture;
    }

    public String getPostPicture() {
        return postPicture;
    }

    public String getText() {
        return text;
    }

    public String getCreated() {
        return created;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public String getTitle() {
        return title;
    }

    public String getPost_id() {
        return post_id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public Bitmap getBitmapPicture(Context context){

        if(picture.isEmpty()){
            return null;
        }
        return BitmapUtils.getBitmapFromString(context,picture);
    }

    public Bitmap getBitmapPostPicture(Context context){

        if(postPicture.isEmpty()){
            return null;
        }
        return BitmapUtils.getBitmapFromString(context,postPicture);
    }

    public String getLikeString(Context context) {
        StringBuilder sb = new StringBuilder();
        int likes = Integer.parseInt(getLikeCount());
        if(isLiked()){
            String youPlus = context.getResources().getString(R.string.you_plus);
            sb.append(youPlus);
            sb.append(" ");
            likes = likes-1;
        }
        sb.append(likes);
        return sb.toString();
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
}
