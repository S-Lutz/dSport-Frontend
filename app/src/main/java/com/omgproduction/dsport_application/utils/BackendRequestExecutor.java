package com.omgproduction.dsport_application.utils;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.omgproduction.dsport_application.controller.App;
import com.omgproduction.dsport_application.listeners.callbacks.BackendCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackendRequestExecutor {


    private static RequestQueue requestQueue = Volley.newRequestQueue(App.getContext());
    private static String DEFAULT_TAG = UUID.randomUUID().toString();
    private static List<String> pendingTags = new ArrayList<>();


    public static <T> ParseRequest<T> executeGetRequest(String url, Class<T> clazz, BackendCallback<T> listener) {
        return executeGetRequest(url, clazz, listener, DEFAULT_TAG);
    }

    public static <T> ParseRequest<T> executeGetRequest(String url, Class<T> clazz, BackendCallback<T> listener, String tag) {
        //TODO Fix Request.Method.GET causes error in backend!
        ParseRequest<T> request = new ParseRequest<>(url, Request.Method.POST, clazz, null, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    public static <T> ParseRequest<T> executePostRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executePostRequest(url, clazz, body, listener, DEFAULT_TAG);
    }


    public static <T> ParseRequest<T> executePostRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        ParseRequest<T> request =new ParseRequest<>(url, Request.Method.POST, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    public static <T> ParseRequest<T> executePutRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executePutRequest(url, clazz, body, listener, DEFAULT_TAG);
    }


    public static <T> ParseRequest<T> executePutRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        ParseRequest<T> request =new ParseRequest<>(url, Request.Method.PUT, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    public static <T> ParseRequest<T> executeDeleteRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener) {
        return executeDeleteRequest(url, clazz, body, listener, DEFAULT_TAG);
    }


    public static <T> ParseRequest<T> executeDeleteRequest(String url, Class<T> clazz, Object body, BackendCallback<T> listener, String tag) {
        ParseRequest<T> request =new ParseRequest<>(url, Request.Method.DELETE, clazz, body, createHeader(), listener);
        addToRequestQueue(request, tag);
        return request;
    }

    private static Map<String, String> createHeader() {
        return Collections.emptyMap();
    }

    private static <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, DEFAULT_TAG);
    }

    private static <T> void addToRequestQueue(Request<T> req, String tag) {
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