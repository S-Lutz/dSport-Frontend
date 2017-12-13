package com.omgproduction.dsport_application.aaRefactored.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.aaRefactored.services.ExerciseService;
import com.omgproduction.dsport_application.aaRefactored.views.CheckedEditText;

import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity implements View.OnClickListener {
    private ExerciseService exerciseService;
    private Spinner spinner = null;

    public enum SetType{
        REPEAT,
        DISTANCE,
        TIME
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout_activity_create_exercise);

        exerciseService = new ExerciseService();

        prepareSpinner();

        findViewById(R.id.create_exercise_button).setOnClickListener(this);

        setUpToolbarTitle();
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create a new exercise");
    }

    private void prepareSpinner() {
        spinner = (Spinner) findViewById(R.id.exercise_type_spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SetType.values()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_exercise_button:
                createExercise();
                break;
        }
    }

    private void createExercise() {
        final String selectedType = spinner.getSelectedItem().toString();

        final CheckedEditText exerciseName = ((CheckedEditText)findViewById(R.id.create_new_exercise));
        if (!exerciseName.checkRequired()) return;

        exerciseService.createExercise(this,new ExerciseNode(exerciseName.getTextString(), selectedType), new BackendCallback<ExerciseNode>() {
            @Override
            public void onSuccess(ExerciseNode result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }

        });

        finish();
    }
}
