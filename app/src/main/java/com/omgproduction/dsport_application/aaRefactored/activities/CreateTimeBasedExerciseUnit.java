package com.omgproduction.dsport_application.aaRefactored.activities;


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
import com.omgproduction.dsport_application.aaRefactored.adapter.TimeBasedSetAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.sets.AbstractSet;
import com.omgproduction.dsport_application.aaRefactored.services.ExerciseUnitService;

import java.util.ArrayList;
import java.util.Map;

public class CreateTimeBasedExerciseUnit extends TimeTrackingActivity implements View.OnClickListener,TimeBasedSetAdapter.OnSetLongClickedListener{

    private RecyclerView recyclerView;
    private TimeBasedSetAdapter adapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<AbstractSet> sets;

    private TextView timeTV;

    private Button addSetBtn, addTimedBtn, finishBtn;
    private ProgressBar progressBar;
    private long currentTime;
    private boolean started;

    private ExerciseUnitNode currenExerciseUnit;

    private ExerciseUnitService exerciseUnitService;

    private ExerciseNode exercise;
    private long finalTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_create_timed_exercise_unit);
        sets = new ArrayList<>();

        this.exercise = (ExerciseNode) getIntent().getSerializableExtra("EXERCISE");
        this.exerciseUnitService = new ExerciseUnitService();

        createExerciseUnit(exercise.getId());

        findViews();
        setupRecyclerView();
        setListeners();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.create_timed_exercise_unit_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TimeBasedSetAdapter(sets);
        recyclerView.setAdapter(adapter);
        adapter.setOnSetLongClickedListener(this);
    }


    private void findViews() {
        timeTV = (TextView) findViewById(R.id.create_timed_exercise_unit_time_tv);
        addSetBtn = (Button) findViewById(R.id.create_timed_exercise_unit_add_set_bt);
        addTimedBtn = (Button) findViewById(R.id.create_timed_exercise_unit_start_time_set_btn);
        finishBtn = (Button) findViewById(R.id.create_timed_exercise_unit_finish_and_save_btn);
        progressBar = (ProgressBar) findViewById(R.id.create_timed_exercise_unit_progress);
        progressBar.setVisibility(View.GONE);
        timeTV.setVisibility(View.GONE);
    }

    private void setListeners() {
        timeTV.setOnClickListener(this);
        addSetBtn.setOnClickListener(this);
        addTimedBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        progressBar.setOnClickListener(this);
    }

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
        if (sets.isEmpty()) {
            Toast.makeText(this, "You have to add a set before saving!", Toast.LENGTH_SHORT).show();
        } else {
            exerciseUnitService.addAbstractSet(this, currenExerciseUnit.getId(), sets, new BackendCallback<ExerciseUnitNode>() {
                @Override
                public void onSuccess(ExerciseUnitNode result, Map<String, String> responseHeader) {
                }

                @Override
                public void onFailure(ErrorResponse error) {

                }

            });
        }
        finish();
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

    private void onTimeSetStarted() {
        if(!started){
            addTimedBtn.setText(R.string.stop_timed_set);
            started = true;
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
                            new AlertDialog.Builder(CreateTimeBasedExerciseUnit.this)
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

    }

    private void addSet(long time) {
        AbstractSet set = new AbstractSet(String.valueOf(time));
        sets.add(set);
        adapter.notifyItemRangeChanged(sets.size()-1, sets.size());
    }


    @Override
    public void onLongClicked(final int position, AbstractSet set) {
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