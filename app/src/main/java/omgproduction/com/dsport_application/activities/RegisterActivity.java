package omgproduction.com.dsport_application.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import omgproduction.com.dsport_application.R;
import omgproduction.com.dsport_application.controller.BackendController;
import omgproduction.com.dsport_application.controller.SessionController;

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
public class RegisterActivity extends Activity implements View.OnClickListener{

    //Activity Context
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        findViewById(R.id.signup_link).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_link :
                startActivity(new Intent(context, LoginActivity.class));
                break;
            case R.id.btn_register :
                registerUser();
                break;
            
        }
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
            printError("e4");
            return;
        }
        //Check if Email is valid. Show Error below the Input-Field
        if(!isValidEmail(email)){
            printInputError(R.id.register_layout_email,"e3");
            return;
        }

        //Show Progressbar
        showProgressBar();

        //Process Registration with Backend-Server
        BackendController.getInstance().registerUser(username, firstname, lastname, email, password_1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    //Check if Backend send some Errors
                    if(jsonObject.getString("error").equals("OK")){
                        //If no Backend Errors hide Progressbar and Logout user to comeback to Login Activity and delete Session-Data
                        hideProgressBar();
                        removeAllErrors();
                        new SessionController(context).logoutUser();
                    }else{
                        //If some Backend Error hide Progressbar and handle Error
                        hideProgressBar();
                        String errorCode = jsonObject.getString("value");
                        //Check Error-Code (See in error_codes.xml
                        switch (errorCode){
                            case "e301": printInputError(R.id.register_layout_username,errorCode); break;
                            case "e302": printInputError(R.id.register_layout_email,errorCode); break;
                            //On any other Error print Universal-Error e0
                            default: printError("e0");
                        }
                    }
                } catch (JSONException e) {
                    //If some JSON_Error hide Progressbar print Universal-Error e0
                    e.printStackTrace();
                    hideProgressBar();
                    printError("e0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //If some Connection Error hide Progressbar and print Connection-Failed-Error e100 in Snackbar with Retry-Button
                hideProgressBar();
                printError("e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //On Click Retry, retry Register
                        registerUser();
                    }
                });

            }
        });
    }

    /**
     * Hide all Input-Fields and show Progress-bar instead
     */
    private void showProgressBar(){
        findViewById(R.id.register_input_container).setVisibility(ProgressBar.GONE);
        findViewById(R.id.progress_bar).setVisibility(ProgressBar.VISIBLE);
    }

    /**
     * Hide Progressbar and show all Input-Fields instead
     */
    private void hideProgressBar(){
        findViewById(R.id.register_input_container).setVisibility(ProgressBar.VISIBLE);
        findViewById(R.id.progress_bar).setVisibility(ProgressBar.GONE);
    }

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    private void printError(String errorCode){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.register_layout), getString(resId), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * Print some Error with Snackbar with Button
     * @param errorCode ErrorCode (See in error_codes)
     * @param buttonLabelId StringID for Button Label
     * @param listener OnClickListener for Button-Click
     */
    private void printError(String errorCode, int buttonLabelId, View.OnClickListener listener){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.register_layout), getString(resId), Snackbar.LENGTH_LONG)
                .setAction(getString(buttonLabelId), listener);

        snackbar.show();
    }

    /**
     * Print some Error in Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout
     * @param errorCode Errorcode to print Error-Messsage (See in error_code)
     */
    private void printInputError(int id, String errorCode){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(true);
        til.setError(getString(resId));
    }

    /**
     * Remove some Error from Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout which has the Error
     */
    private void removeInputError(int id){
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(false);
    }

    /**
     * Remove all Error from View
     */
    private void removeAllErrors(){
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
