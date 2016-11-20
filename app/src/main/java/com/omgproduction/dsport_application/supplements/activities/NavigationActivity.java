package com.omgproduction.dsport_application.supplements.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.MainActivity;
import com.omgproduction.dsport_application.activities.ProfileActivity;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Florian on 06.11.2016.
 */

public abstract class NavigationActivity extends AdvancedAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    protected Context context;
    protected NavigationView navigationView;
    protected DrawerLayout drawer;

    public ArrayList<DrawerLayout.DrawerListener> drawerListeners = new ArrayList<>();

    private static int id = R.id.nav_main;

    /**
     * While onCreate you should set the Content View. This Method is called after super-onCreate like
     * setContentView(onSetContentView(savedInstanceState));
     * @param savedInstanceState
     * @return id of the Content View
     */
    protected abstract int onSetContentView(Bundle savedInstanceState);

    /**
     * onBackPressed will check if the NavigationBar is closed...
     * after this, this Method will be called
     *
     * if you want to call super.onBackPressed, return true, else return false
     * @return call super.onBackPressed
     */
    protected abstract boolean onBackPressedAfterNavigationClosed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onSetContentView(savedInstanceState));

        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = ((DrawerLayout) findViewById(R.id.drawer_layout));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_SETTLING) {
                    for(DrawerLayout.DrawerListener d : drawerListeners){
                        d.onDrawerStateChanged(newState);
                    }
                    if (!isDrawerOpened()) {
                        updateNavigationValues();
                    }
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                for(DrawerLayout.DrawerListener d : drawerListeners){
                    d.onDrawerSlide(drawerView,slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                for(DrawerLayout.DrawerListener d : drawerListeners){
                    d.onDrawerOpened(drawerView);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                for(DrawerLayout.DrawerListener d : drawerListeners){
                    d.onDrawerClosed(drawerView);
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationValues();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(isDrawerOpened()){
            closeDrawer();
        }else{
            openDrawer();
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void updateNavigationValues() {
        View headerLayout =
                navigationView.getHeaderView(0);
        ((TextView)headerLayout.findViewById(R.id.nav_username)).setText(Preferences.getInstance(this).getStringDetail(ApplicationKeys.USERNAME,getString(R.string.empty)));
        ((TextView)headerLayout.findViewById(R.id.nav_email)).setText(Preferences.getInstance(this).getStringDetail(ApplicationKeys.EMAIL,getString(R.string.empty)));
        ((ImageView)headerLayout.findViewById(R.id.nav_img)).setImageBitmap(BitmapUtils.getBitmapFromString(this,Preferences.getInstance(this).getStringDetail(ApplicationKeys.PICTURE,getString(R.string.empty))));
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpened()) {
            closeDrawer();
        } else{
            if(onBackPressedAfterNavigationClosed()){
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();
        switch (id){
            case R.id.nav_main: startActivity(new Intent(this, MainActivity.class)); break;
            case R.id.nav_profile: performProfileClick();break;
            case R.id.nav_friends: break;
            case R.id.nav_logout: logoutUser(); break;
            case R.id.nav_settings: break;
        }

        closeDrawer();
        return true;
    }

    protected void performProfileClick(){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    protected void logoutUser(){
        SessionController.getInstance().logout(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.setChecked(false);
        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpened(){
        return ((DrawerLayout) findViewById(R.id.drawer_layout)).isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer(){
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
    }

    public void openDrawer(){
        ((DrawerLayout) findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
    }

    public void addDrawerListener(DrawerLayout.DrawerListener drawerListener){
        this.drawerListeners.add(drawerListener);
    }

    public void removeDrawerListener(DrawerLayout.DrawerListener drawerListener){
        this.drawerListeners.remove(drawerListener);
    }
}
