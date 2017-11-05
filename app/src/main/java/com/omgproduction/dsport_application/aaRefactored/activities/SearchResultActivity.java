package com.omgproduction.dsport_application.aaRefactored.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.SearchEventAdapter;
import com.omgproduction.dsport_application.aaRefactored.adapter.SearchUserAdapter;
import com.omgproduction.dsport_application.aaRefactored.adapter.SerachPostAdapter;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;
import com.omgproduction.dsport_application.aaRefactored.interfaces.onItemClickListener;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.PostNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.UserResultNode;
import com.omgproduction.dsport_application.aaRefactored.services.UserService;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;

import java.util.ArrayList;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity implements onItemClickListener<UserResultNode> {
    private static String query;
    private UserService userService;
    private static final String JSON_KEY = "query";

    private RecyclerView userResultRecyclerView;
    private RecyclerView eventResultRecyclerView;
    private RecyclerView postResultRecyclerView;

    private SearchUserAdapter userAdapter;

    private ArrayList<UserResultNode> users;
    private ArrayList<PostNode> posts;
    private ArrayList<EventNode> events;

    private Boolean findUser = false, findEvent = false, findPost = false;
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_new_search_result);


        this.userService = new UserService();
        query = getIntent().getExtras().getString("QUERY_STRING");

        loadingView = (LoadingView) findViewById(R.id.loading_view_search_result);
        loadingView.show();

        setUpToolbarTitle();
        setupRecyclerViews();
        startQuerys();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startQuerys() {
        findUsers(query);
        findEvents(query);
        findPosts(query);
    }

    private void setupRecyclerViews() {
        setUpUserRecyclerView();
        setUpEventRecyclerView();
        setUpPostRecyclerView();
    }

    //set toolbar title and set back button
    private void setUpToolbarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result");
    }

    //setup recycler view
    private void setUpUserRecyclerView() {
        userResultRecyclerView = (RecyclerView) findViewById(R.id.user_result);
        userResultRecyclerView.setHasFixedSize(true);
        userResultRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setUpEventRecyclerView() {
        eventResultRecyclerView = (RecyclerView) findViewById(R.id.event_result);
        eventResultRecyclerView.setHasFixedSize(true);
        eventResultRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        eventResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setUpPostRecyclerView() {
        postResultRecyclerView = (RecyclerView) findViewById(R.id.post_result);
        postResultRecyclerView.setHasFixedSize(true);
        postResultRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        postResultRecyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setAdapters() {
        if (findUser && findEvent && findPost) {
            setUserAdapter();
            setEventAdapter();
            setPostAdapter();
            loadingView.hide();
        }
    }

    private void setUserAdapter() {
        userAdapter = new SearchUserAdapter(SearchResultActivity.this, users, this);
        userResultRecyclerView.setAdapter(userAdapter);
    }

    private void setEventAdapter() {
        SearchEventAdapter adapter = new SearchEventAdapter(SearchResultActivity.this, events);
        eventResultRecyclerView.setAdapter(adapter);
    }

    private void setPostAdapter() {
        SerachPostAdapter adapter = new SerachPostAdapter(SearchResultActivity.this, posts);
        postResultRecyclerView.setAdapter(adapter);
    }

    private void findUsers(final String query) {
        JsonObject request = new JsonObject();
        request.addProperty(JSON_KEY, query);
        userService.findUsers(this, request, new BackendCallback<ArrayList<UserResultNode>>() {

            @Override
            public void onSuccess(ArrayList<UserResultNode> result, Map<String, String> responseHeader) {
                findUser = true;
                if (users != null) users.clear();
                users = result;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                Toast.makeText(SearchResultActivity.this, "An error occurred, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findEvents(final String query) {
        JsonObject request = new JsonObject();
        request.addProperty(JSON_KEY, query);
        userService.findEvents(this, request, new BackendCallback<ArrayList<EventNode>>() {
            @Override
            public void onSuccess(ArrayList<EventNode> result, Map<String, String> responseHeader) {
                findEvent = true;
                events = result;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                Toast.makeText(SearchResultActivity.this, "An error occurred, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findPosts(String query) {
        JsonObject request = new JsonObject();
        request.addProperty(JSON_KEY, query);
        userService.findPosts(this, request, new BackendCallback<ArrayList<PostNode>>() {

            @Override
            public void onSuccess(ArrayList<PostNode> result, Map<String, String> responseHeader) {
                findPost = true;
                posts = result;
                setAdapters();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                Toast.makeText(SearchResultActivity.this, "An error occurred, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v, UserResultNode userResultNode, SearchUserAdapter.relationshipType status, int adapterPosition) {
        switch (v.getId()) {
            case R.id.person_add:
                switch (status) {
                    case ADD:
                        sendFriendRequest(userResultNode);
                        userAdapter.updateImageRessource(adapterPosition, SearchUserAdapter.relationshipType.REQUESTED, userResultNode);
                        break;
                    case ACCEPT:
                        acceptFriendRequest(userResultNode);
                        userAdapter.updateImageRessource(adapterPosition, SearchUserAdapter.relationshipType.FRIEND, userResultNode);
                        break;
                    case FRIEND:
                        deleteFriendRequest(userResultNode);
                        userAdapter.updateImageRessource(adapterPosition, SearchUserAdapter.relationshipType.DELETE, userResultNode);
                        break;
                    case REQUESTED:
                        Toast.makeText(this, "Your friend has not accept your request yet", Toast.LENGTH_SHORT).show();
                        break;
                    case UNKNOWN:
                        Toast.makeText(this, "Unknown relationship status", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    }

    public void sendFriendRequest(UserResultNode userResultNode) {
        userService.requestFriendship(this, userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }

    public void acceptFriendRequest(UserResultNode userResultNode) {
        userService.acceptFriendship(this, userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }

    public void deleteFriendRequest(UserResultNode userResultNode) {
        userService.deleteFriendship(this, userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }

    public void declineFriendRequest(UserResultNode userResultNode) {
        userService.declineFriendship(this, userResultNode, new BackendCallback<String>() {
            @Override
            public void onSuccess(String result, Map<String, String> responseHeader) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        });
    }


}
