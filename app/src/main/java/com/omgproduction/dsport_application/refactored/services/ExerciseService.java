package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;

import com.omgproduction.dsport_application.refactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseNode;

import java.util.ArrayList;

public class ExerciseService {

    public void getExercises(Context context, Long userId, BackendCallback<ArrayList<ExerciseNode>> callback){
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetExercisesRoute(userId), ExerciseNode.class, null, PreferencesService.getToken(context), callback);
    }

    public void createExercise(Context context, ExerciseNode exerciseNode, BackendCallback<ExerciseNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateCreateExerciseRoute(), ExerciseNode.class, exerciseNode,PreferencesService.getToken(context), callback);
    }
}