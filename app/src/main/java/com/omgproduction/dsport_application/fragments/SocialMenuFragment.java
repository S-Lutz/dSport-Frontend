package com.omgproduction.dsport_application.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.interfaces.IFABMenu;
import com.omgproduction.dsport_application.listeners.adapters.AnimationAdapter;
import com.omgproduction.dsport_application.utils.Transitions;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMenuFragment extends Fragment implements IFABMenu{

    private FloatingActionButton camera, gallery, exercise_unit, event, text;
    private FloatingActionButton rootFab;

    public SocialMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_floating_menu_social,container,false);

        camera = (FloatingActionButton) v.findViewById(R.id.social_fab_picture);
        gallery = (FloatingActionButton) v.findViewById(R.id.social_fab_gallery);
        exercise_unit = (FloatingActionButton) v.findViewById(R.id.social_fab_exercise_unit);
        event = (FloatingActionButton) v.findViewById(R.id.social_fab_event);
        text = (FloatingActionButton) v.findViewById(R.id.social_fab_text);

        v.setVisibility(View.GONE);

        return v;
    }

    public void setRootFab(FloatingActionButton fab){
        rootFab = fab;
    }

    public void hide(){
        try{
            Animation fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_menu_close);
            camera.startAnimation(fab_close);
            gallery.startAnimation(fab_close);
            exercise_unit.startAnimation(fab_close);
            event.startAnimation(fab_close);
            text.startAnimation(fab_close);
            Transitions.hideFading((ViewGroup)getView(),getView());


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
            Animation rotate_clockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_clockwise);
            rootFab.startAnimation(rotate_clockwise);
        }catch (NullPointerException e){

        }
    }

    public void show(){
        try{
            Transitions.showFading((ViewGroup)getView(),getView());
            Animation fab_open = AnimationUtils.loadAnimation(getContext(),R.anim.fab_menu_open);
            camera.startAnimation(fab_open);
            gallery.startAnimation(fab_open);
            exercise_unit.startAnimation(fab_open);
            event.startAnimation(fab_open);
            text.startAnimation(fab_open);


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
            Animation rotate_anti_clockwise = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anti_clockwise);
            rootFab.startAnimation(rotate_anti_clockwise);

        }catch (NullPointerException e){

        }
    }
}
