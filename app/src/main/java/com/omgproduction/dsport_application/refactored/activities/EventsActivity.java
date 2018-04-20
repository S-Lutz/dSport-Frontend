package com.omgproduction.dsport_application.refactored.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.adapter.EventAdapter;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.refactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.refactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.models.resultnodes.SocialResultPair;
import com.omgproduction.dsport_application.refactored.services.EventService;
import com.omgproduction.dsport_application.refactored.services.LikeService;
import com.omgproduction.dsport_application.refactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener, onSocialItemClickedListener, onDialogFragmentClickListener {

    private ArrayList<SocialResultPair> socialResult;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private EventService eventService;
    private LikeService likeService;
    private LoadingView loadingView;
    private UserNode eventsOwner;
    private GeneralDialogFragment dialogFragment;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_layout);

        eventsOwner = (UserNode) getIntent().getSerializableExtra("USER");

        loadingView = (LoadingView) findViewById(R.id.loading_events_result);
        dialogFragment = new GeneralDialogFragment(this, this, this);
        socialResult = new ArrayList<>();

        this.eventService = new EventService();
        this.likeService = new LikeService();

        findViewById(R.id.create_event_fab).setOnClickListener(this);

        setupRecyclerView();

        loadingView.show();
        getEvents(eventsOwner.getId());

        setUpToolbarTitle();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.event_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });
    }

    private void setUpToolbarTitle() {
        getSupportActionBar().setTitle("Events");
    }

    private void updateView() {
        loadingView.show();
        getEvents(eventsOwner.getId());
    }

    private void setupRecyclerView() {
        eventRecyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setEventAdapter() {
        eventAdapter = new EventAdapter(EventsActivity.this, socialResult, this);
        eventRecyclerView.setAdapter(eventAdapter);
    }

    private void getEvents(Long id) {
        if (!socialResult.isEmpty()) socialResult.clear();
        eventService.getEvents(this, id, new BackendCallback<ArrayList<SocialResultPair>>() {
            @Override
            public void onSuccess(ArrayList<SocialResultPair> result, Map<String, String> responseHeader) {
                socialResult = result;
                setEventAdapter();
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_event_fab:
                startActivity(new Intent(this, CreateEventActivity.class));
        }
    }

    @Override
    public void onClick(View v, int adapterPosition) {
        SocialResultPair node = socialResult.get(adapterPosition);

        switch (v.getId()) {
            case R.id.event_like_btn:
                likeSocialNode(node);
                adjustLikeIcon(node, adapterPosition);
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

    private void adjustLikeIcon(SocialResultPair socialNode, int position) {
        if (socialNode.isLikesSocialNode()) {
            socialNode.setLikesSocialNode(false);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) - 1));
        } else {
            socialNode.setLikesSocialNode(true);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount()) + 1));
        }

        eventAdapter.notifyItemChanged(position);
    }

    private void adjustParticipateIcon(EventNode eventNode, int position) {
        if (eventNode.getParticipating()) {
            eventNode.setParticipating(false);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) - 1));
        } else {
            eventNode.setParticipating(true);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) + 1));
        }

        eventAdapter.notifyItemChanged(position);
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


    @Override
    public void onOkClicked() {
        updateView();
    }

    @Override
    public void onCancelClicked() {
        //do nothing
    }
}