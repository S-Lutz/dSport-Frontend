package com.omgproduction.dsport_application.models;

import android.content.Context;
import android.graphics.Bitmap;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.App;
import com.omgproduction.dsport_application.utils.BitmapUtils;
import com.omgproduction.dsport_application.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Strik on 08.03.2017.
 */

public class Event implements Serializable {


    private String event_id, username, userid, picture, eventPicture, text, created, eventDate, likeCount, commentCount, shareCount, title, eventMember;
    private boolean liked;



    private boolean participating;
    private String locationName;
    private String locationAddress;

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
        this.locationName = getLocationName(location);
        this.locationAddress = getLocationAddress(location);
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

    public void setLocationName(CharSequence name){
        this.locationName = String.valueOf(name);
    }

    public void setLocationAddress(CharSequence address){
        this.locationAddress = String.valueOf(address);
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocation(){
        return toLocationString(getLocationName(), getLocationAddress());
    }

    public static String toLocationString(String locationName, String locationAddress){
        return locationName+"&"+locationAddress;
    }

    private String getLocationName(String locationString){
        try{
            return locationString.split("&")[0];
        }catch (ArrayIndexOutOfBoundsException e){
            return App.getContext().getString(R.string.unknown);
        }
    }

    private String getLocationAddress(String locationString){
        try{
            return locationString.split("&")[1];
        }catch (ArrayIndexOutOfBoundsException e){
            return App.getContext().getString(R.string.unknown);
        }
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getEventDate() { return eventDate; }

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

