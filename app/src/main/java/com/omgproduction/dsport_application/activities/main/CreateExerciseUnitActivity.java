package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ExerciseUnitKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.Exercise;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.ExerciseService;
import com.omgproduction.dsport_application.supplements.activities.AbstractAppCompatActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateExerciseUnitActivity extends AbstractAppCompatActivity implements ExerciseUnitKeys{

    private Spinner spinner;
    private TextView userNameTV;
    private ImageView img;
    private FloatingActionButton addFab;
    private Button newExerciseButton;
    private List<Exercise> exercises;

    private static final int NO_TYPE = -1;

    private ExerciseService exerciseService;
    private ArrayAdapter<Exercise> adapter;

    private Exercise currentExercise;

    @Override
    protected void onStart() {
        super.onStart();
        fillSpinner();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise_unit);
        exerciseService = new ExerciseService(this);

        spinner = (Spinner) findViewById(R.id.exercise_unit_type_spinner);
        userNameTV = (TextView) findViewById(R.id.create_exercise_unit_username);
        img = (ImageView) findViewById(R.id.create_exercise_unit_picture);
        addFab = (FloatingActionButton) findViewById(R.id.create_exercise_unit_button);
        newExerciseButton = (Button) findViewById(R.id.create_exercise_unit_create_exercise_button);


        User localUser = getLocalUser();
        userNameTV.setText(localUser.getUsername());
        img.setImageBitmap(BitmapUtils.getBitmapFromString(localUser.getPicture()));

        addFab.setOnClickListener(this);

        newExerciseButton.setOnClickListener(this);
        addFab.setEnabled(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentExercise = exercises.get(position);
                if(currentExercise.getType() == NO_TYPE){
                    addFab.setEnabled(false);
                }else {
                    addFab.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fillSpinner();

    }

    @Override
    protected void removeAllErrors() {

    }

    private void fillSpinner() {

        User localUser = getLocalUser();

        exerciseService.getAllExercises(localUser.getId(), new RequestFuture<List<Exercise>>(){
            @Override
            public void onStartQuery() {
                exercises = new ArrayList<>();
                exercises.add(new Exercise("","Loading content", NO_TYPE));
                fillSpinner(exercises);
            }

            @Override
            public void onSuccess(List<Exercise> exercises) {
                if(exercises!=null){
                    fillSpinner(exercises);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                printError(R.id.activity_create_exercise_unit,errorMessage);
            }

            @Override
            public void onFinishQuery() {
                super.onFinishQuery();
            }
        });
    }

    private void fillSpinner(final List<Exercise> exercises) {
        this.exercises = exercises;
        adapter = new ArrayAdapter<>(CreateExerciseUnitActivity.this,android.R.layout.simple_list_item_1,exercises);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_exercise_unit_create_exercise_button:
                Intent i = new Intent(this, CreateExerciseActivity.class);
                startActivity(i);
                break;
            case R.id.create_exercise_unit_button:
                startExerciseUnit();
                break;
        }
    }

    private void startExerciseUnit() {
        Intent i;
        switch (currentExercise.getType()){
            case 0: i = new Intent(this, CreateWeightExerciseUnitActivity.class);
                i.putExtra(EXERCISE, currentExercise);
                startActivity(i);
                break;
            case 1: i = new Intent(this, RouteTrackingActivity.class);
                i.putExtra(EXERCISE, currentExercise);
                startActivity(i);
                break;
            case 2: i = new Intent(this, CreateTimedExerciseUnitActivity.class);
                i.putExtra(EXERCISE, currentExercise);
                startActivity(i);
                break;
        }
    }
}
