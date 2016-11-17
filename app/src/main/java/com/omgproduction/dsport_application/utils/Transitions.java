package com.omgproduction.dsport_application.utils;

import android.support.v4.view.GravityCompat;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.omgproduction.dsport_application.R;

/**
 * Created by Florian on 17.11.2016.
 */

public class Transitions {

    private static final int duration = 300;

    public static void slideOutLeft(ViewGroup rootView, View... views){

        Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.START, rootView.getResources().getConfiguration().getLayoutDirection()));
        slide.setDuration(duration);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);
    }

    public static void slideInLeft(ViewGroup rootView, View... views){

        Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.START, rootView.getResources().getConfiguration().getLayoutDirection()));
        slide.setDuration(duration);
        slide.setMode(Slide.MODE_IN);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);
    }

    public static void slideOutRight(ViewGroup rootView, View... views){

        Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
        slide.setDuration(duration);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);
    }

    public static void slideInRight(ViewGroup rootView, View... views){

        Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
        slide.setDuration(duration);
        slide.setMode(Slide.MODE_IN);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);
    }

    public static void slideOutBottom(ViewGroup rootView, View... views){

        Slide slide = new Slide();
        slide.setDuration(duration);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);

    }

    public static void slideInBottom(ViewGroup rootView, View... views){

        Slide slide = new Slide();
        slide.setDuration(duration);
        slide.setMode(Slide.MODE_IN);
        TransitionManager.beginDelayedTransition(rootView,slide);
        ViewUtils.toggleExistence(views);
    }
}
