package com.omgproduction.dsport_application.aaRefactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.ExerciseNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.services.ExerciseService;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateExerciseUnitActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private List<ExerciseNode> exercises;

    private UserNode exerciseOwner;

    private ExerciseService exerciseService;
    private LoadingView loadingView;

    private ExerciseNode currentExercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_create_exercise_unit);
        exerciseOwner = (UserNode) getIntent().getSerializableExtra("USER");

        exerciseService = new ExerciseService();

        exercises = new ArrayList<>();

        loadingView = (LoadingView) findViewById(R.id.loading_exercise_units_result);
        spinner = (Spinner) findViewById(R.id.exercise_unit_type_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentExercise = exercises.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CreateExerciseUnitActivity.this, "No Exercise Selected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.create_exercise_unit_create_exercise_button).setOnClickListener(this);
        findViewById(R.id.create_exercise_unit_button).setOnClickListener(this);

        getExercises(exerciseOwner.getId());
    }

    private void getExercises(Long id) {
        if (!exercises.isEmpty()) exercises.clear();
        exerciseService.getExercises(this, id, new BackendCallback<ArrayList<ExerciseNode>>() {
            @Override
            public void onSuccess(ArrayList<ExerciseNode> result, Map<String, String> responseHeader) {
                exercises = result;

                fillSpinner(exercises);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
            }

        });

    }

    private void fillSpinner(List<ExerciseNode> exercises) {
        this.exercises = exercises;
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exercises));
        loadingView.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_exercise_unit_create_exercise_button:
                startActivity(new Intent(this, CreateExerciseActivity.class));
                break;
            case R.id.create_exercise_unit_button:
                //TODO enum
                switch(currentExercise.getSetType()){
                    case "REPEAT":
                        Log.e("EXERCISE", String.valueOf(currentExercise.getId()));
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("EXERCISE", this.currentExercise);
                        startActivity(new Intent(this, CreateRepeatBasedExerciseUnit.class).putExtras(bundle1));
                        break;
                    case "DISTANCE":
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("EXERCISE", currentExercise);
                        startActivity(new Intent(this, CreateDistanceBasedActivity.class).putExtras(bundle2));
                        break;
                    case "TIME":
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("EXERCISE", currentExercise);
                        startActivity(new Intent(this, CreateTimeBasedExerciseUnit.class).putExtras(bundle3));
                        break;
                }
                break;
        }

    }


}