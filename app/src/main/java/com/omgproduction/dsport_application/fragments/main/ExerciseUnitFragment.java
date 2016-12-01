package com.omgproduction.dsport_application.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.supplements.activities.AdvancedFragment;

public class ExerciseUnitFragment extends AdvancedFragment {
    public ExerciseUnitFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_unit, container, false);
    }

    @Override
    public void onRefresh() {

    }
}
