package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.refactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.helper.Converter;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.AbstractSet;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.DistanceBasedSet;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.RepeatBasedSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExerciseUnitService {
    //public void getExerciseUnits(Context context, Long id, BackendCallback<ArrayList<ExerciseUnitNode>> callback) {
    //    BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetExerciseUnitsRoute(id), ExerciseUnitNode.class, null, PreferencesService.getToken(context),callback );
    //}

    public void getExerciseUnits(Context context, Long id, BackendCallback<ArrayList<ExerciseUnitNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetExerciseUnitsRoute(id), ExerciseUnitNode.class, null, PreferencesService.getToken(context), callback, new Converter<JSONObject, ExerciseUnitNode>() {
            @Override
            public ExerciseUnitNode convert(JSONObject input) {
                Gson gson = new Gson();

                try {
                    ExerciseUnitNode exerciseUnitNode = gson.fromJson(input.toString(), ExerciseUnitNode.class);
                    exerciseUnitNode.setOfType(gson.fromJson(input.getJSONObject("ofType").get("exerciseNode").toString(), ExerciseNode.class));
                    return exerciseUnitNode;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;

                }

            }
        });
    }

    public void createExerciseUnit(Context context, Long ofExerciseId, ExerciseUnitNode exerciseUnitNode, BackendCallback<ExerciseUnitNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateCreateExerciseUnitRoute(ofExerciseId), ExerciseUnitNode.class, exerciseUnitNode, PreferencesService.getToken(context), callback);
    }

    public void addSet(Context context, Long exerciseUnitId, ArrayList<RepeatBasedSet> set, BackendCallback<ExerciseUnitNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateAddSetRoute(exerciseUnitId), ExerciseUnitNode.class, set, PreferencesService.getToken(context), callback);
    }

    public void addAbstractSet(Context context, Long exerciseUnitId, ArrayList<AbstractSet> set, BackendCallback<ExerciseUnitNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateAddSetRoute(exerciseUnitId), ExerciseUnitNode.class, set, PreferencesService.getToken(context), callback);
    }

    public void addDistanceSet(Context context, Long exerciseUnitId, ArrayList<DistanceBasedSet> set, BackendCallback<ExerciseUnitNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateAddSetRoute(exerciseUnitId), ExerciseUnitNode.class, set, PreferencesService.getToken(context), callback);
    }

    public void getExerciseUnitDetail(Context context, Long exerciseUnitId, BackendCallback<ArrayList<AbstractSet>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetExerciseUnitDetailRoute(exerciseUnitId), AbstractSet.class, null, PreferencesService.getToken(context), callback, new Converter<JSONObject, AbstractSet>() {
            @Override
            public AbstractSet convert(JSONObject input) {
                Gson gson = new Gson();

                try {
                    ExerciseNode exerciseNode = gson.fromJson(input.get("exerciseNode").toString(), ExerciseNode.class);
                    AbstractSet abstractSet = null;
                    if (exerciseNode.getSetType().equals("REPEAT")){
                        abstractSet = gson.fromJson(input.get("set").toString(), RepeatBasedSet.class);
                    }else if (exerciseNode.getSetType().equals("TIME")){
                        abstractSet = gson.fromJson(input.get("set").toString(), AbstractSet.class);
                    }else if (exerciseNode.getSetType().equals("DISTANCE")){
                        abstractSet = gson.fromJson(input.get("set").toString(), DistanceBasedSet.class);
                    }

                    return abstractSet;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}