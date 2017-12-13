package com.omgproduction.dsport_application.aaRefactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.ExerciseAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.services.ExerciseService;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class ExercisesActivity extends AppCompatActivity implements View.OnClickListener, onDialogFragmentClickListener {

    private ArrayList<ExerciseNode> exerciseNodes;

    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter exerciseAdapter;

    private ExerciseService exerciseService;
    private LoadingView loadingView;
    private UserNode exerciseOwner;
    private FloatingActionButton newExerciseFab;
    private GeneralDialogFragment dialogFragment;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_exercises_layout);

        this.exerciseService = new ExerciseService();
        exerciseOwner = (UserNode) getIntent().getSerializableExtra("USER");

        newExerciseFab = (FloatingActionButton) findViewById(R.id.create_exercise_fab);
        loadingView = (LoadingView) findViewById(R.id.loading_exercises_result);
        dialogFragment = new GeneralDialogFragment(this, this, this);

        exerciseNodes = new ArrayList<>();

        loadingView.show();
        setupRecyclerView();
        getExercises(exerciseOwner.getId());
        newExerciseFab.setOnClickListener(this);

        setUpToolbarTitle();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.exercises_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setTitle("Exercises");
    }

    private void updateView() {
        loadingView.show();
        getExercises(exerciseOwner.getId());
    }

    private void setupRecyclerView() {
        exerciseRecyclerView = (RecyclerView) findViewById(R.id.exercise_recycler_view);
        exerciseRecyclerView.setHasFixedSize(true);
        exerciseRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setEventAdapter() {
        exerciseAdapter = new ExerciseAdapter(ExercisesActivity.this, exerciseNodes);
        exerciseRecyclerView.setAdapter(exerciseAdapter);
    }

    private void getExercises(Long id) {
        if (!exerciseNodes.isEmpty()) exerciseNodes.clear();
        exerciseService.getExercises(this, id, new BackendCallback<ArrayList<ExerciseNode>>() {
            @Override
            public void onSuccess(ArrayList<ExerciseNode> result, Map<String, String> responseHeader) {
                exerciseNodes = result;
                setEventAdapter();
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }

        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_exercise_fab:
                startCreateExerciseActivity();
        }
    }

    private void startCreateExerciseActivity() {
        startActivity(new Intent(this, CreateExerciseActivity.class));
    }


    @Override
    public void onOkClicked() {
        updateView();
    }

    @Override
    public void onCancelClicked() {

    }
}
