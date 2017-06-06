package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.omgproduction.dsport_application.utils.DateConverter;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.Exercise;
import com.omgproduction.dsport_application.models.ExerciseUnit;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.TimedSet;
import com.omgproduction.dsport_application.models.WeightSet;
import com.omgproduction.dsport_application.utils.ConverterFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;
/**
 * Created by Strik on 22.03.2017.
 */

public class ExerciseService extends AbstractService {

    public ExerciseService(Context context) {super(context); }

    public void saveExercise(final String localUserID, final String exerciseName, final int type, final IRequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EXERCISES_CREATE)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EXERCISE_TITLE, exerciseName)
                    .addParam(APPLICATION_EXERCISE_TYPE, type)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
                            }else{
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void getAllExercises(final String localUserID, final IRequestFuture<List<Exercise>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EXERCISES_GET_ALL)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToExerciseConverter(),APPLICATION_EXERCISES));
                            }else{
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void getAllExerciseUnits(final String localUserID, final IRequestFuture<List<ExerciseUnit>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EXERCISE_UNIT_GET_ALL)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .setDebug(true)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToExerciseUnitConverter(),APPLICATION_EXERCISE_UNITS));
                            }else{
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void saveDistanceExerciseUnit(final String localUserID, final int exerciseId, final String exerciseTitle, final long time, final float distance,  final IRequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EXERCISE_UNIT_CREATE_TYPE1)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EXERCISE_UNIT_EXERCISE_ID, exerciseId)
                    .addParam(APPLICATION_EXERCISE_UNIT_TITLE, exerciseTitle)
                    .addParam(APPLICATION_EXERCISE_UNIT_TIME, time)
                    .addParam(APPLICATION_EXERCISE_DISTANCE, distance)
                    .addParam(APPLICATION_EXERCISE_UNIT_CREATED, new DateConverter().convertNow())
                    .setDebug(true)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
                            }else{
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void likeExerciseUnit(final String localUserID, final String exerciseUnitId, final IRequestFuture<LikeResult> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_EXERCISE_UNIT_LIKE)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EXERCISE_UNIT_ID, exerciseUnitId)
                    .setDebug(true)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToExerciseUnitLikeResultConverter()));
                            }else{
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void saveWeightExercise(final String localUserID, final int exerciseId, final String exerciseTitle, final List<WeightSet> sets,final IRequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONArray jsonSets = new JSONArray();
            for(WeightSet set : sets){
                JSONObject object = new JSONObject();
                object.put(APPLICATION_EXERCISE_UNIT_TIME, set.getTime());
                object.put(APPLICATION_EXERCISE_UNIT_WEIGHT, set.getWeight());
                object.put(APPLICATION_EXERCISE_UNIT_REPEATS, set.getRepeats());
                jsonSets.put(object);
            }
            JSONObject setsObject = new JSONObject();
            setsObject.put(APPLICATION_EXERCISE_UNIT_SETS, jsonSets);


            try {
                JSONRequest request = new JSONRequest(ROUTE_EXERCISE_UNIT_CREATE_TYPE0)
                        .addParam(APPLICATION_USER_USER_ID, localUserID)
                        .addParam(APPLICATION_EXERCISE_UNIT_EXERCISE_ID, exerciseId)
                        .addParam(APPLICATION_EXERCISE_UNIT_TITLE, exerciseTitle)
                        .addParam(APPLICATION_EXERCISE_UNIT_SETS, setsObject)
                        .addParam(APPLICATION_EXERCISE_UNIT_CREATED, new DateConverter().convertNow())
                        .setDebug(true)
                        .setOnResultListener(new JSONRequest.OnResultListener() {
                            @Override
                            public void onResult(JSONResponse response) {
                                listener.onFinishQuery();
                                if(response.isOk()){
                                    listener.onSuccess(null);
                                }else{
                                    listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                                }
                            }
                        });
                request.execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listener.onFinishQuery();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void saveTimedExercise(final String localUserID, final int exerciseId, final String exerciseTitle, final List<TimedSet> sets, final IRequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONArray jsonSets = new JSONArray();
            for(TimedSet set : sets){
                JSONObject object = new JSONObject();
                object.put(APPLICATION_EXERCISE_UNIT_TIME, set.getTime());
                jsonSets.put(object);
            }
            JSONObject setsObject = new JSONObject();
            setsObject.put(APPLICATION_EXERCISE_UNIT_SETS, jsonSets);


            try {
                JSONRequest request = new JSONRequest(ROUTE_EXERCISE_UNIT_CREATE_TYPE2)
                        .addParam(APPLICATION_USER_USER_ID, localUserID)
                        .addParam(APPLICATION_EXERCISE_UNIT_EXERCISE_ID, exerciseId)
                        .addParam(APPLICATION_EXERCISE_UNIT_TITLE, exerciseTitle)
                        .addParam(APPLICATION_EXERCISE_UNIT_SETS, setsObject)
                        .addParam(APPLICATION_EXERCISE_UNIT_CREATED, new DateConverter().convertNow())
                        .setDebug(true)
                        .setOnResultListener(new JSONRequest.OnResultListener() {
                            @Override
                            public void onResult(JSONResponse response) {
                                listener.onFinishQuery();
                                if(response.isOk()){
                                    listener.onSuccess(null);
                                }else{
                                    listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                                }
                            }
                        });
                request.execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                listener.onFinishQuery();
            }
        }catch (JSONException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }
}
