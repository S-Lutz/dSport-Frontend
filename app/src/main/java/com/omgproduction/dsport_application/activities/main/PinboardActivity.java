package com.omgproduction.dsport_application.activities.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.services.UserService;
import com.omgproduction.dsport_application.fragments.helper.SocialMenuFragment;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;


public class PinboardActivity extends AbstractNavigationActivity implements SearchView.OnQueryTextListener {

    private SocialMenuFragment socialMenuFragment;

    @Override
    protected int onSetContentView(Bundle savedInstanceState) {
        return R.layout.layout_activity_pinboard;
    }

    @Override
    public boolean onBackPressedAfterNavigationClosed() {
        if(socialMenuFragment.isOpened()){
            showFABMenu(false);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SocialFragment socialFragment = new SocialFragment();
        socialFragment.setFilter(SocialFragment.Filter.PRIVATE);

        ((SearchView)findViewById(R.id.toolbar_search)).setOnQueryTextListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.pinboard_fragment_container, socialFragment).commit();

        preferFabs();

        loadData();
    }

    private void loadData() {

        User user = getLocalUser();
        socialMenuFragment.setPinboardOwner(user.getId());

    }

    private void preferFabs() {

        findViewById(R.id.options_fab).setOnClickListener(this);

        socialMenuFragment = new SocialMenuFragment();
        socialMenuFragment.setRootFab((FloatingActionButton) findViewById(R.id.options_fab));


        getSupportFragmentManager().beginTransaction()
                .add(R.id.fab_menu_container, socialMenuFragment).commit();

    }

    @Override
    public void onBackPressed() {
        if(socialMenuFragment.isOpened()){
            showFABMenu(false);
        }else{
            super.onBackPressed();
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
            showFABMenu(socialMenuFragment,true);
        }else{
            showFABMenu(socialMenuFragment,false);
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
