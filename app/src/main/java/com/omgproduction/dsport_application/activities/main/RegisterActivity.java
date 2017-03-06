package com.omgproduction.dsport_application.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.helper.WelcomeActivity;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.ConnectionErrorCodes;
import com.omgproduction.dsport_application.config.LocalErrorCodes;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;
import com.omgproduction.dsport_application.utils.StringUtils;

/**
 * Created by Florian on 17.10.2016.
 *
 * RegisterActivity where the User can Sign-Up
 *
 * Sign-Up with
 * Username
 * Firstname
 * Lastname
 * Email
 * Password
 * Password_confirm
 * Accept AGB
 */
public class RegisterActivity extends AbstractFragmentActivity implements ConnectionErrorCodes{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_register);

        setRefresher((SwipeRefreshLayout)findViewById(R.id.register_refresher));

        findViewById(R.id.signup_link).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        Intent i = getIntent();
        String username;
        if((username = i.getStringExtra(INTENT_USERNAME))!=null){
            ((EditText)findViewById(R.id.register_username)).setText(username);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_link :
                startLoginActivity(this);
                break;
            case R.id.btn_register :
                registerUser();
                break;
            
        }
    }


    private void startLoginActivity(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra(ApplicationKeys.APPLICATION_USER_USERNAME,((EditText)findViewById(R.id.register_username)).getText().toString());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(i);
    }

    /**
     * Use this Method to Register an User
     */
    private void registerUser() {
        removeAllErrors();

        //Save User-Input in String values
        String username = ((EditText)findViewById(R.id.register_username)).getText().toString();
        String email = ((EditText)findViewById(R.id.register_email)).getText().toString();
        String firstname = ((EditText)findViewById(R.id.register_firstname)).getText().toString();
        String lastname = ((EditText)findViewById(R.id.register_lastname)).getText().toString();
        String password_1 = ((EditText)findViewById(R.id.register_password)).getText().toString();
        String password_2 = ((EditText)findViewById(R.id.register_password_confirm)).getText().toString();
        boolean accepted = ((CheckBox) findViewById(R.id.cb_agb)).isChecked();

        //Check if Username is Empty. Show Error below the Input-Field
        if(username.trim().isEmpty()){
            printInputError(R.id.register_layout_username, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Firstname is Empty. Show Error below the Input-Field
        if(firstname.trim().isEmpty()){
            printInputError(R.id.register_layout_firstname, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Lastname is Empty. Show Error below the Input-Field
        if(lastname.trim().isEmpty()){
            printInputError(R.id.register_layout_lastname, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Email is Empty. Show Error below the Input-Field
        if(email.trim().isEmpty()) {
            printInputError(R.id.register_layout_email, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Password 1 is Empty. Show Error below the Input-Field
        if(password_1.trim().isEmpty()){
            printInputError(R.id.register_layout_password, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Password 2 is Empty. Show Error below the Input-Field
        if(password_2.trim().isEmpty()){
            printInputError(R.id.register_layout_password_confirm, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if Password 1 equals Password 2. Show Error below the Input-Field
        if(!password_1.equals(password_2)){
            printInputError(R.id.register_layout_password, FIELD_EMPTY_ERROR);
            printInputError(R.id.register_layout_password_confirm, FIELD_EMPTY_ERROR);
            return;
        }
        //Check if AGB is Accepted. Show Error in Snackbar
        if(!accepted){
            printError(R.id.register_layout, ACCEPT_AGB_ERROR);
            return;
        }
        //Check if Email is valid. Show Error below the Input-Field
        if(!StringUtils.isValidEmail(email)){
            printInputError(R.id.register_layout_email, INVALID_EMAIL_ERROR);
            return;
        }

        //Show Progressbar
        showProgressBar(true);

        //Process Registration with Backend-Server
        sessionService.registerUser(username, firstname, lastname, email, password_1, new RequestFuture<String>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
                removeAllErrors();
            }

            @Override
            public void onSuccess(String result) {
                startWelcomeActivity(result);
            }

            @Override
            public void onFailure(String errorCode) {
                switch (errorCode){
                    case USERNAME_ALREADY_EXISTS_ERROR: printInputError(R.id.register_layout_username,errorCode); break;
                    case EMAIL_ALREADY_EXISTS_ERROR: printInputError(R.id.register_layout_email,errorCode); break;
                    //On any other Error print Universal-Error e0
                    default: printError(R.id.register_layout, LocalErrorCodes.SOMETHING_WENT_WRONG_ERROR);
                }
            }

            @Override
            public void onFinishQuery() {
                showProgressBar(false);
            }
        });
    }

    private void startWelcomeActivity(String username) {
        Intent i = new Intent(this, WelcomeActivity.class);
        i.putExtra(ApplicationKeys.APPLICATION_USER_USERNAME,username);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    protected void removeAllErrors(){
        removeInputError(R.id.register_layout_firstname);
        removeInputError(R.id.register_layout_lastname);
        removeInputError(R.id.register_layout_password);
        removeInputError(R.id.register_layout_password_confirm);
        removeInputError(R.id.register_layout_username);
        removeInputError(R.id.register_layout_email);
    }

    @Override
    public void onBackPressed() {
        startLoginActivity(this);
    }

    @Override
    public void onRefresh() {

    }
}
