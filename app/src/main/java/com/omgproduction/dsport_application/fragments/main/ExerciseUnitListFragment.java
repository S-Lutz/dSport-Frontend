package com.omgproduction.dsport_application.fragments.main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.fragments.helper.UniversalListFragment;

import java.util.List;

public class ExerciseUnitListFragment extends UniversalListFragment {
    public ExerciseUnitListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_exercise_unit, container, false);
    }

    @Override
    public RecyclerView.Adapter getAdapter(List values) {
        return null;
    }

    @Override
    protected void updatePrivate() {

    }

    @Override
    protected void updateGlobal() {

    }

    @Override
    public void onRefresh() {

    }
}
