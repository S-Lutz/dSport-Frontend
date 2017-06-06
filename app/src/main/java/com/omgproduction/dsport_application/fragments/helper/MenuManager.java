package com.omgproduction.dsport_application.fragments.helper;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.models.User;

/**
 * Created by Strik on 09.03.2017.
 */

public class MenuManager implements View.OnClickListener{

    final private FloatingActionButton rootButton;
    final private FragmentActivity activity;
    private MenuFragment menuFragment;
    final private User user;

    public MenuManager(FloatingActionButton rootButton, FragmentActivity activity, User user) {
        this.user= user;
        this.rootButton = rootButton;
        this.activity = activity;
        rootButton.setOnClickListener(this);
    }

    public void setMenuFragment(MenuFragment menuFragment) {
        if(this.menuFragment!=null){
            activity.getSupportFragmentManager().beginTransaction().remove(this.menuFragment).commit();
        }
        this.menuFragment = menuFragment;

        if(menuFragment==null){
            rootButton.setVisibility(View.GONE);
            return;
        }else {
            rootButton.setVisibility(View.VISIBLE);
        }
        menuFragment.setRootFab(rootButton);
        menuFragment.setOwner(user.getId());
        activity.getSupportFragmentManager().beginTransaction()
                .add(R.id.fab_menu_container, menuFragment).commit();
    }

    @Override
    public void onClick(View view) {
        Log.e("Menu", "Menu Clicked");
        toggleMenu();
    }

    public boolean isMenuOpened(){
        if(menuFragment==null) return false;
        return menuFragment.isOpened();
    }

    public void showMenu(boolean flag){
        if(menuFragment==null) return;
        if(flag){
            if(!isMenuOpened()){
                menuFragment.show();
            }
        }else{
            if(isMenuOpened()) {
                menuFragment.hide();
            }
        }
    }

    public void toggleMenu(){
        showMenu(!isMenuOpened());
    }
}
