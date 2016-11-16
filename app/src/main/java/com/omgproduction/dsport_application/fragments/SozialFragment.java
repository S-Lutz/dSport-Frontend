package com.omgproduction.dsport_application.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.User;


public class SozialFragment extends Fragment {

    public SozialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sozial, container, false);

        User user = User.getInstance();

        ((ImageView)view.findViewById(R.id.fragment_create_post_image)).setImageBitmap(user.getBitmap(getContext()));

        // Inflate the layout for this fragment
        return view;
    }
}
