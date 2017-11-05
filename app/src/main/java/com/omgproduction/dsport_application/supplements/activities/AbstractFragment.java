package com.omgproduction.dsport_application.supplements.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.config.IntentKeys;
import com.omgproduction.dsport_application.config.LocalErrorCodes;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.services.SessionService;
import com.omgproduction.dsport_application.services.UserService;

/**
 * Created by Florian on 17.11.2016.
 */

public abstract class AbstractFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, LocalErrorCodes, IntentKeys{

    private SwipeRefreshLayout refresher;

    protected UserService userService;
    protected SessionService sessionService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionService = new SessionService(getContext());
        userService = new UserService(getContext());

    }

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    protected void printError(View rootView, View layout, String errorCode){
        //String packageName = rootView.getContext().getPackageName();
        //int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(layout, errorCode, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * Print some Error with Snackbar with Button
     * @param errorCode ErrorCode (See in error_codes)
     * @param buttonLabelId StringID for Button Label
     * @param listener OnClickListener for Button-Click
     */
    protected void printError(View rootView, View layout, String errorCode, int buttonLabelId, View.OnClickListener listener){
        //String packageName = rootView.getContext().getPackageName();
        //int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(layout, errorCode, Snackbar.LENGTH_LONG)
                .setAction(getString(buttonLabelId), listener);

        snackbar.show();
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

    @Override
    public void onClick(View v) {

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
        sessionService.logout();
    }
}
