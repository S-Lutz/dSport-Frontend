package com.omgproduction.dsport_application.refactored.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.activities.EventsActivity;
import com.omgproduction.dsport_application.refactored.activities.ExerciseUnitsActivity;
import com.omgproduction.dsport_application.refactored.activities.ExercisesActivity;
import com.omgproduction.dsport_application.refactored.activities.LoginActivity;
import com.omgproduction.dsport_application.refactored.activities.PinboardActivity;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.services.PreferencesService;

public class MoreFragment extends Fragment implements View.OnClickListener {

    RelativeLayout pinboard;
    RelativeLayout events;
    RelativeLayout exercises;
    RelativeLayout exerciseUnits;
    RelativeLayout logout;


    public MoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_bar_more_fragment, container, false);

        pinboard = (RelativeLayout) rootView.findViewById(R.id.layout1);
        events = (RelativeLayout) rootView.findViewById(R.id.layout2);
        exercises = (RelativeLayout) rootView.findViewById(R.id.layout3);
        exerciseUnits = (RelativeLayout) rootView.findViewById(R.id.layout4);
        logout = (RelativeLayout) rootView.findViewById(R.id.layout5);

        setListeners();

        return rootView;
    }

    private void setListeners() {
        pinboard.setOnClickListener(this);
        events.setOnClickListener(this);
        exercises.setOnClickListener(this);
        exerciseUnits.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                startMyPinboardActivity(new Gson().
                        fromJson(PreferencesService.getSharedPreferencesUser(getContext()), UserNode.class), "OWNER");
                break;
            case R.id.layout2:
                startMyEventsActivity(new Gson().
                        fromJson(PreferencesService.getSharedPreferencesUser(getContext()), UserNode.class));
                break;
            case R.id.layout3:
                startExerciseActivity(new Gson().
                        fromJson(PreferencesService.getSharedPreferencesUser(getContext()), UserNode.class));
                break;
            case R.id.layout4:
                startExerciseUnitsActivity(new Gson().
                        fromJson(PreferencesService.getSharedPreferencesUser(getContext()), UserNode.class));
                break;
            case R.id.layout5:
                SharedPreferences preferences = PreferencesService.getSharedPreferences(getContext());
                preferences.edit().clear().apply();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
                break;
        }
    }

    private void startMyPinboardActivity(UserNode userNode, String relationship) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", userNode);
        bundle.putSerializable("RELATIONSHIP", relationship);
        startActivity(new Intent(getContext(), PinboardActivity.class).putExtras(bundle));
    }

    private void startMyEventsActivity(UserNode userNode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", userNode);
        startActivity(new Intent(getContext(), EventsActivity.class).putExtras(bundle));
    }

    private void startExerciseActivity(UserNode userNode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", userNode);
        startActivity(new Intent(getContext(), ExercisesActivity.class).putExtras(bundle));
    }

    private void startExerciseUnitsActivity(UserNode userNode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", userNode);
        startActivity(new Intent(getContext(), ExerciseUnitsActivity.class).putExtras(bundle));
    }
}