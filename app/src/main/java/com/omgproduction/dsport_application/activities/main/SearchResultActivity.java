package com.omgproduction.dsport_application.activities.main;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.SearchService;
import com.omgproduction.dsport_application.fragments.search.EventsResultFragment;
import com.omgproduction.dsport_application.fragments.search.StudioResultFragment;
import com.omgproduction.dsport_application.fragments.search.UserResultFragment;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;

public class SearchResultActivity extends AbstractNavigationActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String query;
    private UserResultFragment userResultFragment;
    private StudioResultFragment studioResultFragment;
    private EventsResultFragment eventsResultFragment;

    private SearchService searchService;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.layout_activity_search_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);
        query = getIntent().getStringExtra(ApplicationKeys.APPLICATION_QUERY);

        searchService = new SearchService(this);

        preferFabs();

        buildTabViewer();

        searchResults(query);
    }

    private void searchResults(final String query) {

        User user = getLocalUser();

        searchService.searchQuery(user.getId(), query, new RequestFuture<SearchResultHolder>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(SearchResultHolder result) {
                userResultFragment.setSearchUsers(result.getUsers());
            }

            @Override
            public void onFailure(String errorCode) {
                //TODO

            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
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
        showProgressBar(false);
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
