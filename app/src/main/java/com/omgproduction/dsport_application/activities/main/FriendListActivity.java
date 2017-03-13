package com.omgproduction.dsport_application.activities.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.SearchUserAdapter;
import com.omgproduction.dsport_application.fragments.search.UserResultFragment;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.PostService;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;

import java.util.List;

public class FriendListActivity extends AbstractNavigationActivity implements SearchUserAdapter.OnSearchUserClicked{

    private List<SearchUser> friends;
    private RecyclerView searchUserRecycler;
    private LinearLayoutManager layoutManager;
    private SearchUserAdapter searchUserAdapter;

    private UserService userService;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.activity_friend_list;
    }

    @Override
    protected boolean onBackPressedAfterNavigationClosed() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRefresher((SwipeRefreshLayout) findViewById(R.id.friend_list_refresher));
        userService = new UserService(this);

        update();

    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onRefresh() {
        update();
    }

    private void update() {
        User localUser = getLocalUser();
        userService.getAllFriends(localUser.getId(), new RequestFuture<List<SearchUser>>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(List<SearchUser> result) {
                setFriends(result);
            }

            @Override
            public void onFailure(String errorCode) {
                printError(R.id.friend_list_container,errorCode);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void setFriends(List<SearchUser> friends){
        this.friends = friends;
        searchUserRecycler = (RecyclerView) findViewById(R.id.friend_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        searchUserRecycler.setLayoutManager(layoutManager);
        searchUserRecycler.setHasFixedSize(true);
        searchUserAdapter = new SearchUserAdapter(friends);
        searchUserAdapter.addOnSearchUserClicked(FriendListActivity.this);
        searchUserRecycler.setAdapter(searchUserAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSearchUserClicked(SearchUser searchUser) {

    }
}
