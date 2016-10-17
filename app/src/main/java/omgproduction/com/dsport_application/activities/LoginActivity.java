package omgproduction.com.dsport_application.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
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
 * Activity to Login the User
 * Login with username and Password
 */
public class LoginActivity extends Activity implements View.OnClickListener{

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
        showProgressBar();

        //Process login with Backend
        //Send request to Backend and wait for response
        BackendController.getInstance().loginUser(username, password, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    //Check if Backend send any Errors
                    if(jsonObject.getString("error").equals("OK")){
                        //If no Errors hide Progress-Bar
                        hideProgressBar();
                        //Save userlogin in SQLite
                        JSONObject user = jsonObject.getJSONObject("value");
                        new SessionController(context).createLoginSession(
                                user.getString("user_id"),
                                user.getString("email"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("created"),
                                user.getString("agbversion"),
                                user.getString("picture"));
                    }else{
                        //If some internal Errors hide Progressbar
                        hideProgressBar();

                        //Errorhandling for Internal Errors
                        String errorCode = jsonObject.getString("value");

                        //Check Errorcode (See it in error_codes.xml
                        switch (errorCode){
                            case "e303": printInputError(R.id.login_layout_password,errorCode); break;
                            default: printError("e0");
                        }
                    }
                } catch (JSONException e) {
                    //If some JSON Error print e0 (Universal Error)
                    e.printStackTrace();
                    hideProgressBar();
                    printError("e0");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //If some Connection error print e100 Connection failed
                hideProgressBar();
                printError("e100", R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loginUser();
                    }
                });
            }
        });

    }

    /**
     * Hide all Input-Fields and show Progress-bar instead
     */
    private void showProgressBar(){
        findViewById(R.id.login_input_container).setVisibility(ProgressBar.GONE);
        findViewById(R.id.progress_bar).setVisibility(ProgressBar.VISIBLE);
    }

    /**
     * Hide Progressbar and show all Input-Fields instead
     */
    private void hideProgressBar(){
        findViewById(R.id.login_input_container).setVisibility(ProgressBar.VISIBLE);
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
                .make(findViewById(R.id.login_layout), getString(resId), Snackbar.LENGTH_LONG);

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
                .make(findViewById(R.id.login_layout), getString(resId), Snackbar.LENGTH_LONG)
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
        removeInputError(R.id.login_layout_username);
        removeInputError(R.id.login_layout_password);
    }
}
