package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;

/**
 * Created by Florian on 01.12.2016.
 */

public class Comment implements Serializable{

    private String comment_id,username , userid, commentPicture, text, created, likeCount;

    public Comment(String comment_id, String username, String userid, String commentPicture, String text, String created, String likeCount) {
        this.username = username;
        this.userid = userid;
        this.commentPicture = commentPicture;
        this.text = text;
        this.created = created;
        this.likeCount = likeCount;
        this.comment_id = comment_id;
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
}
