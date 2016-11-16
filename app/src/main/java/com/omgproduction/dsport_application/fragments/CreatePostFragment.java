package com.omgproduction.dsport_application.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;

public class CreatePostFragment extends Fragment implements View.OnClickListener{

    public CreatePostFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onClick(View view) {
        //TODO Open Create Post Activity with Transactions (Shared Element)
    }
}
