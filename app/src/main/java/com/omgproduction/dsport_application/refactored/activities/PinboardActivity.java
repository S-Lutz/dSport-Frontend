package com.omgproduction.dsport_application.refactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.adapter.PinboardAdapter;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.refactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.models.resultnodes.SocialResultPair;
import com.omgproduction.dsport_application.refactored.services.LikeService;
import com.omgproduction.dsport_application.refactored.services.PinboardService;
import com.omgproduction.dsport_application.refactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class PinboardActivity extends AppCompatActivity implements View.OnClickListener, onSocialItemClickedListener, onDialogFragmentClickListener {

    //TODO ENUM FOR RELATIONSHIP STATE
    //public enum relationship {OWNER, FRIEND, UNKNOWN}

    private Boolean isFabOpen = false;
    private FloatingActionButton main_fab, post_fab, event_fab, exercise_fab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private ImageView profileImageHeader;
    private SwipeRefreshLayout swipeRefreshLayout;

    private UserNode pinnboardOwner;

    private ArrayList<SocialResultPair> socialResult;
    private RecyclerView socialResultRecyclerView;
    private PinboardAdapter pinboardAdapter;

    private PinboardService pinboardService;
    private LikeService likeService;

    private LoadingView loadingView;
    private GeneralDialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pinboard_activity_layout);

        this.pinboardService = new PinboardService();
        this.likeService = new LikeService();

        profileImageHeader = (ImageView) findViewById(R.id.new_profile_pic);
        loadingView = (LoadingView) findViewById(R.id.loading_pinboard_result);
        dialogFragment = new GeneralDialogFragment(this, this , this);

        socialResult = new ArrayList<>();
        pinnboardOwner = (UserNode) getIntent().getSerializableExtra("USER");
        String type = getIntent().getExtras().getString("RELATIONSHIP");

        setPinboardHeader(pinnboardOwner);
        preferFloatingButtons();

        setupRecyclerView();

        loadingView.show();
        getPinboard(pinnboardOwner.getId());

        setUpToolbarTitle();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.new_pinboard_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });

    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setTitle("Pinboard");
    }

    private void updateView() {
        loadingView.show();
        getPinboard(pinnboardOwner.getId());
    }

    private void setupRecyclerView() {
        socialResultRecyclerView = (RecyclerView) findViewById(R.id.socialResultRecyclerView);
        socialResultRecyclerView.setHasFixedSize(true);
        socialResultRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        socialResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setPinboardAdapter() {
        pinboardAdapter = new PinboardAdapter(PinboardActivity.this, socialResult, this);
        socialResultRecyclerView.setAdapter(pinboardAdapter);
    }

    private void getPinboard(Long id) {
        if (!socialResult.isEmpty()) socialResult.clear();
        pinboardService.getPinboard(this, id, new BackendCallback<ArrayList<SocialResultPair>>() {
            @Override
            public void onSuccess(ArrayList<SocialResultPair> result, Map<String, String> responseHeader) {
                socialResult = result;
                setPinboardAdapter();
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }
        });

    }

    private void setPinboardHeader(UserNode user) {
        setText(R.id.username_toolbar, user.getUsername());
        GlideApp
                .with(this)
                .load(user.getPicture())
                .fitCenter()
                .into(profileImageHeader);
    }

    @Override
    public void onBackPressed() {
        if (isFabOpen) {
            animateFAB();
        } else {
            super.onBackPressed();
        }
    }

    private void preferFloatingButtons() {
        main_fab = (FloatingActionButton) findViewById(R.id.main_fab);
        post_fab = (FloatingActionButton) findViewById(R.id.post_fab);
        event_fab = (FloatingActionButton) findViewById(R.id.event_fab);
        exercise_fab = (FloatingActionButton) findViewById(R.id.exercise_fab);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.new_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.new_fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.new_fab_rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.new_fab_rotate_backward);

        main_fab.setOnClickListener(this);
        post_fab.setOnClickListener(this);
        event_fab.setOnClickListener(this);
        exercise_fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_fab:
                animateFAB();
                break;
            case R.id.post_fab:
                startCreatePostActivity(pinnboardOwner.getId());
                break;
            case R.id.event_fab:
                startActivity(new Intent(this, CreateEventActivity.class));
                break;
            case R.id.exercise_fab:

                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            main_fab.startAnimation(rotate_backward);
            post_fab.startAnimation(fab_close);
            event_fab.startAnimation(fab_close);
            exercise_fab.startAnimation(fab_close);
            post_fab.setClickable(false);
            event_fab.setClickable(false);
            exercise_fab.setClickable(false);
            isFabOpen = false;

        } else {

            main_fab.startAnimation(rotate_forward);
            post_fab.startAnimation(fab_open);
            event_fab.startAnimation(fab_open);
            exercise_fab.startAnimation(fab_open);
            post_fab.setClickable(true);
            event_fab.setClickable(true);
            exercise_fab.setClickable(true);
            isFabOpen = true;

        }
    }

    public void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    @Override
    public void onClick(View v, int adapterPosition) {
        SocialResultPair node = socialResult.get(adapterPosition);

        switch (v.getId()) {
            case R.id.post_like_btn:
                likeSocialNode(node);
                adjustIcon(node, adapterPosition);
                break;
            case R.id.post_comment_button:
                startCommentActivity(node.getSocialNode());
                break;
            case R.id.event_like_btn:
                likeSocialNode(node);
                adjustIcon(node, adapterPosition);
                break;
            case R.id.event_comment_button:
                startCommentActivity(node.getSocialNode());
                break;
            case R.id.event_participate_btn:
                participateEvent(node);
                adjustParticipateIcon((EventNode) node.getSocialNode(), adapterPosition);
                break;
        }

    }

    private void adjustIcon(SocialResultPair socialNode, int position) {
        if (socialNode.isLikesSocialNode()) {
            socialNode.setLikesSocialNode(false);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) - 1));
        } else {
            socialNode.setLikesSocialNode(true);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) + 1));
        }

        pinboardAdapter.notifyItemChanged(position);
    }

    private void adjustParticipateIcon(EventNode eventNode, int position) {
        if (eventNode.getParticipating()) {
            eventNode.setParticipating(false);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) - 1));
        } else {
            eventNode.setParticipating(true);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) + 1));
        }

        pinboardAdapter.notifyItemChanged(position);
    }

    private void likeSocialNode(final SocialResultPair socialResultPair) {
        likeService.likeSocialNode(this, socialResultPair.getSocialNode().getId(), new BackendCallback<SocialNode>() {
            @Override
            public void onSuccess(SocialNode result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {
                dialogFragment.createDialog().show();
            }
        });
    }

    private void participateEvent(final SocialResultPair socialResultPair) {
        likeService.participateEvent(this, socialResultPair.getSocialNode().getId(), new BackendCallback<EventNode>() {
            @Override
            public void onSuccess(EventNode result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {
                dialogFragment.createDialog().show();
            }
        });
    }

    private void startCommentActivity(SocialNode socialNode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("SOCIAL_NODE", socialNode);
        startActivity(new Intent(this, CommentActivity.class).putExtras(bundle));
    }

    private void startCreatePostActivity(Long userId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER_ID", userId);
        startActivity(new Intent(this, CreatePostActivity.class).putExtras(bundle));
    }

    @Override
    public void onOkClicked() {
      updateView();
    }

    @Override
    public void onCancelClicked() {
        //do nothing
    }
}
