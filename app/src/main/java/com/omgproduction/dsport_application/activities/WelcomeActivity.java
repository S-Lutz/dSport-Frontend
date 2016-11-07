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
import android.widget.EditText;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.Keys;

public class WelcomeActivity extends Activity {

    String username;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Intent i = getIntent();
        username = i.getStringExtra(Keys.USERNAME);
        email = i.getStringExtra(Keys.EMAIL);
        //Resources res = getResources();
       // String text = String.format(res.getString(R.string.welcome_message), username, email);
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.welcome_messages, 1, username, email);

        ((TextView)findViewById(R.id.welcome_message)).setText(text);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(Keys.USERNAME,username);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }
}
