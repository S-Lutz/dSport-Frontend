package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.SearchUserAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;
import com.omgproduction.dsport_application.utils.Triple;

import java.util.List;

public class FriendListActivity extends AbstractNavigationActivity implements SearchView.OnQueryTextListener, SearchUserAdapter.OnUpdateTrigger{

    private List<SearchUser> friends;
    private List<SearchUser> sendedFriends;
    private List<SearchUser> receivedFriends;
    private RecyclerView searchUserRecycler;
    private LinearLayoutManager layoutManager;
    private SearchUserAdapter searchUserAdapter;
    private RecyclerView searchUserRecyclerSended;
    private LinearLayoutManager layoutManagerSended;
    private SearchUserAdapter searchUserAdapterSended;
    private RecyclerView searchUserRecyclerReceived;
    private LinearLayoutManager layoutManagerReceived;
    private SearchUserAdapter searchUserAdapterReceived;

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
        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);

        update();

    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onRefresh() {
        Log.e("refresh","refresh");
        update();
    }

    private void update() {
        User localUser = getLocalUser();
        userService.getAllFriends(localUser.getId(), new RequestFuture<Triple<List<SearchUser>,List<SearchUser>,List<SearchUser>>>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(Triple<List<SearchUser>,List<SearchUser>,List<SearchUser>> result) {
                setFriends(result.get_1());
                setReceivedFriends(result.get_2());
                setSendedFriends(result.get_3());
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printError(R.id.friend_list_container,errorMessage);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void setSendedFriends(List<SearchUser> sendedFriends) {
        this.sendedFriends = sendedFriends;
        searchUserRecyclerSended= (RecyclerView) findViewById(R.id.friend_list_recycler_view_sended);
        layoutManagerSended = new LinearLayoutManager(this);
        searchUserRecyclerSended.setLayoutManager(layoutManagerSended);
        searchUserRecyclerSended.setHasFixedSize(true);
        searchUserAdapterSended = new SearchUserAdapter(sendedFriends, this);
        searchUserRecyclerSended.setAdapter(searchUserAdapterSended);


    }

    private void setReceivedFriends(List<SearchUser> receivedFriends) {
        this.receivedFriends = receivedFriends;
        searchUserRecyclerReceived= (RecyclerView) findViewById(R.id.friend_list_recycler_view_received);
        layoutManagerReceived = new LinearLayoutManager(this);
        searchUserRecyclerReceived.setLayoutManager(layoutManagerReceived);
        searchUserRecyclerReceived.setHasFixedSize(true);
        searchUserAdapterReceived = new SearchUserAdapter(receivedFriends, this);
        searchUserRecyclerReceived.setAdapter(searchUserAdapterReceived);

    }

    private void setFriends(List<SearchUser> friends){
        this.friends = friends;
        searchUserRecycler = (RecyclerView) findViewById(R.id.friend_list_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        searchUserRecycler.setLayoutManager(layoutManager);
        searchUserRecycler.setHasFixedSize(true);
        searchUserAdapter = new SearchUserAdapter(friends, this);
        searchUserRecycler.setAdapter(searchUserAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onUpdate() {
        update();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(this, SearchResultActivity.class);
        i.putExtra(ApplicationKeys.APPLICATION_QUERY, query);
        startActivity(i);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
