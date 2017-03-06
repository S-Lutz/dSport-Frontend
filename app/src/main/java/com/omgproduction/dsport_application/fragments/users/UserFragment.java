package com.omgproduction.dsport_application.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragment;

/**
 * Created by Florian on 22.12.2016.
 */

public class UserFragment extends AbstractFragment {
    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_exercise_unit, container, false);
    }

    @Override
    public void onRefresh() {

    }
}