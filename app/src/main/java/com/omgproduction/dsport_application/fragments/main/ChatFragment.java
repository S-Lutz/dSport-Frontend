package com.omgproduction.dsport_application.fragments.main;

import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.fragments.helper.UniversalListFragment;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragment;

import java.util.List;

public class ChatFragment extends UniversalListFragment<User, PostAdapter> {
    public ChatFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_fragment_chat, container, false);
    }

    @Override
    public PostAdapter getAdapter(List<User> values) {
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
