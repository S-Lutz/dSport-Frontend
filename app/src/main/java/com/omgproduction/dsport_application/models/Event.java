package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.io.Serializable;

/**
 * Created by Strik on 08.03.2017.
 */

public class Event implements Serializable {


    private String event_id, username, userid, picture, eventPicture, text, created, eventDate, location, likeCount, commentCount, shareCount, title, eventMember;
    private boolean liked;



    private boolean participating;



    /**
     *
     * @param event_id
     * @param username
     * @param userid
     * @param title Title of the event
     * @param picture Possible extra picture below title
     * @param eventPicture Picture of the event at the header
     * @param text Description of the event
     * @param created Date when event was created
     * @param eventDate Date when event takes place
     * @param likeCount number of likes
     * @param commentCount number of comments
     * @param shareCount number of shares
     * @param liked shows if user liked this event or not
     * @param participating
     */
    public Event(String event_id, String username, String userid, String title, String eventPicture, String picture,  String text, String created, String eventDate, String eventMember,String location, String likeCount, String commentCount, String shareCount, boolean participating, boolean liked) {
        this.username = username;
        this.userid = userid;
        this.picture = picture;
        this.eventPicture = eventPicture;
        this.text = text;
        this.created = created;
        this.eventMember = eventMember;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.title = title;
        this.event_id = event_id;
        this.liked = liked;
        this.participating = participating;
        this.location = location;
        this.eventDate = eventDate;
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

    public String getEventPicture() {
        return eventPicture;
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

    public String getEventMember() {
        return eventMember;
    }

    public String getShareCount() {
        return shareCount;
    }

    public String getTitle() {
        return title;
    }

    public String getEvent_id() {
        return event_id;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isParticipating() {
        return participating;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getEventDate() { return eventDate; }

    public String getLocation() {
        return location;
    }

    public Bitmap getBitmapPicture(Context context){

        if(picture.isEmpty()){
            return null;
        }
        return BitmapUtils.getBitmapFromString(context,picture);
    }


    public Bitmap getBitmapEventPicture(Context context){

        if(eventPicture.isEmpty()){
            return null;
        }
        return BitmapUtils.getBitmapFromString(context,eventPicture);
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

