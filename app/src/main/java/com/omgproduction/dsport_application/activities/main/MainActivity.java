package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.fragments.helper.MenuManager;
import com.omgproduction.dsport_application.fragments.helper.UniversalListFragment;
import com.omgproduction.dsport_application.listeners.adapters.DrawerListenerAdapter;
import com.omgproduction.dsport_application.adapters.ViewPagerAdapter;
import com.omgproduction.dsport_application.fragments.main.ChatFragment;
import com.omgproduction.dsport_application.fragments.main.EventListFragment;
import com.omgproduction.dsport_application.fragments.main.ExerciseUnitListFragment;
import com.omgproduction.dsport_application.fragments.main.SocialListFragment;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;


public class MainActivity extends AbstractNavigationActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private MenuManager menuManager;
    private SocialListFragment socialFragment;
    private ExerciseUnitListFragment exerciseUnitFragment;
    private EventListFragment eventFragment;
    private ChatFragment chatFragment;
    private UniversalListFragment currentFragment;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.layout_activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuManager = new MenuManager((FloatingActionButton) findViewById(R.id.options_fab), this, getLocalUser());
        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);
        buildTabViewer();
    }

    private void buildTabViewer() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        socialFragment = new SocialListFragment();
        socialFragment.setMenuManager(menuManager);
        exerciseUnitFragment = new ExerciseUnitListFragment();
        exerciseUnitFragment.setMenuManager(menuManager);
        eventFragment = new EventListFragment();
        eventFragment.setMenuManager(menuManager);
        chatFragment = new ChatFragment();
        chatFragment.setMenuManager(menuManager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOnPageChangeListener(this);
        viewPagerAdapter.addFragments(socialFragment,getString(R.string.social));
        viewPagerAdapter.addFragments(exerciseUnitFragment,getString(R.string.exercise_units));
        viewPagerAdapter.addFragments(eventFragment,getString(R.string.events));
        viewPagerAdapter.addFragments(chatFragment,getString(R.string.chats));
        viewPager.setOffscreenPageLimit(3);

        currentFragment = socialFragment;
        socialFragment.onSetActive(true);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
        setTabIcons();

        addDrawerListener(new DrawerListenerAdapter(){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                menuManager.showMenu(false);
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
        if(menuManager.isMenuOpened()){
            menuManager.showMenu(false);
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
                menuManager.toggleMenu();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        menuManager.showMenu(false);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Position", String.valueOf(position));
        if(currentFragment!=null)
            currentFragment.onSetActive(false);
        switch (position){
            case 0: currentFragment = socialFragment; break;
            case 1: currentFragment = exerciseUnitFragment; break;
            case 2: currentFragment = eventFragment; break;
            case 3: currentFragment = chatFragment; break;
        }
        if(currentFragment!=null)
            currentFragment.onSetActive(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
