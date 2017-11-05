package com.omgproduction.dsport_application.supplements.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.config.CameraOptions;
import com.omgproduction.dsport_application.config.IntentKeys;
import com.omgproduction.dsport_application.config.LocalErrorCodes;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.models.backendModels.UserNode;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.services.UserService;

/**
 * Created by Florian on 06.11.2016.
 */

public abstract class AbstractAppCompatActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LocalErrorCodes, IntentKeys, CameraOptions {

    protected SwipeRefreshLayout refresher;

    protected SessionService sessionService;
    protected UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionService = new SessionService(this);
        userService = new UserService(this);
    }

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    protected void printError(int layoutID, String errorCode){
        //String packageName = getPackageName();
        //int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(layoutID), errorCode, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * Print some Error with Snackbar with Button
     * @param errorCode ErrorCode (See in error_codes)
     * @param buttonLabelId StringID for Button Label
     * @param listener OnClickListener for Button-Click
     */
    protected void printError(int layoutID, String errorCode, int buttonLabelId, View.OnClickListener listener){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(layoutID), errorCode, Snackbar.LENGTH_LONG)
                .setAction(getString(buttonLabelId), listener);

        snackbar.show();
    }

    /**
     * Print some Error in Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout
     * @param errorCode Errorcode to print Error-Messsage (See in error_code)
     */
    protected void printInputError(int id, String errorCode){
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(true);
        til.setError(errorCode);
    }

    /**
     * Remove some Error from Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout which has the Error
     */
    protected void removeInputError(int id){
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(false);
    }

    /**
     * Remove all Error from View
     */
    protected abstract void removeAllErrors();

    /**
     * WeightSet Text to a TextView with id
     * @param id id of the TextView
     * @param text text to put in
     */
    protected void setText(int id, String text){
        ((TextView)findViewById(id)).setText(text);
    }

    /**
     * WeightSet Text to a TextView with id
     * @param id id of the TextView
     */
    protected String getTVText(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
    /**
     * WeightSet Drawable to a ImageView with id
     * @param id id of the ImageView
     * @param drawable id of Drawable
     */
    protected void setPic(int id, int drawable){
        ((ImageView)findViewById(id)).setImageDrawable(getResources().getDrawable(drawable));
    }
    /**
     * WeightSet Bitmap to a ImageView with id
     * @param id id of the ImageView
     * @param bitmap bitmap to put in
     */
    protected void setPic(int id, Bitmap bitmap){
        ((ImageView)findViewById(id)).setImageBitmap(bitmap);
    }
    public void setRefresher(SwipeRefreshLayout refresher){
        this.refresher = refresher;
        refresher.setOnRefreshListener(this);
    }

    public void showProgressBar(boolean flag){
        if(refresher!=null){
            refresher.setRefreshing(flag);
        }
    }

    protected User getLocalUser(){
        User user = new User(null,"","","","","","","","");

        if(userService.isAvailable(user)){
            return user;
        }
        Log.e("FragmentActivity", "USER NOT FOUND");
        logoutUser();
        return user;
    }

    protected void logoutUser(){
        userService.logout();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
