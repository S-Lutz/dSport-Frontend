package com.omgproduction.dsport_application.fragments.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.CreateEventActivity;
import com.omgproduction.dsport_application.activities.main.CreateExerciseActivity;

/**
 * Created by Strik on 22.03.2017.
 */

public class ExerciseMenuFragment extends MenuFragment implements View.OnClickListener {


    public ExerciseMenuFragment(){
    }

    @Override
    public int getLayout() {
        return R.layout.layout_fragment_floating_menu_exercise;
    }

    @Override
    protected void setMenuButtons(View v) {

    }

    @Override
    protected int getMenuContainer() {
        return R.id.exercise_menu_sub_button_holder;
    }

    @Override
    public void onMenuButtonClicked(View v) {

    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
        Intent intent = new Intent(getContext(), CreateExerciseActivity.class);
        startActivity(intent);
    }

}
