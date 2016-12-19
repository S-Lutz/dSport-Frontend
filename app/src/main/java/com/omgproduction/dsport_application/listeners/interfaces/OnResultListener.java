package com.omgproduction.dsport_application.listeners.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

/**
 * @param <T> Type of Result
 */
public interface OnResultListener<T> {
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
     * This Method is called if some Connection-Error (Volley Error) occurs
     * @param e The Volley Exception
     */
    void onConnectionError(VolleyError e);

    /**
     * This Method is called if the Backend sent some ErrorCode
     * @param errorCode The Code the Backend sent
     */
    void onBackendError(String errorCode);

    /**
     * This Method is called if some JSOMException occurs
     * @param e
     */
    void onJSONException(JSONException e);

    /**
     * This Method is called if the local User isnt found... Normally you have to logout the Session here
     */
    void onUserNotFound();

    /**
     * Called in EVERY Case the Method Finish... is Called before any other Method is called
     */
    void onFinishQuery();
}
