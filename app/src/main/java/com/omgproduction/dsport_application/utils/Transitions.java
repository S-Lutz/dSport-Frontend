package com.omgproduction.dsport_application.utils;

import android.support.v4.view.GravityCompat;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Florian on 17.11.2016.
 */

public class Transitions {

    private static final int duration = 300;

    public static void slideOutLeft(final ViewGroup rootView, View... views) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Slide slide = new Slide();
                slide.setSlideEdge(Gravity.LEFT);
                slide.setDuration(duration);
                TransitionManager.beginDelayedTransition(rootView,slide);
                ViewUtils.toggleExistence(false, views);
            }
        }

    public static void slideInLeft(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.START, rootView.getResources().getConfiguration().getLayoutDirection()));
            slide.setDuration(duration);
            slide.setMode(Slide.MODE_IN);
            TransitionManager.beginDelayedTransition(rootView,slide);
            ViewUtils.toggleExistence(true, views);
        }
    }

    public static void slideOutRight(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
            slide.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView,slide);
            ViewUtils.toggleExistence(false, views);
        }
    }

    public static void slideInRight(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
            slide.setDuration(duration);
            slide.setMode(Slide.MODE_IN);
            TransitionManager.beginDelayedTransition(rootView, slide);
            ViewUtils.toggleExistence(true, views);
        }
    }

    public static void slideOutBottom(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView, slide);
            ViewUtils.toggleExistence(false, views);
        }

    }

    public static void slideInBottom(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
            slide.setDuration(duration);
            slide.setMode(Slide.MODE_IN);
            TransitionManager.beginDelayedTransition(rootView, slide);
            ViewUtils.toggleExistence(true, views);
        }
    }

    public static void showExploding(ViewGroup rootView, View... views){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView, explode);
            ViewUtils.toggleExistence(true, views);
        }
    }

    public static void hideExploding(ViewGroup rootView, View... views){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView, explode);
            ViewUtils.toggleExistence(false, views);
        }
    }

    public static void hideFading(ViewGroup rootView, View... views){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView, fade);
            ViewUtils.toggleExistence(false, views);
        }
    }

    public static void showFading(ViewGroup rootView, View... views){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(duration);
            TransitionManager.beginDelayedTransition(rootView, fade);
            ViewUtils.toggleExistence(true, views);
        }
    }
}
