package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Florian on 17.11.2016.
 */

public class Post  implements Serializable {

    private String post_id,username , userid, picture, postPicture, text, created, likeCount, commentCount, shareCount, title;

    public Post(String post_id, String username, String userid, String title, String picture, String postPicture, String text, String created, String likeCount, String commentCount, String shareCount) {
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
}
