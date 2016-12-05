package com.omgproduction.dsport_application.listeners.adapters;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;

import org.json.JSONException;

/**
 * Created by Florian on 16.11.2016.
 */

public class OnResultAdapter<T> implements OnResultListener<T> {
    @Override
    public void onStartQuery() {

    }

    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onConnectionError(VolleyError e) {

    }

    @Override
    public void onBackendError(String errorCode) {

    }

    @Override
    public void onJSONException(JSONException e) {

    }

    @Override
    public void onUserNotFound() {

    }

    @Override
    public void onFinishQuery() {

    }
}
