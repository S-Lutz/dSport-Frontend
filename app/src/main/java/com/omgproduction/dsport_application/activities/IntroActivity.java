package com.omgproduction.dsport_application.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.omgproduction.dsport_application.R;

public class IntroActivity extends AppCompatActivity {

    AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        imageView=(AppCompatImageView) findViewById(R.id.intro_img);

        Animation animationIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_animation_in);
        imageView.setAnimation(animationIn);

        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
