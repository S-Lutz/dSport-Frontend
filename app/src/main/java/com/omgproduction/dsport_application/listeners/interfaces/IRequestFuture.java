package com.omgproduction.dsport_application.listeners.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

/**
 * @param <T> Type of Result
 */
public interface IRequestFuture<T> {
    /**
     * This Method is called on Start of the Method... Its the first method which is called
     */
    void onStartQuery();

    /**
     * This Method is called if the Query gets a good result
     * @param result Result
     */
    void onSuccess(T result);

    /**
     * This Method is called if the Backend sent some ErrorCode
     * @param errorCode The Code the Backend sent
     */
    void onFailure(String errorCode);

    /**
     * Called in EVERY Case the Method Finish... is Called before any other Method is called
     */
    void onFinishQuery();
}
