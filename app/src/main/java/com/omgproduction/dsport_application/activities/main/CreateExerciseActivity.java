package com.omgproduction.dsport_application.activities.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.ExerciseService;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

/**
 * Created by Strik on 22.03.2017.
 */

public class CreateExerciseActivity extends AbstractFragmentActivity{
    private Spinner spinner = null;

    private ExerciseService exerciseService;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exerciseService = new ExerciseService(this);

        prepareSpinner();

        User user = getLocalUser();
        setPic(R.id.create_exercise_picture, BitmapUtils.getBitmapFromString(user.getPicture()));
        setText(R.id.create_exercise_username, user.getUsername());

        findViewById(R.id.create_exercise_button).setOnClickListener(this);

    }

    private void prepareSpinner() {
        spinner = (Spinner) findViewById(R.id.exercise_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.exercise_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public int getLayout() {
        return R.layout.layout_activity_create_exercise;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_exercise_button:
                saveExercise();
                break;
        }

    }

    private void saveExercise() {
        final int selectedType = spinner.getSelectedItemPosition();
        final String exerciseName = ((EditText)findViewById(R.id.create_new_exercise)).getText().toString();

        if(exerciseName.trim().isEmpty()){
            Toast.makeText(this, "You did not enter an exercise names", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userService.getLocalUser();

        exerciseService.saveExercise(user.getId(), exerciseName, selectedType, new IRequestFuture<Void>() {
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Void result) {
                CreateExerciseActivity.super.onBackPressed();
                CreateExerciseActivity.this.finish();
            }

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                printError(R.id.activity_create_exercise, errorMessage, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveExercise();
                    }
                });
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });


    }
}
