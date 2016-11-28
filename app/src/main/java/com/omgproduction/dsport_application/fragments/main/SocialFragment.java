package com.omgproduction.dsport_application.fragments.main;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.config.NotificationKeys;
import com.omgproduction.dsport_application.controller.PostController;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.NotificationReceiver;
import com.omgproduction.dsport_application.supplements.activities.AdvancedFragment;

import org.json.JSONException;

import java.util.ArrayList;



public class SocialFragment extends AdvancedFragment implements SwipeRefreshLayout.OnRefreshListener, PostAdapter.OnPostClickedListener, OnResultListener<ArrayList<Post>>{

    private RecyclerView postsRecyler;
    private PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresher;
    private Filter filter = Filter.ALL;

    public enum Filter{
        PRIVATE,
        ALL
    }



    public SocialFragment() {
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
                switch (filter){
                    case ALL:
                        NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(NotificationKeys.NEW_POSTS,NotificationReceiver.NEW_POSTS_ID);
                        PostController.getInstance().getAllPosts(getContext(), user.getId(), SocialFragment.this);
                        break;
                    case PRIVATE:
                        PostController.getInstance().getPinboard(getContext(), user.getId(), user.getId(), SocialFragment.this);
                        break;
                }

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

    @Override
    public void onPostClicked(Post p) {
        //TODO Detail View
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    @Override
    public void onSuccess(ArrayList<Post> posts) {
        postsRecyler = (RecyclerView) getView().findViewById(R.id.social_posts_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        postsRecyler.setLayoutManager(layoutManager);
        postsRecyler.setHasFixedSize(true);
        adapter = new PostAdapter(posts);
        adapter.addOnPostClickedListener(SocialFragment.this);
        postsRecyler.setAdapter(adapter);
    }

    @Override
    public void onConnectionError(VolleyError e) {
        e.printStackTrace();
        printError(getView(), getView().findViewById(getView().getId()), "e100");
    }

    @Override
    public void onBackendError(String errorCode) {
        printError(getView(), getView().findViewById(getView().getId()), errorCode);
    }

    @Override
    public void onJSONException(JSONException e) {
        e.printStackTrace();
        printError(getView(), getView().findViewById(getView().getId()), "e0");
    }

    @Override
    public void onUserNotFound() {

    }
    @Override
    public void onFinish() {
        refresher.setRefreshing(false);
    }

}
