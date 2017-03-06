package com.omgproduction.dsport_application.fragments.helper;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.models.SearchUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendMenuFragment extends SocialMenuFragment{

    private FloatingActionButton add, delete;
    private SearchUser user;

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_fragment_floating_menu_friend,container,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater,container,savedInstanceState);

        setPinboardOwner(user.getId());

        add = (FloatingActionButton) v.findViewById(R.id.friend_fab_add);
        delete = (FloatingActionButton) v.findViewById(R.id.friend_fab_delete);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);

        if(user.isFriend()){
            add.setEnabled(false);
        }else{
            delete.setEnabled(false);
            getCamera().setEnabled(false);
            getGallery().setEnabled(false);
            getExercise_unit().setEnabled(false);
            getEvent().setEnabled(false);
            getText().setEnabled(false);
        }

        return v;
    }

    public SearchUser getUser() {
        return user;
    }

    public void setUser(SearchUser user) {
        this.user = user;
    }

    public void hide(){
        try{
            Animation fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.sub_fab_close);
            add.startAnimation(fab_close);
            delete.startAnimation(fab_close);

            super.hide();

        }catch (NullPointerException e){

        }
    }

    public void show(){
        try{
            super.show();
            Animation fab_open = AnimationUtils.loadAnimation(getContext(),R.anim.sub_fab_open);
            add.startAnimation(fab_open);
            delete.startAnimation(fab_open);

        }catch (NullPointerException e){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friend_fab_add: onAddClicked(); break;
            case R.id.friend_fab_delete: onDeleteClicked();break;
            default: super.onClick(v); break;
        }
    }

    private void onAddClicked() {

    }

    private void onDeleteClicked() {

    }
}
