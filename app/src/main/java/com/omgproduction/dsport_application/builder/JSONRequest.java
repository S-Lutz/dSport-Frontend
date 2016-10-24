package com.omgproduction.dsport_application.builder;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florian on 21.10.2016.
 */
public class JSONRequest implements Response.Listener<JSONObject>, Response.ErrorListener{

    private static String TAG = JSONRequest.class.getSimpleName();

    private JsonObjectRequest request;

    private int method = Request.Method.POST;
    private String url;
    private JSONObject jsonObject = new JSONObject();
    private Response.Listener<JSONObject> responseListener;
    private Response.ErrorListener errorListener;

    public JSONRequest(String url) {
        this.url = url;
    }

    public JSONRequest method(int method) {
        this.method = method;
        return this;
    }

    public JSONRequest jsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public JSONRequest param(String key, Object value){
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JSONRequest responseListener(Response.Listener<JSONObject> responseListener) {
        this.responseListener = responseListener;
        return this;
    }

    public JSONRequest errorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public JsonObjectRequest build(){
        request = new JsonObjectRequest(method,url, jsonObject,responseListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //Creating JSON Header
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        return request;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
        if(errorListener!=null){
            errorListener.onErrorResponse(volleyError);
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        Log.i(TAG, "Response: "+jsonObject.toString());
        if(responseListener!=null){
            responseListener.onResponse(jsonObject);
        }
    }
}
