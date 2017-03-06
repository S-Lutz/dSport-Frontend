package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.controller.SearchController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.fragments.search.EventsResultFragment;
import com.omgproduction.dsport_application.fragments.search.StudioResultFragment;
import com.omgproduction.dsport_application.fragments.search.UserResultFragment;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;

import org.json.JSONException;

public class SearchResultActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String query;
    private UserResultFragment userResultFragment;
    private StudioResultFragment studioResultFragment;
    private EventsResultFragment eventsResultFragment;


    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.activity_search_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);
        query = getIntent().getStringExtra(ApplicationKeys.QUERY);

        searchResults(query);

        buildTabViewer();

        preferFabs();
    }

    private void searchResults(final String query) {
        UserController.getInstance().getLocalUserID(this,new OnResultAdapter<String>(){
            @Override
            public void onStartQuery() {
                super.onStartQuery();
            }

            @Override
            public void onSuccess(String localUserID) {
                SearchController.getInstance().searchQuery(localUserID, query, new OnResultAdapter<SearchResultHolder>(){
                    @Override
                    public void onStartQuery() {
                        super.onStartQuery();
                    }

                    @Override
                    public void onSuccess(SearchResultHolder result) {
                        userResultFragment.setSearchUsers(result.getUsers());
                        for(SearchUser searchUser: result.getUsers()){
                            System.out.println(searchUser.getFirstname());
                        }
                    }

                    @Override
                    public void onConnectionError(VolleyError e) {
                        super.onConnectionError(e);
                    }

                    @Override
                    public void onBackendError(String errorCode) {
                        super.onBackendError(errorCode);
                    }

                    @Override
                    public void onJSONException(JSONException e) {
                        super.onJSONException(e);
                    }

                    @Override
                    public void onUserNotFound() {
                        super.onUserNotFound();
                    }

                    @Override
                    public void onFinishQuery() {
                        super.onFinishQuery();
                    }
                });
            }

            @Override
            public void onConnectionError(VolleyError e) {
                super.onConnectionError(e);
            }

            @Override
            public void onBackendError(String errorCode) {
                super.onBackendError(errorCode);
            }

            @Override
            public void onJSONException(JSONException e) {
                super.onJSONException(e);
            }

            @Override
            public void onUserNotFound() {
                super.onUserNotFound();
            }

            @Override
            public void onFinishQuery() {
                super.onFinishQuery();
            }
        });
    }

    private void preferFabs() {

    }

    private void buildTabViewer() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        userResultFragment = new UserResultFragment();
        eventsResultFragment = new EventsResultFragment();
        studioResultFragment = new StudioResultFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(userResultFragment,getString(R.string.users));
        viewPagerAdapter.addFragments(eventsResultFragment,getString(R.string.events));
        viewPagerAdapter.addFragments(studioResultFragment,getString(R.string.studios));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        setTabIcons();


    }

    private void setTabIcons() {
        TypedArray icons = getResources().obtainTypedArray(R.array.search_result_icons);
        // get resource ID by index
        for (int l = 0; l < icons.length(); l++) {
            tabLayout.getTabAt(l).setText("");
            tabLayout.getTabAt(l).setIcon(getResources().getDrawable(icons.getResourceId(l, -1)));
        }
    }

    @Override
    public boolean onBackPressedAfterNavigationClosed() {
        if (viewPager.getCurrentItem() == 0) {
            return true;
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return false;
        }
    }

    @Override
    protected void removeAllErrors() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchResults(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
