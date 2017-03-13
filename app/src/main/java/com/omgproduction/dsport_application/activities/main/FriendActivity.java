package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.fragments.helper.FriendMenuFragment;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.fragments.users.UserFragment;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.listeners.adapters.DrawerListenerAdapter;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;

public class FriendActivity  extends AbstractNavigationActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SearchUser friend;
    private FriendMenuFragment friendMenuFragment;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.layout_activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friend = (SearchUser) getIntent().getSerializableExtra(ApplicationKeys.APPLICATION_FRIEND_FRIEND);

        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);
        buildTabViewer();

        preferFabs();
    }

    private void preferFabs() {

        findViewById(R.id.options_fab).setOnClickListener(this);

        friendMenuFragment = new FriendMenuFragment();
        friendMenuFragment.setRootFab((FloatingActionButton) findViewById(R.id.options_fab));
        friendMenuFragment.setUser(friend);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fab_menu_container, friendMenuFragment).commit();

    }

    private void buildTabViewer() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        SocialFragment socialFragment = new SocialFragment();
        socialFragment.setFilter(SocialFragment.Filter.PRIVATE);
        socialFragment.setOwner(friend);

        UserFragment userFragment = new UserFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(socialFragment,getString(R.string.nav_pinboard));
        viewPagerAdapter.addFragments(userFragment,getString(R.string.nav_profile));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        setTabIcons();

        addDrawerListener(new DrawerListenerAdapter(){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                showFABMenu(false);
            }
        });
    }

    private void setTabIcons() {
        TypedArray icons = getResources().obtainTypedArray(R.array.user_activity_icons);
        // get resource ID by index
        for (int l = 0; l < icons.length(); l++) {
            tabLayout.getTabAt(l).setText("");
            tabLayout.getTabAt(l).setIcon(getResources().getDrawable(icons.getResourceId(l, -1)));
        }
    }

    @Override
    public boolean onBackPressedAfterNavigationClosed() {
        if(friendMenuFragment.isOpened()){
            showFABMenu(false);
            return false;
        }else if (viewPager.getCurrentItem() == 0) {
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
            case R.id.options_fab:
                showFABMenu(!friendMenuFragment.isOpened());
                break;
        }
    }

    private void showFABMenu(boolean flag) {
        if(flag){
            if(!friendMenuFragment.isOpened()){
                switch (viewPager.getCurrentItem()){
                    case 0 : showFABMenu(friendMenuFragment,true);
                        break;
                }
            }
        }else{
            if(friendMenuFragment.isOpened()) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        showFABMenu(friendMenuFragment, false);
                        break;
                }
            }
        }
    }

    private void showFABMenu(FloatingMenu floatingMenu, boolean flag){
        if(flag){
            floatingMenu.show();
        }else{
            floatingMenu.hide();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        showFABMenu(false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
