package omgproduction.com.dsport_application.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import omgproduction.com.dsport_application.R;
import omgproduction.com.dsport_application.controller.SessionController;
import omgproduction.com.dsport_application.utils.ConnectionUtils;

/**
 * Created by Florian on 17.10.2016.
 *
 * Activity to Login the User
 * Login with username and Password
 */
public class LoginActivity extends BasicActivity {

    //Activity-Context
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        findViewById(R.id.registration_link).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registration_link :
                startActivity(new Intent(context, RegisterActivity.class));
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
        if(username.trim().isEmpty()){
            printInputError(R.id.login_layout_username,"e2");
            return;
        }
        //Check if Password is not Empty
        if(password.trim().isEmpty()){
            printInputError(R.id.login_layout_password,"e2");
            return;
        }

        //Start showing the Progressbar
        showProgressBar(R.id.login_input_container,R.id.progress_bar);

        //Process login with Backend
        //Send request to Backend and wait for response
        SessionController.getInstance().loginUser(this,username, password, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                //hide Progressbar
                hideProgressBar(R.id.login_input_container,R.id.progress_bar);
                //Check if Backend send any Errors
                if(ConnectionUtils.Success(jsonObject)){
                }else{
                    //Errorhandling for Internal Errors
                    String errorCode = ConnectionUtils.extractErrorCode(jsonObject);

                    //Check Errorcode (See it in error_codes.xml
                    switch (errorCode){
                        case "e303": printInputError(R.id.login_layout_password,errorCode); break;
                        default: printError(R.id.login_layout,"e0");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //If some Connection error print e100 Connection failed
                hideProgressBar(R.id.login_input_container,R.id.progress_bar);
                printError(R.id.login_layout,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginUser();
                    }
                });
            }
        });

    }

    /**
     * Remove all Error from View
     */
    protected void removeAllErrors(){
        removeInputError(R.id.login_layout_username);
        removeInputError(R.id.login_layout_password);
    }
}
