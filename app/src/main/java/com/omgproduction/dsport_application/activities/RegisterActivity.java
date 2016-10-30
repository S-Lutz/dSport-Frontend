package com.omgproduction.dsport_application.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.SessionController;
import com.omgproduction.dsport_application.utils.ConnectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
public class RegisterActivity extends AdvancedActivity {

    //Activity Context
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        findViewById(R.id.signup_link).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        Intent i = getIntent();
        String username;
        if((username = i.getStringExtra("username"))!=null){
            ((EditText)findViewById(R.id.register_username)).setText(username);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_link :
                goToLoginActivity(this);
                break;
            case R.id.btn_register :
                registerUser();
                break;
            
        }
    }


    private void goToLoginActivity(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra("username",((EditText)findViewById(R.id.register_username)).getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            printInputError(R.id.register_layout_username,"e2");
            return;
        }
        //Check if Firstname is Empty. Show Error below the Input-Field
        if(firstname.trim().isEmpty()){
            printInputError(R.id.register_layout_firstname,"e2");
            return;
        }
        //Check if Lastname is Empty. Show Error below the Input-Field
        if(lastname.trim().isEmpty()){
            printInputError(R.id.register_layout_lastname,"e2");
            return;
        }
        //Check if Email is Empty. Show Error below the Input-Field
        if(email.trim().isEmpty()) {
            printInputError(R.id.register_layout_email, "e2");
            return;
        }
        //Check if Password 1 is Empty. Show Error below the Input-Field
        if(password_1.trim().isEmpty()){
            printInputError(R.id.register_layout_password,"e2");
            return;
        }
        //Check if Password 2 is Empty. Show Error below the Input-Field
        if(password_2.trim().isEmpty()){
            printInputError(R.id.register_layout_password_confirm,"e2");
            return;
        }
        //Check if Password 1 equals Password 2. Show Error below the Input-Field
        if(!password_1.equals(password_2)){
            printInputError(R.id.register_layout_password,"e1");
            printInputError(R.id.register_layout_password_confirm,"e1");
            return;
        }
        //Check if AGB is Accepted. Show Error in Snackbar
        if(!accepted){
            printError(R.id.register_layout,"e4");
            return;
        }
        //Check if Email is valid. Show Error below the Input-Field
        if(!isValidEmail(email)){
            printInputError(R.id.register_layout_email,"e3");
            return;
        }

        //Show Progressbar
        showProgressBar(R.id.register_input_container,R.id.progress_bar);

        //Process Registration with Backend-Server
        SessionController.getInstance().registerUser(username, firstname, lastname, email, password_1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    //Check if Backend send some Errors
                    if(ConnectionUtils.Success(jsonObject)){
                        //If no Backend Errors hide Progressbar and Logout user to comeback to Login Activity and delete Session-Data
                        hideProgressBar(R.id.register_input_container,R.id.progress_bar);
                        removeAllErrors();
                        SessionController.getInstance().logoutUser(context);
                        startWelcomeActivity(ConnectionUtils.extractJSONValue(jsonObject));
                    }else{
                        //If some Backend Error hide Progressbar and handle Error
                        hideProgressBar(R.id.register_input_container,R.id.progress_bar);
                        String errorCode = jsonObject.getString("value");
                        //Check Error-Code (See in error_codes.xml
                        switch (errorCode){
                            case "e301": printInputError(R.id.register_layout_username,errorCode); break;
                            case "e302": printInputError(R.id.register_layout_email,errorCode); break;
                            //On any other Error print Universal-Error e0
                            default: printError(R.id.register_layout,"e0");
                        }
                    }
                } catch (JSONException e) {
                    //If some JSON_Error hide Progressbar print Universal-Error e0
                    e.printStackTrace();
                    hideProgressBar(R.id.register_input_container,R.id.progress_bar);
                    printError(R.id.register_layout,"e0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //If some Connection Error hide Progressbar and print Connection-Failed-Error e100 in Snackbar with Retry-Button
                hideProgressBar(R.id.register_input_container,R.id.progress_bar);
                printError(R.id.register_layout,"e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //On Click Retry, retry Register
                        registerUser();
                    }
                });

            }
        });
    }

    private void startWelcomeActivity(JSONObject jsonObject) {
        try {
            String username = jsonObject.getString("username");
            String email = jsonObject.getString("email");

            Intent i = new Intent(this, WelcomeActivity.class);
            i.putExtra("username",username);
            i.putExtra("email",email);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void removeAllErrors(){
        removeInputError(R.id.register_layout_firstname);
        removeInputError(R.id.register_layout_lastname);
        removeInputError(R.id.register_layout_password);
        removeInputError(R.id.register_layout_password_confirm);
        removeInputError(R.id.register_layout_username);
        removeInputError(R.id.register_layout_email);
    }

    /**
     * Check if Email is Valid. User Util-Patterns from Anroid
     * @param email Email to Check
     * @return True id Email is a Valid email and False if Email isnt a Valid Email
     */
    public final static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }





}
