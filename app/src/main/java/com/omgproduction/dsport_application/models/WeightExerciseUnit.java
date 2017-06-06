package com.omgproduction.dsport_application.models;

import java.util.ArrayList;

/**
 * Created by Florian on 22.03.2017.
 */

public class WeightExerciseUnit extends ExerciseUnit{

    private ArrayList<WeightSet> sets;

    public WeightExerciseUnit(String id, String userId, String exerciseId, String exerciseTitle,ArrayList<WeightSet> sets, String userPic, String username, int likeCount, boolean liked, String date) {
        super(id, userId, exerciseId, exerciseTitle, 0,userPic, username, likeCount, liked, date);
        this.sets = sets;
    }

    public WeightExerciseUnit(String userId, String exerciseId, String exerciseTitle, ArrayList<WeightSet> sets, String userPic, String username, int likeCount, boolean liked, String date) {
        this("",userId, exerciseId, exerciseTitle, sets,userPic, username, likeCount, liked, date);
    }

    public ArrayList<WeightSet> getSets() {
        return sets;
    }

    public void setSets(ArrayList<WeightSet> sets) {
        this.sets = sets;
    }
}
