package com.omgproduction.dsport_application.refactored.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.refactored.helper.GlideApp;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.services.PreferencesService;

public class FullImageActivity extends AppCompatActivity {

    private AppCompatImageView imgFullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);


        imgFullImage = (AppCompatImageView) findViewById(R.id.new_fullscreen_pic);

        Bundle bundle = getIntent().getExtras();
        String imageUrl = bundle.getString("imageUrl");

       GlideApp
               .with(this)
               .load(new Gson().fromJson(PreferencesService.getSharedPreferencesUser(this), UserNode.class).getPicture())
               .fitCenter()
               .into(imgFullImage);
    }

}
