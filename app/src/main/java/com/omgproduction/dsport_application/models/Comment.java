package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.App;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;

/**
 * Created by Florian on 01.12.2016.
 */

public class Comment implements Serializable{

    private String comment_id,username , userid, commentPicture, text, created, likeCount;
    private boolean liked;

    public Comment(String comment_id, String username, String userid, String commentPicture, String text, String created, String likeCount, boolean liked) {
        this.username = username;
        this.userid = userid;
        this.commentPicture = commentPicture;
        this.text = text;
        this.created = created;
        this.likeCount = likeCount;
        this.comment_id = comment_id;
        this.liked = liked;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public String getCommentPicture() {
        return commentPicture;
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

    public String getComment_id() {
        return comment_id;
    }

    public Bitmap getBitmapCommentPicture(Context context){

        if(commentPicture.isEmpty()){
            return null;
        }
        return BitmapUtils.getBitmapFromString(context, commentPicture);
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikeString() {
        StringBuilder sb = new StringBuilder();
        int likes = Integer.parseInt(getLikeCount());
        if(isLiked()){
            String youPlus = App.getInstance().getApplicationContext().getResources().getString(R.string.you_plus);
            sb.append(youPlus);
            sb.append(" ");
            likes = likes-1;
        }
        sb.append(likes);
        return sb.toString();
    }
}
