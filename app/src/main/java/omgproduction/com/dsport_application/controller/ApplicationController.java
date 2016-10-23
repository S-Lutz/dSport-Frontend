package omgproduction.com.dsport_application.controller;

/**
 * Created by Florian on 17.10.2016.
 *
 * APPController to Control App-Specific Data
 *
 * This is the first Class which is running on App-Start
 */

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationController extends Application {
    //Activity-Tag for Log
    public static final String TAG = ApplicationController.class.getSimpleName();
    //Requestqueue to handle HTTP-Connections
    private RequestQueue mRequestQueue;
    //ApplicationController instance to instanciate form other classes
    private static ApplicationController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    /**
     * Get an instance from this ApplicationController class to interact with it
     * @return
     */
    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    /**
     * Get RequestQueue to add HTTP-Connections
     * @return Global Request-Queue from Application
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        } return mRequestQueue;
    }

    /**
     * Add some HTTP-Request to Requestqueue with Tag (Tag is used to delete or get it later)
     * @param req HTTP-Request which shall be running
     * @param tag Tag to Iditify the Request later to get it or delete it
     * @param <T> Responsetyp of the Request
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Add some HTTP-Request to Requestqueue with Tag (Tag is used to delete or get it later)
     * @param req HTTP-Request which shall be running
     * @param <T> Responsetyp of the Request
     */
    public <T> void addToRequestQueue(Request<T>req) {
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
