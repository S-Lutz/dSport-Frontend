package com.omgproduction.dsport_application.fragments.helper;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.CreatePostActivity;
import com.omgproduction.dsport_application.config.CreateEventStartValues;
import com.omgproduction.dsport_application.config.CreatePostStartValues;
import com.omgproduction.dsport_application.config.IntentKeys;
import com.omgproduction.dsport_application.interfaces.FloatingMenu;
import com.omgproduction.dsport_application.utils.Transitions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strik on 09.03.2017.
 */

public abstract class MenuFragment extends Fragment implements FloatingMenu, View.OnClickListener, IntentKeys, CreatePostStartValues, CreateEventStartValues {

    protected FloatingActionButton rootFab;
    protected String owner;
    protected boolean opened = false;
    protected List<FloatingActionButton> menuButtons;

    public MenuFragment() {
        // Required empty public constructor
    }

    protected void addMenuButton(FloatingActionButton menuButton) {
        menuButtons.add(menuButton);
    }


    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayout(), container, false);
    }

    public abstract int getLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflateView(inflater, container);

        menuButtons = new ArrayList<>();
        setMenuButtons(v);

        for(FloatingActionButton b : menuButtons){
            b.setOnClickListener(this);
        }

        v.setVisibility(View.GONE);

        return v;
    }

    protected abstract void setMenuButtons(View v);

    public void setRootFab(FloatingActionButton fab) {
        rootFab = fab;
    }

    public void hide() {
        try {
            Animation rotate_out = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_bottom_right_anticlock);
            getView().findViewById(getMenuContainer()).startAnimation(rotate_out);


            Animation fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.sub_fab_close);
            for (FloatingActionButton b : menuButtons) {
                b.startAnimation(fab_close);
            }
            Transitions.hideFading((ViewGroup) getView(), getView());


            final ObjectAnimator animator = ObjectAnimator.ofInt(rootFab, "backgroundTint", Color.rgb(255, 0, 0), getContext().getResources().getColor(R.color.colorAccent));
            animator.setDuration(300L);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setInterpolator(new DecelerateInterpolator(2));
            animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    rootFab.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
                }
            });
            animator.start();
            Animation rotate_clockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
            rootFab.startAnimation(rotate_clockwise);
            opened = false;
        } catch (NullPointerException e) {

        }
    }

    protected abstract int getMenuContainer();

    public void show() {
        try {
            Transitions.showFading((ViewGroup) getView(), getView());
            Animation rotate_in = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_bottom_right_clock);
            getView().findViewById(getMenuContainer()).startAnimation(rotate_in);

            Animation fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.sub_fab_open);
            for (FloatingActionButton b : menuButtons) {
                b.startAnimation(fab_open);
            }


            final ObjectAnimator animator = ObjectAnimator.ofInt(rootFab, "backgroundTint", getContext().getResources().getColor(R.color.colorAccent), Color.rgb(255, 0, 0));
            animator.setDuration(300L);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setInterpolator(new DecelerateInterpolator(2));
            animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    rootFab.setBackgroundTintList(ColorStateList.valueOf(animatedValue));
                }
            });
            animator.start();
            Animation rotate_anti_clockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anti_clockwise);
            rootFab.startAnimation(rotate_anti_clockwise);

            opened = true;

        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onClick(View v) {
        onMenuButtonClicked(v);
    }

    public abstract void onMenuButtonClicked(View v);

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isOpened() {
        return opened;
    }

    public FloatingActionButton getRootFab() {
        return rootFab;
    }

    public String getOwner() {
        return owner;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}