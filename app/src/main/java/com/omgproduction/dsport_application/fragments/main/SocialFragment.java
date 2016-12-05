package com.omgproduction.dsport_application.fragments.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.PostDetailActivity;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
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
import com.omgproduction.dsport_application.utils.Converter;

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

        update();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void update() {
        UserController.getInstance().getLocalUser(getContext(), new OnResultAdapter<User>() {
            @Override
            public void onStartQuery() {
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
    public void onPostClicked(final PostAdapter.PostViewHolder holder, Post p){
        Intent i = new Intent(getContext(), PostDetailActivity.class);
        i.putExtra(ApplicationKeys.POSTS, p);
        Pair<View, String> p1 = Pair.create((View) holder.getIv_picture(), "user_picture");
        Pair<View, String> p2 = Pair.create((View) holder.getTv_username(), "post_username");
        Pair<View, String> p3 = Pair.create((View) holder.getIv_post_picture_overlay(), "post_picture_overlay");
        Pair<View, String> p4 = Pair.create((View) holder.getTv_title(), "post_title");
        Pair<View, String> p5 = Pair.create((View) holder.getTv_text(), "post_text");
        Pair<View, String> p6 = Pair.create((View) holder.getPost_buttons(), "post_buttons");
        Pair<View, String> p7 = Pair.create((View) holder.getPost_layout(), "post_layout");
        Pair<View, String> p8 = Pair.create((View) holder.getTv_date(), "post_date");
        ActivityOptionsCompat options;

        options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2,p3,p4,p5,p6,p7,p8);

        startActivity(i, options.toBundle());
    }

    @Override
    public void onPostLike(PostAdapter.PostViewHolder holder, final Post p) {
        Log.e("POST","Like");
        UserController.getInstance().getLocalUserID(getContext(), new OnResultAdapter<String>(){
            @Override
            public void onSuccess(String result) {
                PostController.getInstance().likePost(result, p.getPost_id(), new OnResultAdapter<Void>(){
                    @Override
                    public void onStartQuery() {
                        super.onStartQuery();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        super.onSuccess(result);
                    }

                    @Override
                    public void onConnectionError(VolleyError e) {
                        super.onConnectionError(e);
                    }

                    @Override
                    public void onBackendError(String errorCode) {
                        super.onBackendError(errorCode);
                    }

                    @Override
                    public void onJSONException(JSONException e) {
                        super.onJSONException(e);
                    }

                    @Override
                    public void onUserNotFound() {
                        super.onUserNotFound();
                    }

                    @Override
                    public void onFinishQuery() {
                        super.onFinishQuery();
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
    public void onPostShare(PostAdapter.PostViewHolder holder, Post p) {
        Log.e("POST","Share");
    }

    @Override
    public void onPostComment(PostAdapter.PostViewHolder holder, Post p) {
        //Log.e("POST","Comment");
        onPostClicked(holder, p);
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void onStartQuery() {
        refresher.setRefreshing(true);
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
        SessionController.getInstance().logout(getContext());
    }
    @Override
    public void onFinishQuery() {
        refresher.setRefreshing(false);
    }

}
