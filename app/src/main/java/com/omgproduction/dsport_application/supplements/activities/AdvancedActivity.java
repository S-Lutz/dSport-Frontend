package com.omgproduction.dsport_application.supplements.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.utils.BitmapUtils;


/**
 * Created by Florian on 21.10.2016.
 */
public abstract class AdvancedActivity extends FragmentActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    protected SwipeRefreshLayout refresher;

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
    public void setRefresher(SwipeRefreshLayout refresher){
        this.refresher = refresher;
        refresher.setOnRefreshListener(this);
    }

    public void showProgressBar(boolean flag){
        if(refresher!=null){
            refresher.setRefreshing(flag);
        }
    }
}
