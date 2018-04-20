package com.omgproduction.dsport_application.refactored.connection;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class StringRequest extends com.android.volley.toolbox.StringRequest {

    private final Map<String, String> headers;

    public StringRequest(int method, String url, Map<String, String> headers, final BackendCallback<String> callback) {
        super(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailure(new ErrorResponse(volleyError.networkResponse.statusCode, volleyError.getMessage()));
            }
        });

        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}