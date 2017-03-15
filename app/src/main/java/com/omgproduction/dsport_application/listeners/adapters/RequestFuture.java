package com.omgproduction.dsport_application.listeners.adapters;

import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;

import org.json.JSONException;

/**
 * Created by Florian on 16.11.2016.
 */

public class RequestFuture<T> implements IRequestFuture<T> {
    @Override
    public void onStartQuery() {

    }

    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onFailure(int errorCode, String errorMessage) {

    }

    @Override
    public void onFinishQuery() {

    }
}
