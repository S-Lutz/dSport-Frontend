package com.omgproduction.dsport_application.supplements.activities;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;


/**
 * Created by Florian on 21.10.2016.
 */
public abstract class AdvancedActivity extends Activity implements View.OnClickListener{
    /**
     * Hide all Input-Fields and show Progress-bar instead
     */
    protected void showProgressBar(int containerIDToHide, int progressBarId){
        findViewById(containerIDToHide).setVisibility(ProgressBar.GONE);
        findViewById(progressBarId).setVisibility(ProgressBar.VISIBLE);
    }

    /**
     * Hide Progressbar and show all Input-Fields instead
     */
    protected void hideProgressBar(int containerIDToShow, int progressBarId){
        findViewById(containerIDToShow).setVisibility(ProgressBar.VISIBLE);
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

}
