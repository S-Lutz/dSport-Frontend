package com.omgproduction.dsport_application.supplements.activities;

import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Florian on 06.11.2016.
 */

public abstract class AdvancedAppCompatActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * Hide all Input-Fields and show Progress-bar instead
     */
    protected void showProgressBar(int containerIDToHide, int progressBarId){
        showProgressBar(findViewById(containerIDToHide),progressBarId);
    }
    /**
     * Hide all Input-Fields and show Progress-bar instead
     */
    protected void showProgressBar(View containerToHide, int progressBarId){
        containerToHide.setVisibility(ProgressBar.GONE);
        findViewById(progressBarId).setVisibility(ProgressBar.VISIBLE);
    }

    /**
     * Hide Progressbar and show all Input-Fields instead
     */
    protected void hideProgressBar(int containerIDToShow, int progressBarId){
        hideProgressBar(findViewById(containerIDToShow),progressBarId);
    }

    /**
     * Hide Progressbar and show all Input-Fields instead
     */
    protected void hideProgressBar(View containerToShow, int progressBarId){
        containerToShow.setVisibility(ProgressBar.VISIBLE);
        findViewById(progressBarId).setVisibility(ProgressBar.GONE);
    }
    /**
     * show Progress-bar
     */
    protected void showProgressBar(int progressBarId){
        findViewById(progressBarId).setVisibility(ProgressBar.VISIBLE);
    }

    /**
     * Hide Progressbar
     */
    protected void hideProgressBar(int progressBarId){
        findViewById(progressBarId).setVisibility(ProgressBar.GONE);
    }

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    protected void printError(int layoutID, String errorCode){
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(findViewById(layoutID), getString(resId), Snackbar.LENGTH_LONG);

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
                .make(findViewById(layoutID), getString(resId), Snackbar.LENGTH_LONG)
                .setAction(getString(buttonLabelId), listener);

        snackbar.show();
    }

    /**
     * Print some Error in Helper-Field from android.support.design.widget.TextInputLayout
     * @param id Ressource id from Layout-Element in xml-Layout
     * @param errorCode Errorcode to print Error-Messsage (See in error_code)
     */
    protected void printInputError(int id, String errorCode){
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
    protected void removeInputError(int id){
        TextInputLayout til = (TextInputLayout) findViewById(id);
        til.setErrorEnabled(false);
    }

    /**
     * Remove all Error from View
     */
    protected abstract void removeAllErrors();

    /**
     * Set Text to a TextView with id
     * @param id id of the TextView
     * @param text text to put in
     */
    protected void setText(int id, String text){
        ((TextView)findViewById(id)).setText(text);
    }

    /**
     * Set Text to a TextView with id
     * @param id id of the TextView
     */
    protected String getTVText(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
    /**
     * Set Drawable to a ImageView with id
     * @param id id of the ImageView
     * @param drawable id of Drawable
     */
    protected void setPic(int id, int drawable){
        ((ImageView)findViewById(id)).setImageDrawable(getResources().getDrawable(drawable));
    }
    /**
     * Set Bitmap to a ImageView with id
     * @param id id of the ImageView
     * @param bitmap bitmap to put in
     */
    protected void setPic(int id, Bitmap bitmap){
        ((ImageView)findViewById(id)).setImageBitmap(bitmap);
    }
}
