package com.omgproduction.dsport_application.aaRefactored.connection;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;

import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class ListRequest<T> extends Request<ArrayList<T>>{


    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", "utf-8");
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final BackendCallback<ArrayList<T>> listener;
    private final String body;
    private Map<String, String> responseHeader;


    public ListRequest(String url, int method, Class<T> clazz, Object body, Map<String, String> headers, final BackendCallback<ArrayList<T>> listener) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse == null || networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    listener.onFailure(new ErrorResponse(401, "An error occurred"));
                }else{
                    listener.onFailure(new ErrorResponse(volleyError.networkResponse.statusCode, volleyError.getLocalizedMessage()));
                }
            }
        });

        this.body = gson.toJson(body);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<ArrayList<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Type listType = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
            ArrayList<T> tList = gson.fromJson(json, listType);
            responseHeader = response.headers;
            return Response.success(
                    tList,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<T> response) {
        listener.onSuccess(response, responseHeader);
    }

    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    public byte[] getBody() {
        try {
            return this.body == null?null:this.body.getBytes("utf-8");
        } catch (UnsupportedEncodingException var2) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", this.body, "utf-8");
            return null;
        }
    }
} 