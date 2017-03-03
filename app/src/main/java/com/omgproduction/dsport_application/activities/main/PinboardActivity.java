package com.omgproduction.dsport_application.activities.main;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.controller.UserController;
import com.omgproduction.dsport_application.fragments.helper.SocialMenuFragment;
import com.omgproduction.dsport_application.fragments.main.SocialFragment;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.listeners.adapters.OnResultAdapter;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractNavigationActivity;


public class PinboardActivity extends AbstractNavigationActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SocialFragment socialFragment = new SocialFragment();
        socialFragment.setFilter(SocialFragment.Filter.PRIVATE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.pinboard_fragment_container, socialFragment).commit();

        preferFabs();

        loadData();
    }

    private void loadData() {
        UserController.getInstance().getLocalUser(this,new OnResultAdapter<User>(){
            @Override
            public void onSuccess(User user) {
                //setPic(R.id.profile_pic, user.getBitmap(PinboardActivity.this));
                socialMenuFragment.setPinboardOwner(user.getId());
            }

            @Override
            public void onUserNotFound() {
                SessionController.getInstance().logout(PinboardActivity.this);
            }
        });

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
}
