package com.omgproduction.dsport_application.fragments.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.ExerciseUnitAdapter;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.fragments.helper.ExerciseMenuFragment;
import com.omgproduction.dsport_application.fragments.helper.UniversalListFragment;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.ExerciseUnit;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.ExerciseService;

import java.util.ArrayList;
import java.util.List;

public class ExerciseUnitListFragment extends UniversalListFragment<ExerciseUnit, ExerciseUnitAdapter> {


    private ExerciseService exerciseService;
    private ExerciseMenuFragment exerciseMenuFragment;

    public ExerciseUnitListFragment() {
        exerciseService = new ExerciseService(getContext());
        exerciseMenuFragment = new ExerciseMenuFragment();

    }

    @Override
    protected void updatePrivate() {

    }

    @Override
    protected void updateGlobal() {
        User localUser = getLocalUser();
        exerciseService.getAllExerciseUnits(localUser.getId(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_fragment_exercise_unit, container, false);
        setRefresher((SwipeRefreshLayout) view.findViewById(R.id.exerciseUnit_refresher));
        update();

        return view;
    }

    @Override
    public ExerciseUnitAdapter getAdapter(List<ExerciseUnit> values) {
        ExerciseUnitAdapter adapter = new ExerciseUnitAdapter(values);
        return adapter;
    }

    @Override
    public void onSetActive(boolean flag) {
        menuManager.setMenuFragment(flag?exerciseMenuFragment:null);
    }


}
