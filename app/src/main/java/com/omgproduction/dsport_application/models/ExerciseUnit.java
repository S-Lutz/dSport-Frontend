package com.omgproduction.dsport_application.models;

import android.content.Context;

import com.omgproduction.dsport_application.R;

/**
 * Created by Florian on 22.03.2017.
 */

public abstract class ExerciseUnit {

    private String id, userId, exerciseId, exerciseTitle, userPic, username, date;
    private int type, likeCount;
    private boolean liked;

    public ExerciseUnit(String id, String userId, String exerciseId, String exerciseTitle, int type, String userPic, String username, int likeCount, boolean liked, String date) {
        this.id = id;
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.exerciseTitle = exerciseTitle;
        this.type = type;
        this.userPic = userPic;
        this.username = username;
        this.likeCount = likeCount;
        this.liked = liked;
        this.date = date;
    }


    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLikeString(Context context) {
        StringBuilder sb = new StringBuilder();
        int likes = likeCount;
        if(isLiked()){
            String youPlus = context.getResources().getString(R.string.you_plus);
            sb.append(youPlus);
            sb.append(" ");
            likes = likes-1;
        }
        sb.append(likes);
        return sb.toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
