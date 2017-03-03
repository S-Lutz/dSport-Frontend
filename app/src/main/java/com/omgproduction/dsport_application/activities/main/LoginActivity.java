package com.omgproduction.dsport_application.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.ErrorCodes;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.supplements.activities.AbstractFragmentActivity;

import org.json.JSONObject;


/**
 * Created by Florian on 17.10.2016.
 *
 * Activity to Login the User
 * Login with username and Password
 */
public class LoginActivity extends AbstractFragmentActivity implements ApplicationKeys{


    public SessionService sessionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);

        setRefresher((SwipeRefreshLayout)findViewById(R.id.login_refresher));

        sessionService = new SessionService(this);

        checkLogin();

        findViewById(R.id.registration_link).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);


        Intent i = getIntent();
        String username;
        if((username = i.getStringExtra(APPLICATION_USER_USERNAME))!=null){
            ((EditText)findViewById(R.id.login_username)).setText(username);
        }

    }

    private void checkLogin() {
        if(sessionService.checkLogin()){
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
            printInputError(R.id.login_layout_username, ErrorCodes.FIELD_EMPTY);
            return;
        }


        //Process login with Backend
        //Send request to Backend and wait for response
        sessionService.loginUser(this,username, password, new RequestFuture<JSONObject>(){
            @Override
            public void onStartQuery() {
                showProgressBar(true);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                SessionService.getInstance().saveLocalUser(context,jsonObject, new RequestFuture<User>(){
                    @Override
                    public void onStartQuery() {
                        showProgressBar(true);
                    }

                    @Override
                    public void onSuccess(User result) {
                        startMainActivity(context);
                    }

                    @Override
                    public void onFinishQuery() {
                        showProgressBar(false);
                    }
                });
            }

            @Override
            public void onConnectionError(VolleyError e) {
                printError(R.id.login_layout,ErrorCodes.BACKEND_CONNECTION_FAILED, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginUser();
                    }
                });
            }

            @Override
            public void onFailure(String errorCode) {
                //Check Errorcode (See it in error_codes.xml
                switch (errorCode){
                    case "e303": printInputError(R.id.login_layout_password,errorCode); break;
                    default: printError(R.id.login_layout,ErrorCodes.SOMETHING_WENT_WRONG);
                }
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
        i.putExtra(ApplicationKeys.APPLICATION_USER_USERNAME,((EditText)findViewById(R.id.login_username)).getText().toString());
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
    public void onRefresh() {

    }
}
