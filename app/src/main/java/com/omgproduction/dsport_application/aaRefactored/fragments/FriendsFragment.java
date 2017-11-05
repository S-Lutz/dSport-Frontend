package com.omgproduction.dsport_application.aaRefactored.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.SearchUserAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onItemClickListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.UserResultNode;
import com.omgproduction.dsport_application.aaRefactored.services.UserService;

import java.util.ArrayList;
import java.util.Map;


public class FriendsFragment extends Fragment implements onItemClickListener<UserResultNode> {
    private UserService userService;

    private RecyclerView requestResultRecyclerView;
    private RecyclerView friendResultRecyclerView;


    protected ArrayList<UserResultNode> requestUserNodes;
    protected ArrayList<UserResultNode> friendUserNodes;

    private SearchUserAdapter requestAdapter;
    private SearchUserAdapter friendAdapter;

    private Boolean findFriend = false, findRequest = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userService = new UserService();
        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_bar_friend_layout, container, false);

        setUpRequestRecyclerView(rootView);
        setUpEventRecyclerView(rootView);
        setAdapters();

        return rootView;
    }

    private void setAdapters() {
        if (findFriend && findRequest) {
            setRequestAdapter();
            setFriendAdapter();
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
                findRequest = true;
                requestUserNodes = result;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                Toast.makeText(getActivity(), "An error occurred, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findFriends() {
        userService.findFriends(getContext(), new BackendCallback<ArrayList<UserResultNode>>() {

            @Override
            public void onSuccess(ArrayList<UserResultNode> result, Map<String, String> responseHeader) {
                findFriend = true;
                friendUserNodes = result;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                Toast.makeText(getActivity(), "An error occurred, please try again later", Toast.LENGTH_LONG).show();
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
                        acceptFriendRequest(userResultNode, position);
                        break;
                    case FRIEND:
                        deleteFriendRequest(userResultNode, position);
                        break;
                }
        }
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
                Toast.makeText(getActivity(), "An error occurred, please try again later", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), "An error occurred, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRequestList(int position) {
        requestUserNodes.remove(position);
        requestResultRecyclerView.removeViewAt(position);
        requestAdapter.notifyItemRemoved(position);
        requestAdapter.notifyItemRangeChanged(position, requestUserNodes.size()-1);
    }

    private void updateFriendList(int position) {
        friendUserNodes.remove(position);
        friendResultRecyclerView.removeViewAt(position);
        friendAdapter.notifyItemRemoved(position);
        friendAdapter.notifyItemRangeChanged(position, requestUserNodes.size()-1);
    }

    private void addItemToFriendList(UserResultNode userResultNode){
        userResultNode.setFriend(true);
        userResultNode.setHasRequest(false);
        friendUserNodes.add(userResultNode);
        friendAdapter.notifyDataSetChanged();
    }
}