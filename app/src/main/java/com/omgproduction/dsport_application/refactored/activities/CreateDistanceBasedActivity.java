package com.omgproduction.dsport_application.refactored.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.intent.keys.locationkeys.LocationKeys;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.DistanceBasedSet;
import com.omgproduction.dsport_application.refactored.services.ExerciseUnitService;
import com.omgproduction.dsport_application.refactored.services.GPS_Service;

import java.util.ArrayList;
import java.util.Map;

public class CreateDistanceBasedActivity extends TimeTrackingActivity implements View.OnClickListener, LocationKeys {

    private ExerciseUnitService exerciseUnitService;
    private ExerciseUnitNode currenExerciseUnit;
    private ArrayList<DistanceBasedSet> sets;

    private TextView distanceTV, timeTV;
    private ProgressBar progressBar;
    private ImageView runImg;

    private boolean started = false;
    private boolean firstStart = true;
    private boolean finished = false;

    private Button startBtn, finishBtn;

    private BroadcastReceiver broadcastReceiver;
    private long currentTime;
    private float currentDistance;
    private long finalTime;
    private float finalDistance;
    private TimeDisplayMode currentTimeMode = TimeDisplayMode.SECONDS;
    private DistanceDisplayMode currentDistanceMode = DistanceDisplayMode.M;


    private ExerciseNode exercise;

    private enum TimeDisplayMode {
        SECONDS,
        MINUTES,
        HOURS
    }

    private enum DistanceDisplayMode {
        M,
        KM
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    currentDistance = intent.getFloatExtra(TRAVELED_DISTANCE,0);
                    updateDistance(currentDistance);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter(RESULT_INTENT));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout_activity_distance_based_set);

        exerciseUnitService = new ExerciseUnitService();

        sets = new ArrayList<>();

        distanceTV = (TextView) findViewById(R.id.tracking_distance_tv);
        timeTV = (TextView) findViewById(R.id.create_weight_exercise_unit_time_tv);
        progressBar = (ProgressBar) findViewById(R.id.create_weight_exercise_unit_progress);
        runImg = (ImageView) findViewById(R.id.tracking_run_img);

        startBtn = (Button) findViewById(R.id.tracking_start_btn);
        finishBtn = (Button) findViewById(R.id.tracking_finish_btn);
        timeTV.setOnClickListener(this);
        distanceTV.setOnClickListener(this);
        runImg.setOnClickListener(this);


        exercise = (ExerciseNode) getIntent().getSerializableExtra("EXERCISE");
        createExerciseUnit(exercise.getId());

        progressBar.setVisibility(View.INVISIBLE);
        enableButtons(false, startBtn, finishBtn);
        if(!runtimePermissions()){
            enableButtons(true, startBtn);
        }

    }

    private void enableButtons(boolean flag, Button... buttons){
        for(Button b : buttons){
            b.setEnabled(flag);
            b.setOnClickListener(flag?this:null);
        }
    }

    private boolean runtimePermissions() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableButtons(true, startBtn, finishBtn);
            }
        }else {
            runtimePermissions();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tracking_start_btn:
                startPressed();
                break;
            case R.id.tracking_finish_btn:
                stopPressed();
                break;
            case R.id.create_weight_exercise_unit_time_tv:
                nextTimeMode();
                break;
            case R.id.tracking_distance_tv:
                nextDistanceMode();
                break;
            case R.id.tracking_run_img:
                nextTimeMode();
                break;
        }
    }

    private void nextTimeMode() {
        currentTimeMode = TimeDisplayMode.values()[(currentTimeMode.ordinal()+1)% TimeDisplayMode.values().length];
        updateTime(currentTime);
    }

    private void nextDistanceMode() {
        currentDistanceMode = DistanceDisplayMode.values()[(currentDistanceMode.ordinal()+1)% DistanceDisplayMode.values().length];
        updateDistance(currentDistance);
    }

    private void updateDistance(float currentDistance) {
        float m = Math.round(currentDistance*100)/100;
        float km = Math.round(currentDistance/10)/100;

        switch (currentDistanceMode){
            case M:
                distanceTV.setText(m+getString(R.string.meter));
                break;
            case KM:
                distanceTV.setText(km+getString(R.string.kilometer));
                break;
        }
    }

    private void stopPressed() {
        if(!finished){
            if(started){
                new AlertDialog.Builder(this)
                        .setTitle(R.string.finish)
                        .setMessage(R.string.close_session)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startBtn.setText(R.string.start_new_session);
                                finishBtn.setText(R.string.save_session);
                                finished = true;
                                started = false;
                                progressBar.setVisibility(View.INVISIBLE);
                                finalDistance = currentDistance;
                                finalTime = currentTime;
                                stopTimeTracking();
                                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                                stopService(i);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }else {
            saveExerciseUnit();
        }
    }

    private void createExerciseUnit(Long ofExerciseId) {
        exerciseUnitService.createExerciseUnit(this, ofExerciseId, new ExerciseUnitNode(), new BackendCallback<ExerciseUnitNode>() {
            @Override
            public void onSuccess(ExerciseUnitNode result, Map<String, String> responseHeader) {
                currenExerciseUnit = result;
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }

        });

    }

    private void saveExerciseUnit() {

        sets.add(new DistanceBasedSet(String.valueOf(finalTime),String.valueOf(finalDistance)));

            exerciseUnitService.addDistanceSet(this, currenExerciseUnit.getId(), sets, new BackendCallback<ExerciseUnitNode>() {
                @Override
                public void onSuccess(ExerciseUnitNode result, Map<String, String> responseHeader) {
                }

                @Override
                public void onFailure(ErrorResponse error) {

                }

            });

        finish();
    }

    private void startPressed() {
        if(!started){
            if(firstStart){
                startTimeTracking();
                started = true;
                firstStart = false;
                finished = false;
                startBtn.setText(getString(R.string.pause_tracking));
                progressBar.setVisibility(View.VISIBLE);
                finishBtn.setText(getString(R.string.finish_tracking));
                enableButtons(true,finishBtn);
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                startService(i);
            }else {
                reallyDelete();
            }
        }else {
            if(isPaused()){
                progressBar.setVisibility(View.VISIBLE);
                startTimeTracking();
                startBtn.setText(getString(R.string.pause_tracking));
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                pauseTimeTracking();
                startBtn.setText(getString(R.string.start_tracking));
            }

        }
    }

    private void reallyDelete() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.reset)
                .setMessage(R.string.really_reset_session)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firstStart = true;
                        startPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onTimerTick(long currentTime) {
        this.currentTime = currentTime;
        updateTime(currentTime);
    }

    private void updateTime(long currentTime) {
        int seconds = (int) (currentTime/1000);
        int hr = seconds/3600;
        int min = seconds/60;


        switch (currentTimeMode){
            case HOURS:
                timeTV.setText(hr+getString(R.string.hour));
                break;
            case MINUTES:
                timeTV.setText(min+getString(R.string.minutes));
                break;
            case SECONDS:
                timeTV.setText(seconds+getString(R.string.seconds));
                break;
        }
    }
}