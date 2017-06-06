package com.omgproduction.dsport_application.activities.main;

import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.supplements.activities.AbstractAppCompatActivity;

public abstract class TimeTrackingActivity extends AbstractAppCompatActivity {

    private static String START_TIME = "starttime";
    private static String PAUSE_TIME = "pausetime";
    private static String PAUSE_STARTED = "pausestarted";
    private static String IS_STARTED = "isstarted";
    private static String IS_PAUSED = "ispaused";

    private long startTime = 0;
    private long pauseTime = 0;
    private long pauseStarted;

    private boolean isStarted = false;
    private boolean isPaused= false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            onTimerTick(getCurrentTime());
            handler.postDelayed(this,1000);
        }
    };

    protected abstract void onTimerTick(long currentTime);

    protected void startTimeTracking(){
        if(!isStarted){
            isStarted = true;
            pauseTime = 0;
            isPaused = false;
            startTime = System.currentTimeMillis();
        }else{
            continueTimeTracking();
        }
    }

    private void continueTimeTracking(){
        if(isPaused && isStarted){
            isPaused = false;
            pauseTime += System.currentTimeMillis()-pauseStarted;
        }
    }

    protected void pauseTimeTracking(){
        if(!isPaused && isStarted){
            isPaused = true;
            pauseStarted = System.currentTimeMillis();
        }
    }

    protected long stopTimeTracking(){
        isStarted = false;
        if(isPaused){
            isPaused = false;
            pauseTime += System.currentTimeMillis()-pauseStarted;
        }
        return getCurrentTime();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public long getPauseStarted() {
        return pauseStarted;
    }

    public long getCurrentTime() {

        long currentPause = isPaused?System.currentTimeMillis()-pauseStarted:0;
        if(isStarted){
            return System.currentTimeMillis() - startTime - pauseTime - currentPause;
        }else {
            return 0;
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(START_TIME,startTime);
        outState.putLong(PAUSE_TIME,pauseTime);
        outState.putLong(PAUSE_STARTED,pauseStarted);

        outState.putBoolean(IS_STARTED, isStarted);
        outState.putBoolean(IS_PAUSED, isPaused);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        startTime = savedInstanceState.getLong(START_TIME);
        pauseTime = savedInstanceState.getLong(PAUSE_TIME);
        pauseStarted = savedInstanceState.getLong(PAUSE_STARTED);

        isStarted = savedInstanceState.getBoolean(IS_STARTED);
        isPaused = savedInstanceState.getBoolean(IS_PAUSED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable,0);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
