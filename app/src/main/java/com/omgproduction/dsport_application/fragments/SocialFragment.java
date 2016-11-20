package com.omgproduction.dsport_application.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.controller.PostController;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AdvancedFragment;

import org.json.JSONException;

import java.util.ArrayList;


public class SocialFragment extends AdvancedFragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView postsRecyler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresher;


    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_social, container, false);
        refresher = (SwipeRefreshLayout) view.findViewById(R.id.social_refresher);
        refresher.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        UserController.getInstance().getLocalUser(getContext(), new OnResultAdapter<User>() {
            @Override
            public void onStart() {
                refresher.setRefreshing(true);
            }

            @Override
            public void onSuccess(User user) {

                PostController.getInstance().getAllPosts(getContext(), user.getId(), user.getId(), new OnResultAdapter<ArrayList<Post>>() {
                    @Override
                    public void onSuccess(ArrayList<Post> posts) {
                        postsRecyler = (RecyclerView) getView().findViewById(R.id.social_posts_recycler);
                        layoutManager = new LinearLayoutManager(getContext());
                        postsRecyler.setLayoutManager(layoutManager);
                        postsRecyler.setHasFixedSize(true);
                        adapter = new PostAdapter(posts);
                        postsRecyler.setAdapter(adapter);
                    }

                    @Override
                    public void onConnectionError(VolleyError e) {
                        e.printStackTrace();
                        printError(getView(), getView().findViewById(R.id.viewPager), "e100");
                    }

                    @Override
                    public void onBackendError(String errorCode) {
                        printError(getView(), getView().findViewById(R.id.viewPager), errorCode);
                    }

                    @Override
                    public void onJSONException(JSONException e) {
                        e.printStackTrace();
                        printError(getView(), getView().findViewById(R.id.viewPager), "e0");
                    }

                    @Override
                    public void onFinish() {
                        refresher.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(getContext());
            }
        });


    }

    @Override
    public void onRefresh() {
        update();
    }
}
