package com.omgproduction.dsport_application.models;

/**
 * Created by Florian on 22.03.2017.
 */

public class DistanceExerciseUnit extends ExerciseUnit {

    private long time;
    private float distance;


    public DistanceExerciseUnit(String id, String userId, String exerciseId, String exerciseTitle, long time, float distance, String userPic, String username, int likeCount, boolean liked, String date) {
        super(id, userId, exerciseId, exerciseTitle, 1, userPic, username,likeCount, liked, date);
        this.time = time;
        this.distance = distance;
    }

    public DistanceExerciseUnit(String userId, String exerciseId, String exerciseTitle, long time, float distance, String userPic, String username, int likeCount, boolean liked, String date) {
        this("", userId, exerciseId, exerciseTitle,time, distance,userPic, username, likeCount, liked, date);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
