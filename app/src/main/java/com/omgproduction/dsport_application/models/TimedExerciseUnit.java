package com.omgproduction.dsport_application.models;

import java.util.ArrayList;

/**
 * Created by Florian on 22.03.2017.
 */

public class TimedExerciseUnit extends ExerciseUnit{

    private ArrayList<TimedSet> sets;

    public TimedExerciseUnit(String id, String userId, String exerciseId, String exerciseTitle, ArrayList<TimedSet> sets, String userPic, String username, int likeCount, boolean liked, String date) {
        super(id, userId, exerciseId, exerciseTitle, 2,userPic, username, likeCount, liked, date);
        this.sets = sets;
    }

    public TimedExerciseUnit(String id, String userId, String exerciseId, String exerciseTitle, String userPic, String username, int likeCount, boolean liked, String date) {
        this(id, userId, exerciseId, exerciseTitle, new ArrayList<TimedSet>(),userPic, username, likeCount, liked, date);
    }

    public ArrayList<TimedSet> getSets() {
        return sets;
    }

    public void setSets(ArrayList<TimedSet> sets) {
        this.sets = sets;
    }
}
