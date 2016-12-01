package com.omgproduction.dsport_application.supplements.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.omgproduction.dsport_application.R;

/**
 * Created by Florian on 17.11.2016.
 */

public abstract class AdvancedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private SwipeRefreshLayout refresher;

    /**
     * Print some Error with Snackbar but Without any Control-Element
     * @param errorCode ErrorCode (See in error_codes)
     */
    protected void printError(View rootView, View layout, String errorCode){
        String packageName = rootView.getContext().getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(layout, getString(resId), Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * Print some Error with Snackbar with Button
     * @param errorCode ErrorCode (See in error_codes)
     * @param buttonLabelId StringID for Button Label
     * @param listener OnClickListener for Button-Click
     */
    protected void printError(View rootView, View layout, String errorCode, int buttonLabelId, View.OnClickListener listener){
        String packageName = rootView.getContext().getPackageName();
        int resId = getResources().getIdentifier(errorCode, "string", packageName);
        Snackbar snackbar = Snackbar
                .make(layout, getString(resId), Snackbar.LENGTH_LONG)
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
}
