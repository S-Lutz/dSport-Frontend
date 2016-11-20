package com.omgproduction.dsport_application.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.adapters.DrawerListenerAdapter;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.fragments.ChatFragment;
import com.omgproduction.dsport_application.fragments.EventFragment;
import com.omgproduction.dsport_application.fragments.ExerciseUnitFragment;
import com.omgproduction.dsport_application.fragments.SocialMenuFragment;
import com.omgproduction.dsport_application.fragments.SocialFragment;
import com.omgproduction.dsport_application.interfaces.IFABMenu;
import com.omgproduction.dsport_application.supplements.activities.NavigationActivity;


public class MainActivity extends NavigationActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private boolean isFABMenuOpened = false;
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
        if(isFABMenuOpened){
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
                showFABMenu(!isFABMenuOpened);
                break;
        }
    }

    private void showFABMenu(boolean flag) {
        if(flag){
            switch (viewPager.getCurrentItem()){
                case 0 : showFABMenu(socialMenuFragment,true);
                    break;
            }
        }else{
            switch (viewPager.getCurrentItem()){
                case 0 : showFABMenu(socialMenuFragment,false);
                    break;
            }
        }
    }

    private void showFABMenu(IFABMenu ifabMenu, boolean flag){
        if(flag){
            ifabMenu.show();
            isFABMenuOpened = flag;
        }else{
            ifabMenu.hide();
            isFABMenuOpened = flag;
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
