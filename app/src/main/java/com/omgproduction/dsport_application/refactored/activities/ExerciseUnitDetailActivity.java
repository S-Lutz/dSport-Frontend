package com.omgproduction.dsport_application.refactored.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.adapter.ExerciseUnitDetailAdapter;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.ExerciseUnitNode;
import com.omgproduction.dsport_application.refactored.models.nodes.sets.AbstractSet;
import com.omgproduction.dsport_application.refactored.services.ExerciseUnitService;

import java.util.ArrayList;
import java.util.Map;

public class ExerciseUnitDetailActivity extends AppCompatActivity {

    private ExerciseUnitNode currentExerciseUnit;
    private RecyclerView exerciseDetailRecyclerView;
    private ExerciseUnitService exerciseUnitService;
    private ExerciseUnitDetailAdapter exerciseUnitDetailAdapter;
    private ArrayList<AbstractSet> abstractSets;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentExerciseUnit = (ExerciseUnitNode) getIntent().getSerializableExtra("EXERCISEUNIT");
        setContentView(R.layout.new_activity_layout_exercise_unit_detail);

        exerciseUnitService = new ExerciseUnitService();
        setUpRecylerView();

        getExerciseDetail();
    }

    private void setUpRecylerView() {
        exerciseDetailRecyclerView = (RecyclerView) findViewById(R.id.exercise_unit_detail_recycler_view);
        exerciseDetailRecyclerView.setHasFixedSize(true);
        exerciseDetailRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exerciseDetailRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setDetailAdapter(){
        exerciseUnitDetailAdapter = new ExerciseUnitDetailAdapter(this, abstractSets);
        exerciseDetailRecyclerView.setAdapter(exerciseUnitDetailAdapter);
    }

    private void getExerciseDetail(){
        exerciseUnitService.getExerciseUnitDetail(this, currentExerciseUnit.getId(), new BackendCallback<ArrayList<AbstractSet>>() {
            @Override
            public void onSuccess(ArrayList<AbstractSet> result, Map<String, String> responseHeader) {
                abstractSets = result;

                setDetailAdapter();
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });

    }

}