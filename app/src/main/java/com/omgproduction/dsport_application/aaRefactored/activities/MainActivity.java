package com.omgproduction.dsport_application.aaRefactored.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.adapter.FragmentStateAdapter;
import com.omgproduction.dsport_application.aaRefactored.fragments.FriendsFragment;
import com.omgproduction.dsport_application.aaRefactored.fragments.MainFragment;
import com.omgproduction.dsport_application.aaRefactored.fragments.MoreFragment;
import com.omgproduction.dsport_application.aaRefactored.services.UserService;
import com.omgproduction.dsport_application.aaRefactored.viewPager.NoSwipeViewPager;


public class MainActivity extends AppCompatActivity {

    private UserService userService;
    private BottomNavigationView bottomNavigation;

    private FragmentManager fragmentManager;

    private NoSwipeViewPager viewPager;
    private FragmentStateAdapter viewPagerAdapter;

    private FriendsFragment friendsFragment ;
    private MainFragment mainFragment;
    private MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_bottom_navigation);


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            startSearchResultActivity(query);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        userService = new UserService();
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        viewPagerAdapter = new FragmentStateAdapter(fragmentManager);
        viewPager = (NoSwipeViewPager)findViewById(R.id.myViewPager);

        viewPager.setPagingEnabled(false);

        mainFragment = new MainFragment();
        friendsFragment = new FriendsFragment();
        moreFragment = new MoreFragment();

        viewPagerAdapter.addFragments(mainFragment);
        viewPagerAdapter.addFragments(friendsFragment);
        viewPagerAdapter.addFragments(moreFragment);


        viewPager.setAdapter(viewPagerAdapter);


        createListener();
    }

    private void startSearchResultActivity(String query) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("QUERY_STRING", query);
        startActivity(new Intent(this, SearchResultActivity.class).putExtras(bundle));
    }



    private void createListener() {

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_nav_main:
                       viewPager.setCurrentItem(0);
                        break;
                    case R.id.bottom_nav_friends:
                        viewPager.setCurrentItem(1);;
                        break;
                    case R.id.bottom_nav_settings:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_action_bar_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_bar_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }


}
