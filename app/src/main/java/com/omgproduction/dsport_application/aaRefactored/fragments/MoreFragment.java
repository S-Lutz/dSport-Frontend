package com.omgproduction.dsport_application.aaRefactored.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.activities.LoginActivity;
import com.omgproduction.dsport_application.aaRefactored.services.PreferencesService;

public class MoreFragment extends Fragment{


    public MoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_more_fragment, container, false);

        RelativeLayout relativeLayout= (RelativeLayout) rootView.findViewById(R.id.layout5);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferencesService.getSharedPreferences(getContext());
                preferences.edit().clear().apply();
                Intent i = new Intent(getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
            }
        });

        return rootView;
    }
}