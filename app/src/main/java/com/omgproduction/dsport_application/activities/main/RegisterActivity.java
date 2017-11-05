package com.omgproduction.dsport_application.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.omgproduction.dsport_application.aaRefactored.views.LoadingView;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.views.CheckedEditText;
import com.omgproduction.dsport_application.activities.helper.WelcomeActivity;
import com.omgproduction.dsport_application.services.SessionService;

import static com.omgproduction.dsport_application.config.IntentKeys.INTENT_USERNAME;

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
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private SessionService sessionService;
    private LoadingView loadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_activity_register);
        findViewById(R.id.signup_link).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        sessionService = new SessionService(this);

        loadingView  = (LoadingView) findViewById(R.id.loading_view);
        //loadingView.hide();

        Intent i = getIntent();
        String username;
        if((username = i.getStringExtra(INTENT_USERNAME))!=null){
            ((CheckedEditText)findViewById(R.id.register_username)).setText(username);
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
        i.putExtra(INTENT_USERNAME,((EditText)findViewById(R.id.register_username)).getText().toString());
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

        //Save User-Input in String values
        //Check if Username is Empty. Show Error below the Input-Field


        CheckedEditText usernameET = (CheckedEditText) findViewById(R.id.register_username);
        if(!usernameET.checkRequired())  return;

        //Check if Firstname is Empty. Show Error below the Input-Field
        CheckedEditText firstnameET = (CheckedEditText) findViewById(R.id.register_firstname);
        if(!firstnameET.checkRequired())  return;

        //Check if Lastname is Empty. Show Error below the Input-Field
        CheckedEditText lastnameET = (CheckedEditText) findViewById(R.id.register_lastname);
        if(!lastnameET.checkRequired())  return;

        //Check if Email is Empty. Show Error below the Input-Field
        CheckedEditText emailET = (CheckedEditText) findViewById(R.id.register_email);
        if(!emailET.checkRequired() || !emailET.checkEmail())  return;


        CheckedEditText password_1ET = (CheckedEditText) findViewById(R.id.register_password);
        CheckedEditText password_2ET = (CheckedEditText) findViewById(R.id.register_password_confirm);

        if(!password_1ET.checkRequired() || !password_2ET.checkRequired() || !password_1ET.checkContentEquals(password_2ET)) return;



        CheckBox agbCB = (CheckBox) findViewById(R.id.cb_agb);
        if(!agbCB.isChecked()){
            Toast.makeText(this, "Please read the AGB and accept it to go on!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Process Registration with Backend-Server
        loadingView.show();
        //sessionService.registerUser(new RegistrationUser(usernameET.getTextString(), emailET.getTextString(), firstnameET.getTextString(), lastnameET.getTextString(), password_1ET.getTextString()), new BackendCallback<RegistrationNode>() {
        //    @Override
        //    public void onSuccess(RegistrationNode result, Map<String, String> responseHeader) {
        //        loadingView.hide();
        //        startWelcomeActivity(result.getUsername());
        //    }
//
        //    @Override
        //    public void onFailure(ErrorResponse error) {
        //        loadingView.hide();
        //        Toast.makeText(RegisterActivity.this,error.getErrorMessage(), Toast.LENGTH_SHORT).show();
        //    }
        //});
    }

    private void startWelcomeActivity(String username) {
        Intent i = new Intent(this, WelcomeActivity.class);
        i.putExtra(INTENT_USERNAME,username);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        startLoginActivity(this);
    }


}
