package com.omgproduction.dsport_application.services;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.omgproduction.dsport_application.config.ApplicationConfig;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.ErrorCodes;
import com.omgproduction.dsport_application.config.NotificationKeys;
import com.omgproduction.dsport_application.config.Routes;
import com.omgproduction.dsport_application.controller.App;

/**
 * Created by Florian on 03.03.2017.
 */

public abstract class AbstractService<T extends AbstractService> implements Routes, ApplicationKeys, ErrorCodes,NotificationKeys, ApplicationConfig {
    protected final Context context;
    //Requestqueue to handle HTTP-Connections
    private static RequestQueue mRequestQueue;
    //Activity-Tag for Log
    public static final String TAG = AbstractService.class.getSimpleName();

    public AbstractService(Context context){
        this.context = context;
    }

    /**
     * Get RequestQueue to add HTTP-Connections
     * @return Global Request-Queue from Application
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(App.getContext());
        } return mRequestQueue;
    }

    /**
     * Add some HTTP-Request to Requestqueue with Tag (Tag is used to delete or get it later)
     * @param req HTTP-Request which shall be running
     * @param tag Tag to Iditify the Request later to get it or delete it
     * @param <T> Responsetyp of the Request
     */
    public <T> void executeRequest(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Add some HTTP-Request to Requestqueue with Tag (Tag is used to delete or get it later)
     * @param req HTTP-Request which shall be running
     * @param <T> Responsetyp of the Request
     */
    public <T> void executeRequest(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancel an request with its TAG
     * @param tag tag from Request which shall be deleted
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
