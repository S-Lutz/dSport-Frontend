package com.omgproduction.dsport_application.refactored.helper;

import android.support.v4.view.GravityCompat;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.omgproduction.dsport_application.R;


public class SlideAnimationUtil {

    private static final int duration = 300;

    public static void slideInFromRight(ViewGroup rootView, View... views){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(GravityCompat.getAbsoluteGravity(GravityCompat.END, rootView.getResources().getConfiguration().getLayoutDirection()));
            slide.setDuration(duration);
            slide.setMode(Slide.MODE_IN);
            TransitionManager.beginDelayedTransition(rootView, slide);
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

    public static void slideOutLeft(final ViewGroup rootView, View... views) {

        long delayBetweenAnimations = 100l;

        for (int i = 0; i < views.length; i++) {
            final View view = views[i];

            // We calculate the delay for this Animation, each animation starts 100ms
            // after the previous one
            long delay = i * delayBetweenAnimations;

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_out_from_left);
                    view.startAnimation(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            }, delay);

        }
    }

}