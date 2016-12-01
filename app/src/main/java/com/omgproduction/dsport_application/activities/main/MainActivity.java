package com.omgproduction.dsport_application.activities.main;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.listeners.adapters.DrawerListenerAdapter;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.fragments.main.ChatFragment;
import com.omgproduction.dsport_application.fragments.main.EventFragment;
import com.omgproduction.dsport_application.fragments.main.ExerciseUnitFragment;
import com.omgproduction.dsport_application.fragments.helper.SocialMenuFragment;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;


public class MainActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SocialMenuFragment socialMenuFragment;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildTabViewer();

        preferFabs();
    }

    private void preferFabs() {

        findViewById(R.id.options_fab).setOnClickListener(this);

        socialMenuFragment = new SocialMenuFragment();
        socialMenuFragment.setRootFab((FloatingActionButton) findViewById(R.id.options_fab));

        UserController.getInstance().getLocalUser(this,new OnResultAdapter<User>(){
            @Override
            public void onSuccess(User result) {
                socialMenuFragment.setPinboardOwner(result.getId());
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(MainActivity.this);
            }
        });


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fab_menu_container, socialMenuFragment).commit();

    }

    private void buildTabViewer() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        SocialFragment socialFragment = new SocialFragment();
        ExerciseUnitFragment exerciseUnitFragment = new ExerciseUnitFragment();
        EventFragment eventFragment = new EventFragment();
        ChatFragment chatFragment = new ChatFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(socialFragment,getString(R.string.social));
        viewPagerAdapter.addFragments(exerciseUnitFragment,getString(R.string.exercise_units));
        viewPagerAdapter.addFragments(eventFragment,getString(R.string.events));
        viewPagerAdapter.addFragments(chatFragment,getString(R.string.chats));

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
        TypedArray icons = getResources().obtainTypedArray(R.array.tab_icons);
        // get resource ID by index
        for (int l = 0; l < icons.length(); l++) {
            tabLayout.getTabAt(l).setText("");
            tabLayout.getTabAt(l).setIcon(getResources().getDrawable(icons.getResourceId(l, -1)));
        }
    }

    @Override
    public boolean onBackPressedAfterNavigationClosed() {
        if(socialMenuFragment.isOpened()){
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
                showFABMenu(!socialMenuFragment.isOpened());
                break;
        }
    }

    private void showFABMenu(boolean flag) {
        if(flag){
            if(!socialMenuFragment.isOpened()){
                switch (viewPager.getCurrentItem()){
                    case 0 : showFABMenu(socialMenuFragment,true);
                        break;
                }
            }
        }else{
            if(socialMenuFragment.isOpened()) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        showFABMenu(socialMenuFragment, false);
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


}
