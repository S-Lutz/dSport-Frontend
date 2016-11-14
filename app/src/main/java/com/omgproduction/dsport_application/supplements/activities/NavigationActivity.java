package com.omgproduction.dsport_application.supplements.activities;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.LoginActivity;
import com.omgproduction.dsport_application.activities.MainActivity;
import com.omgproduction.dsport_application.activities.ProfileActivity;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.Keys;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.supplements.activities.AdvancedAppCompatActivity;
import com.omgproduction.dsport_application.utils.BitmapUtils;

/**
 * Created by Florian on 06.11.2016.
 */

public abstract class NavigationActivity extends AdvancedAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    protected Context context;
    protected NavigationView navigationView;
    protected DrawerLayout drawer;

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
                    if (!isDrawerOpened()) {
                        updateNavigationValues();
                    }
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
        ((TextView)headerLayout.findViewById(R.id.nav_username)).setText(Preferences.getInstance(this).getStringDetail(Keys.USERNAME,getString(R.string.empty)));
        ((TextView)headerLayout.findViewById(R.id.nav_email)).setText(Preferences.getInstance(this).getStringDetail(Keys.EMAIL,getString(R.string.empty)));
        ((ImageView)headerLayout.findViewById(R.id.nav_img)).setImageBitmap(BitmapUtils.getBitmapFromString(this,Preferences.getInstance(this).getStringDetail(Keys.PICTURE,getString(R.string.empty))));
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
        SessionController.getInstance().logoutUser(this);
        startLoginActivity(this);
    }

    protected void startLoginActivity(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(i);
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
}
