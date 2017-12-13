package com.omgproduction.dsport_application.activities.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.CreateTimedSetAdapter;
import com.omgproduction.dsport_application.config.ExerciseUnitKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Exercise;
import com.omgproduction.dsport_application.models.TimedSet;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.ExerciseService;

import java.util.ArrayList;
import java.util.List;

public class CreateTimedExerciseUnitActivity extends TimeTrackingActivity implements CreateTimedSetAdapter.OnSetLongClickedListener, ExerciseUnitKeys {

    private RecyclerView recyclerView;
    private CreateTimedSetAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<TimedSet> sets;

    private TextView timeTV;

    private Button addSetBtn, addTimedBtn, finishBtn;
    private ProgressBar progressBar;
    private long currentTime;
    private boolean started;

    private Exercise exercise;
    private long finalTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timed_exercise_unit);
        sets = new ArrayList<>();

        this.exercise = (Exercise) getIntent().getSerializableExtra(EXERCISE);

        recyclerView = (RecyclerView) findViewById(R.id.create_timed_exercise_unit_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        adapter = new CreateTimedSetAdapter(sets);
        recyclerView.setAdapter(adapter);
        adapter.setOnSetLongClickedListener(this);

        timeTV = (TextView) findViewById(R.id.create_timed_exercise_unit_time_tv);
        addSetBtn = (Button) findViewById(R.id.create_timed_exercise_unit_add_set_bt);
        addTimedBtn = (Button) findViewById(R.id.create_timed_exercise_unit_start_time_set_btn);
        finishBtn = (Button) findViewById(R.id.create_timed_exercise_unit_finish_and_save_btn);
        progressBar = (ProgressBar) findViewById(R.id.create_timed_exercise_unit_progress);
        progressBar.setVisibility(View.GONE);
        timeTV.setVisibility(View.GONE);

        timeTV.setOnClickListener(this);
        addSetBtn.setOnClickListener(this);
        addTimedBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        progressBar.setOnClickListener(this);



    }

    @Override
    protected void removeAllErrors() {}

    @Override
    public void onRefresh() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_timed_exercise_unit_add_set_bt:
                addSet(0);
                break;
            case R.id.create_timed_exercise_unit_start_time_set_btn:
                onTimeSetStarted();
                break;
            case R.id.create_timed_exercise_unit_finish_and_save_btn:
                saveExerciseUnit();
                break;
        }
    }

    private void saveExerciseUnit() {

        if(sets.isEmpty()){
            Toast.makeText(this, "You have to add a set before saving!", Toast.LENGTH_SHORT).show();
        }else {
            User localUser = getLocalUser();
            ExerciseService exerciseService = new ExerciseService(this);
            exerciseService.saveTimedExercise(localUser.getId(), Integer.parseInt(exercise.getId()), exercise.getTitle(), sets, new RequestFuture<Void>(){
                @Override
                public void onSuccess(Void result) {
                    CreateTimedExerciseUnitActivity.super.onBackPressed();
                    CreateTimedExerciseUnitActivity.super.finish();
                }

                @Override
                public void onFailure(int errorCode, String errorMessage) {
                    printError(R.id.create_timed_exercise_unit_activity,errorMessage);
                }
            });
        }
    }

    private void onTimeSetStarted() {
        if(!started){
            addTimedBtn.setText(R.string.stop_timed_set);
            started = true;
            //TODO Start TImer
            startTimeTracking();
        }else {
            addTimedBtn.setText(R.string.start_timed_set);
            started = false;
            pauseTimeTracking();
            finalTime = currentTime;
            new AlertDialog.Builder(this)
                    .setTitle("Add Set")
                    .setMessage("Are you sure you want to add this set with "+finalTime/1000+"seconds?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addSet(finalTime);
                            stopTimeTracking();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new AlertDialog.Builder(CreateTimedExerciseUnitActivity.this)
                                    .setTitle("Continue Set")
                                    .setMessage("Do you want to continue the current set?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            onTimeSetStarted();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            stopTimeTracking();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        //TODO Stop TImer

    }

    private void addSet(long time) {
        TimedSet set = new TimedSet(time);
        sets.add(set);
        adapter.notifyItemRangeChanged(sets.size()-1, sets.size());
    }

    @Override
    public void onLongClicked(final int position, TimedSet set) {
        new AlertDialog.Builder(this)
                .setTitle("Delete set")
                .setMessage("Are you sure you want to delete this set?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sets.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, sets.size());
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
        timeTV.setText(String.valueOf(currentTime/1000)+getString(R.string.seconds));
    }

    @Override
    protected void startTimeTracking() {
        progressBar.setVisibility(View.VISIBLE);
        timeTV.setVisibility(View.VISIBLE);
        super.startTimeTracking();
    }

    @Override
    protected void pauseTimeTracking() {
        progressBar.setVisibility(View.GONE);
        timeTV.setVisibility(View.GONE);
        super.pauseTimeTracking();
    }

    @Override
    protected long stopTimeTracking() {
        progressBar.setVisibility(View.GONE);
        timeTV.setVisibility(View.GONE);
        return super.stopTimeTracking();
    }
}
