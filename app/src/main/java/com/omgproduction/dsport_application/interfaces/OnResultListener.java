package com.omgproduction.dsport_application.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * Created by Florian Herborn on 16.11.2016.
 */

public interface OnResultListener<T> {
    void onStart();
    void onSuccess(T result);
    void onConnectionError(VolleyError error);
    void onBackendError(String ErrorCode, String ErrorString);
    void onJSONError(JSONException e);
    void onUserNotFound();
    void onResult();
}
