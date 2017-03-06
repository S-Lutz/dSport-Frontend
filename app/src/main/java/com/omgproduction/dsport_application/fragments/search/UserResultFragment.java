package com.omgproduction.dsport_application.fragments.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.FriendActivity;
import com.omgproduction.dsport_application.adapters.PostAdapter;
import com.omgproduction.dsport_application.adapters.SearchUserAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.supplements.activities.AdvancedFragment;

import java.util.ArrayList;

public class UserResultFragment extends AdvancedFragment  implements SwipeRefreshLayout.OnRefreshListener, SearchUserAdapter.OnSearchUserClicked {

    private RecyclerView searchUserRecycler;
    private SearchUserAdapter searchUserAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresher;
    private ArrayList<SearchUser> searchUsers = new ArrayList<>();

    public UserResultFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        refresher = (SwipeRefreshLayout) view.findViewById(R.id.search_user_refresher);
        refresher.setOnRefreshListener(this);
        return view;
    }

    private void update() {

    }

    @Override
    public void onRefresh() {
        update();
    }

    @Override
    public void onSearchUserClicked(SearchUser searchUser) {
        Intent i = new Intent(getContext(), FriendActivity.class);
        i.putExtra(ApplicationKeys.FRIEND_FRIEND,searchUser);
        startActivity(i);
    }

    public ArrayList<SearchUser> getSearchUsers() {
        return searchUsers;
    }

    public void setSearchUsers(ArrayList<SearchUser> searchUsers) {
        this.searchUsers = searchUsers;
        searchUserRecycler = (RecyclerView) getView().findViewById(R.id.search_user_recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        searchUserRecycler.setLayoutManager(layoutManager);
        searchUserRecycler.setHasFixedSize(true);
        searchUserAdapter = new SearchUserAdapter(searchUsers);
        searchUserAdapter.addOnSearchUserClicked(UserResultFragment.this);
        searchUserRecycler.setAdapter(searchUserAdapter);

    }
}
