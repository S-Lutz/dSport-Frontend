package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.herbornsoftware.omnet.JSONRequest;
import com.herbornsoftware.omnet.JSONResponse;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;

import java.net.MalformedURLException;

/**
 * Created by Strik on 22.03.2017.
 */

public class ExerciseService extends AbstractService {

    public ExerciseService(Context context) {super(context); }

    public void saveExercise(final String localUserID, final String exerciseName, final String type, final IRequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_CREATE_EXERCISE)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EXERCISE_EXERCISE_NAME, exerciseName)
                    .addParam(APPLICATION_EXERCISE_EXERCISE_TYPE, type)
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
}
