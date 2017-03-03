package com.omgproduction.dsport_application.activities.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.config.ApplicationKeys;

public class WelcomeActivity extends Activity {

    String username;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_welcome);


        Intent i = getIntent();
        username = i.getStringExtra(ApplicationKeys.USER_USERNAME);
        Resources res = getResources();
        String text = res.getQuantityString(R.plurals.welcome_messages, 1, username);

        ((TextView)findViewById(R.id.welcome_message)).setText(text);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(ApplicationKeys.USER_USERNAME,username);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }
}
