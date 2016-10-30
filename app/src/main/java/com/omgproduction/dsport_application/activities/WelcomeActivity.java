package com.omgproduction.dsport_application.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Intent i = getIntent();
        String username = i.getStringExtra("username");
        String email = i.getStringExtra("email");
        //Resources res = getResources();
       // String text = String.format(res.getString(R.string.welcome_message), username, email);
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.welcome_messages, 1, username, email);

        ((TextView)findViewById(R.id.welcome_email)).setText(email);
        ((TextView)findViewById(R.id.welcome_username)).setText(username);
        ((TextView)findViewById(R.id.welcome_message)).setText(text);


    }

}
