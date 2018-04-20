package com.omgproduction.dsport_application.refactored.connection;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.omgproduction.dsport_application.refactored.App;
import com.omgproduction.dsport_application.refactored.helper.Converter;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackendRequestExecutor {

    private static RequestQueue requestQueue = Volley.newRequestQueue(App.getInstance().getApplicationContext());
    private static String DEFAULT_TAG = UUID.randomUUID().toString();
    private static List<String> pendingTags = new ArrayList<>();


    public static <T> SimpleRequest<T> executeGetRequest(String url, Class<T> clazz, BackendCallback<T> listener) {
        return executeGetRequest(url, clazz, listener, DEFAULT_TAG);
    }

    public static <T> SimpleRequest<T> executeGetRequest(String url, Class<T> clazz, BackendCallback<T> listener, String tag) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.POST, clazz, null, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    public static <T> SimpleRequest<T> executePostRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executePostRequest(url, clazz, body, listener, DEFAULT_TAG);
    }

    public static <T> SimpleRequest<T> executePostRequest(String url, Class<T> clazz, Object body ,Map<String, String> customHeader, BackendCallback<T> listener) {
        return executePostRequest(url, clazz, body, customHeader, listener, DEFAULT_TAG);
    }

    public static <T> SimpleRequest<T> executePostRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.POST, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    public static <T> SimpleRequest<T> executePostRequest(String url, Class<T> clazz, Object body,Map<String, String> customHeader, BackendCallback<T> listener, String tag) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.POST, clazz, body, customHeader, listener);
        addToRequestQueue(request, tag);
        return request;
    }


    public static <T> SimpleRequest<T> executePutRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executePutRequest(url, clazz, body, listener, DEFAULT_TAG);
    }

    public static <T> SimpleRequest<T> executePutRequest(String url, Class<T> clazz, Object body, Map<String, String> customHeader, BackendCallback<T> listener) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.PUT, clazz, body, customHeader, listener);
        addToRequestQueue(request, DEFAULT_TAG);
        return request;
    }

    public static <T> SimpleRequest<T> executePutRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.PUT, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }



    public static <T> MultipartRequest<T> executePutPictureRequest(String url, Class<T> clazz, File imgFile, Object body, Map<String, String> customHeader, BackendCallback<T> callback) {
        MultipartRequest<T> request = new MultipartRequest<>(url, clazz, imgFile, body, customHeader, callback);
        addToRequestQueue(request, DEFAULT_TAG);
        return request;
    }


    public static <T> SimpleRequest<T> executeDeleteRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executeDeleteRequest(url, clazz, body, listener, DEFAULT_TAG);
    }


    public static <T> SimpleRequest<T> executeDeleteRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        SimpleRequest<T> request = new SimpleRequest<>(url, Request.Method.DELETE, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }


    public static StringRequest executeStringRequest(String url, Map<String, String> customHeader, BackendCallback<String> listener) {
        StringRequest request = new StringRequest(Request.Method.POST, url, customHeader, listener);
        addToRequestQueue(request, DEFAULT_TAG);
        return request;
    }


    public static <T> ListRequest<T> executePostListRequest(String url, Class<T> clazz, Object body, Map<String, String> customHeader, BackendCallback<ArrayList<T>> listener) {
        ListRequest<T> request = new ListRequest<>(url, Request.Method.POST, clazz, body, customHeader, listener);
        addToRequestQueue(request, DEFAULT_TAG);
        return request;
    }

    public static <T> ListRequest<T> executePostListRequest(String url, Class<T> clazz, Object body, Map<String, String> customHeader, BackendCallback<ArrayList<T>> listener, Converter<JSONObject, T> converter) {
        ListRequest<T> request = new ListRequest<>(url, Request.Method.POST, clazz, body, customHeader, listener, converter);
        addToRequestQueue(request, DEFAULT_TAG);
        return request;
    }


    private static Map<String, String> createHeader() {
        return Collections.emptyMap();
    }

    private static <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, DEFAULT_TAG);
    }

    private static <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        pendingTags.add(tag);
        req.setTag(tag);
        requestQueue.add(req);
    }

    public static void cancelPendingRequests() {
        cancelPendingRequests(pendingTags.toArray(new String[pendingTags.size()]));
    }

    public static void cancelPendingRequests(String... tags) {
        for (String tag : tags) {
            requestQueue.cancelAll(tag);
            pendingTags.remove(tag);
        }
    }
} 