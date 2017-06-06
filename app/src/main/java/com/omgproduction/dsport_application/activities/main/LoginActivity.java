package com.omgproduction.dsport_application.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;


/**
 * Created by Florian on 17.10.2016.
 *
 * Activity to Login the User
 * Login with username and Password
 */
public class LoginActivity extends AbstractFragmentActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        onCreateAfterPermission();
    }

    private void onCreateAfterPermission() {

        Log.e("LoginActivity", "started");

        setRefresher((SwipeRefreshLayout)findViewById(R.id.login_refresher));

        checkLogin();

        findViewById(R.id.registration_link).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);


        Intent i = getIntent();
        String username;
        if((username = i.getStringExtra(INTENT_USERNAME))!=null){
            ((EditText)findViewById(R.id.login_username)).setText(username);
        }
    }

    private void checkLogin() {
        User user = userService.getLocalUser();
        if(userService.isAvailable(user)){
            startMainActivity(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_link :
                startRegistrationActivity(this);
                break;
            case R.id.btn_login:
                loginUser();
                break;
        }
    }

    /**
     * Use this Funktion to login the User after Userinput
     */
    private void loginUser() {
        removeAllErrors();

        //Save userinput into String values
        String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

        //Check if Username is not Empty
        //Check if Password is not Empty
        if(username.trim().isEmpty()
                ||password.trim().isEmpty()){
            printInputError(R.id.login_layout_username, FIELD_EMPTY_ERROR);
            return;
        }


        //Process login with Backend
        //Send request to Backend and wait for response
        sessionService.validateUser(username, password, new RequestFuture<User>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(User user) {
                userService.saveLocalUser(user);
                startMainActivity(LoginActivity.this);
            }

            @Override
            public void onFailure(int errorCode,String errorMessage) {
                printInputError(R.id.login_layout_password,errorMessage);
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void startMainActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    private void startRegistrationActivity(Context context){
        Intent i = new Intent(context, RegisterActivity.class);
        i.putExtra(INTENT_USERNAME,((EditText)findViewById(R.id.login_username)).getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    /**
     * Remove all Error from View
     */
    protected void removeAllErrors(){
        removeInputError(R.id.login_layout_username);
        removeInputError(R.id.login_layout_password);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_activity_login;
    }

    @Override
    public void onRefresh() {
        showProgressBar(false);
    }
}
