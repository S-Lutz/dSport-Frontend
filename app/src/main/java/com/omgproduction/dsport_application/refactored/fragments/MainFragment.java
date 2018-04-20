package com.omgproduction.dsport_application.refactored.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.activities.CommentActivity;
import com.omgproduction.dsport_application.refactored.adapter.NewsFeedApdater;
import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.refactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.refactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.refactored.interfaces.onSocialItemClickedListener;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.refactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.models.resultnodes.SocialResultPair;
import com.omgproduction.dsport_application.refactored.services.LikeService;
import com.omgproduction.dsport_application.refactored.services.NewsFeedService;
import com.omgproduction.dsport_application.refactored.services.PreferencesService;
import com.omgproduction.dsport_application.refactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class MainFragment extends Fragment implements onSocialItemClickedListener, onDialogFragmentClickListener {

    private View rootView;

    private ArrayList<SocialResultPair> socialResult;
    private RecyclerView socialResultRecyclerView;
    private NewsFeedApdater newsFeedApdapter;

    private LikeService likeService;
    private NewsFeedService newsFeedService;
    private UserNode currenUser;

    private LoadingView loadingView;
    private GeneralDialogFragment dialogFragment;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.likeService = new LikeService();
        this.newsFeedService = new NewsFeedService();
        socialResult = new ArrayList<>();
        currenUser =  new Gson().fromJson(PreferencesService.getSharedPreferencesUser(getContext()), UserNode.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_bar_news_feed_layout, container, false);

        loadingView = (LoadingView) rootView.findViewById(R.id.loading_news_feed_result);
        dialogFragment = new GeneralDialogFragment(getContext(), getActivity(), this);
        setupRecyclerView();

        loadingView.show();
        getNewsFeed(currenUser.getId());

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.new_news_feed_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });

        return rootView;
    }

    private void updateView() {
        loadingView.show();
        getNewsFeed(currenUser.getId());
    }

    private void setupRecyclerView() {
        socialResultRecyclerView = (RecyclerView) rootView.findViewById(R.id.newsFeedRecyclerView);
        socialResultRecyclerView.setHasFixedSize(true);
        socialResultRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        socialResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setNewsFeedAdapter() {
        newsFeedApdapter = new NewsFeedApdater(getActivity(), socialResult, this);
        socialResultRecyclerView.setAdapter(newsFeedApdapter);
    }

    private void getNewsFeed(Long id){
        if (!socialResult.isEmpty()) socialResult.clear();
        newsFeedService.getNewsFeed(getContext(), id, new BackendCallback<ArrayList<SocialResultPair>>() {
            @Override
            public void onSuccess(ArrayList<SocialResultPair> result, Map<String, String> responseHeader) {
                socialResult = result;
                setNewsFeedAdapter();
                loadingView.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }
        });

    }

    private void adjustIcon(SocialResultPair socialNode, int position) {
        if (socialNode.isLikesSocialNode()){
            socialNode.setLikesSocialNode(false);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount())-1));
        }
        else {
            socialNode.setLikesSocialNode(true);
            socialNode.getSocialNode().setLikeCount(String.valueOf(Integer.valueOf(socialNode.getSocialNode().getLikeCount())+1));
        }

        newsFeedApdapter.notifyItemChanged(position);
    }

    private void adjustParticipateIcon(EventNode eventNode, int position) {
        if (eventNode.getParticipating()) {
            eventNode.setParticipating(false);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) - 1));
        } else {
            eventNode.setParticipating(true);
            eventNode.setParticipates(String.valueOf(Integer.valueOf(eventNode.getParticipates()) + 1));
        }

        newsFeedApdapter.notifyItemChanged(position);
    }

    private void likeSocialNode(final SocialResultPair socialResultPair) {
        likeService.likeSocialNode(getContext(), socialResultPair.getSocialNode().getId(), new BackendCallback<SocialNode>() {
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
        likeService.participateEvent(getContext(), socialResultPair.getSocialNode().getId(), new BackendCallback<EventNode>() {
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
        startActivity(new Intent(getActivity(), CommentActivity.class).putExtras(bundle));
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

    @Override
    public void onOkClicked() {
        updateView();
    }

    @Override
    public void onCancelClicked() {
        //Do nothing
    }
}