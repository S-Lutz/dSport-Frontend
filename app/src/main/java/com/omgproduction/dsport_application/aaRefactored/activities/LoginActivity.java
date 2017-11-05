package com.omgproduction.dsport_application.aaRefactored.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.intent.keys.LoginRegisterIntentKeys;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.services.UserService;
import com.omgproduction.dsport_application.aaRefactored.views.CheckedEditText;
import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;
import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;

import java.util.Map;


/**
 * Created by Florian on 17.10.2016.
 * <p>
 * Activity to Login the User
 * Login with username and Password
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private UserService userService;
    private LoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_activity_login);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

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


            this.userService = new UserService();
            loadingView = (LoadingView) findViewById(R.id.loading_view);
        }

        onCreateAfterPermission();
    }

    private void onCreateAfterPermission() {

        if (userService.isLoggedIn(this)) {
            startMainActivity(this);
        }

        findViewById(R.id.registration_link).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        Intent i = getIntent();
        String username;
        if ((username = i.getStringExtra(LoginRegisterIntentKeys.USERNAME_KEY)) != null) {
            ((CheckedEditText) findViewById(R.id.login_username)).setText(username);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_link:
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

        CheckedEditText usernameET = (CheckedEditText) findViewById(R.id.login_username);
        if (!usernameET.checkRequired()) return;

        CheckedEditText passwordET = (CheckedEditText) findViewById(R.id.login_password);
        if (!passwordET.checkRequired()) return;

        loadingView.show();

        //Process login with Backend
        //Send request to Backend and wait for response
        userService.login(this, new UserNode(usernameET.getTextString(), passwordET.getTextString()), new BackendCallback<UserNode>() {
            @Override
            public void onSuccess(UserNode result, Map<String, String> responseHeader) {
                loadingView.hide();
                startMainActivity(LoginActivity.this);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                loadingView.hide();
                Toast.makeText(LoginActivity.this, error.getErrorMessage() + " " + error.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startMainActivity(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    private void startRegistrationActivity(Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        i.putExtra(LoginRegisterIntentKeys.USERNAME_KEY, ((CheckedEditText) findViewById(R.id.login_username)).getTextString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}
