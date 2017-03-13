package com.omgproduction.dsport_application.fragments.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.PostDetailActivity;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.config.NotificationKeys;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Post;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.NotificationReceiver;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragment;

import java.util.List;


public class SocialFragment extends AbstractFragment implements PostAdapter.OnPostClickedListener, IRequestFuture<List<Post>> {

    private RecyclerView postsRecyler;
    private PostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Filter filter = Filter.ALL;

    private PostService postService;
    private SearchUser owner;

    public enum Filter{
        PRIVATE,
        ALL
    }



    public SocialFragment() {
        postService = new PostService(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_fragment_social, container, false);
        setRefresher((SwipeRefreshLayout) view.findViewById(R.id.social_refresher));
        update();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void update() {

        User user = getLocalUser();

        switch (filter){
            case ALL:
                NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NotificationKeys.NOTIFICATION_NEW_POSTS_KEY,NotificationReceiver.NEW_POSTS_ID);
                postService.getAllPosts(user.getId(), SocialFragment.this);
                break;
            case PRIVATE:
                postService.getPinboard(user.getId(), (owner==null?user.getId():owner.getId()), SocialFragment.this);
                break;
        }

    }

    @Override
    public void onRefresh() {
        update();
    }

    @Override
    public void onPostClicked(final PostAdapter.PostViewHolder holder, Post p){
        Intent i = new Intent(getContext(), PostDetailActivity.class);
        i.putExtra(INTENT_POST, p);
        Pair<View, String> p1 = Pair.create((View) holder.getIv_picture(), getString(R.string.transition_user_picture));
        Pair<View, String> p2 = Pair.create((View) holder.getTv_username(), getString(R.string.transition_post_username));
        Pair<View, String> p3 = Pair.create((View) holder.getIv_post_picture_overlay(), getString(R.string.transition_post_picture_overlay));
        Pair<View, String> p4 = Pair.create((View) holder.getTv_title(), getString(R.string.transition_post_title));
        Pair<View, String> p5 = Pair.create((View) holder.getTv_text(), getString(R.string.transition_post_text));
        Pair<View, String> p6 = Pair.create((View) holder.getPost_buttons(), getString(R.string.transition_post_buttons));
        Pair<View, String> p8 = Pair.create((View) holder.getTv_date(), getString(R.string.transition_post_date));
        ActivityOptionsCompat options;

        options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2,p3,p4,p5,p6,p8);

        startActivity(i, options.toBundle());
    }

    @Override
    public void onPostLike(final PostAdapter.PostViewHolder holder, final Post p) {

        User user = getLocalUser();
        postService.likePost(user.getId(), p.getPost_id(), new RequestFuture<LikeResult>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(LikeResult result) {
                p.setLiked(result.isLiked());
                p.setLikeCount(result.getLikeCount());
                holder.getTv_likes().setText(p.getLikeString(getContext()));
            }

            @Override
            public void onFailure(String errorCode) {
                printError(getView(), getView().findViewById(getView().getId()), errorCode);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    @Override
    public void onPostShare(PostAdapter.PostViewHolder holder, Post p) {

    }

    @Override
    public void onPostComment(PostAdapter.PostViewHolder holder, Post p) {
        onPostClicked(holder, p);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void onStartQuery() {
        showProgressBar(true);
    }

    @Override
    public void onSuccess(List<Post> posts) {
        postsRecyler = (RecyclerView) getView().findViewById(R.id.social_posts_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        postsRecyler.setLayoutManager(layoutManager);
        postsRecyler.setHasFixedSize(true);
        adapter = new PostAdapter(posts);
        adapter.addOnPostClickedListener(SocialFragment.this);
        postsRecyler.setAdapter(adapter);
    }

    @Override
    public void onFailure(String errorCode) {
        Log.e("FAILURE", errorCode);
        if(getView()!=null){
            printError(getView(), getView(), errorCode);
        }
    }

    @Override
    public void onFinishQuery() {
        showProgressBar(false);
    }


    public void setOwner(SearchUser owner) {
        this.owner = owner;
    }
}
