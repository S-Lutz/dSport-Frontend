package com.omgproduction.dsport_application.aaRefactored.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.activities.PinboardActivity;
import com.omgproduction.dsport_application.aaRefactored.adapter.SearchUserAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.helper.GeneralDialogFragment;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onDialogFragmentClickListener;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onItemClickListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.UserResultNode;
import com.omgproduction.dsport_application.aaRefactored.services.UserService;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;


public class FriendsFragment extends Fragment implements onItemClickListener<UserResultNode>, onDialogFragmentClickListener {
    private UserService userService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView requestResultRecyclerView;
    private RecyclerView friendResultRecyclerView;


    protected ArrayList<UserResultNode> requestUserNodes;
    protected ArrayList<UserResultNode> friendUserNodes;

    private SearchUserAdapter requestAdapter;
    private SearchUserAdapter friendAdapter;

    private LoadingView loadingView;
    private GeneralDialogFragment dialogFragment;

    private Boolean findFriend = false, findRequest = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !findRequest && !findFriend) {
            loadingView.show();
            initDataset();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userService = new UserService();
        this.dialogFragment = new GeneralDialogFragment(getContext(), getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_bar_friend_layout, container, false);

        loadingView = (LoadingView) rootView.findViewById(R.id.loading_view_friends_result);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.friends_refresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                updateView();
            }
        });

        setUpRequestRecyclerView(rootView);
        setUpEventRecyclerView(rootView);
        setAdapters();
        return rootView;
    }

    private void updateView() {
        loadingView.show();
        initDataset();
    }

    private void setAdapters() {
        if (findFriend && findRequest) {
            setRequestAdapter();
            setFriendAdapter();
            loadingView.hide();
        }
    }


    private void setUpRequestRecyclerView(View rootView) {
        requestResultRecyclerView = (RecyclerView) rootView.findViewById(R.id.request_result_view);
        requestResultRecyclerView.setHasFixedSize(true);
        requestResultRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        requestResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setUpEventRecyclerView(View rootView) {
        friendResultRecyclerView = (RecyclerView) rootView.findViewById(R.id.friend_result_view);
        friendResultRecyclerView.setHasFixedSize(true);
        friendResultRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        friendResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setRequestAdapter() {
        requestAdapter = new SearchUserAdapter(getActivity(), requestUserNodes, this);
        requestResultRecyclerView.setAdapter(requestAdapter);
    }

    private void setFriendAdapter() {
        friendAdapter = new SearchUserAdapter(getActivity(), friendUserNodes, this);
        friendResultRecyclerView.setAdapter(friendAdapter);
    }

    private void findRequests() {
        userService.findRequests(getContext(), new BackendCallback<ArrayList<UserResultNode>>() {

            @Override
            public void onSuccess(ArrayList<UserResultNode> result, Map<String, String> responseHeader) {
                requestUserNodes = result;
                findRequest = true;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }
        });
    }

    private void findFriends() {
        userService.findFriends(getContext(), new BackendCallback<ArrayList<UserResultNode>>() {

            @Override
            public void onSuccess(ArrayList<UserResultNode> result, Map<String, String> responseHeader) {
                friendUserNodes = result;
                findFriend = true;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                dialogFragment.createDialog().show();
            }
        });
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        findRequests();
        findFriends();
    }

    @Override
    public void onClick(View v, UserResultNode userResultNode, SearchUserAdapter.relationshipType status, int position) {
        switch (v.getId()) {
            case R.id.person_add:
                switch (status) {
                    case ACCEPT:
                        v.setEnabled(false);
                        v.setClickable(false);
                        acceptFriendRequest(userResultNode, position);
                        break;
                    case FRIEND:
                        v.setEnabled(false);
                        v.setClickable(false);
                        deleteFriendRequest(userResultNode, position);
                        break;
                }
                break;
            case R.id.item_pic:
                startPinboardActivity(userResultNode, status.toString());
                break;

            case R.id.item_label:
                startPinboardActivity(userResultNode, status.toString());
                break;
        }
    }

    private void startPinboardActivity(UserNode userNode, String relationship) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", userNode);
        bundle.putSerializable("RELATIONSHIP", relationship);
        startActivity(new Intent(getContext(), PinboardActivity.class).putExtras(bundle));
    }

    public void acceptFriendRequest(final UserResultNode userResultNode, final int position) {
        userService.acceptFriendship(getContext(), userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {
                updateRequestList(position);
                addItemToFriendList(userResultNode);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                dialogFragment.createDialog().show();
            }
        });
    }

    public void deleteFriendRequest(UserResultNode userResultNode, final int position) {
        userService.deleteFriendship(getContext(), userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {
                updateFriendList(position);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                dialogFragment.createDialog().show();
            }
        });
    }

    private void updateRequestList(int position) {
        requestUserNodes.remove(position);
        requestResultRecyclerView.removeViewAt(position);
        requestAdapter.notifyItemRemoved(position);
        requestAdapter.notifyItemRangeChanged(position, requestUserNodes.size() - 1);
    }

    private void updateFriendList(int position) {
        friendUserNodes.remove(position);
        friendResultRecyclerView.removeViewAt(position);
        friendAdapter.notifyItemRemoved(position);
        friendAdapter.notifyItemRangeChanged(position, requestUserNodes.size() - 1);
    }

    private void addItemToFriendList(UserResultNode userResultNode) {
        userResultNode.setFriend(true);
        userResultNode.setHasRequest(false);
        friendUserNodes.add(userResultNode);
        friendAdapter.notifyDataSetChanged();
    }


    @Override
    public void onOkClicked() {
        updateView();
    }

    @Override
    public void onCancelClicked() {
        //DO nothing
    }
}