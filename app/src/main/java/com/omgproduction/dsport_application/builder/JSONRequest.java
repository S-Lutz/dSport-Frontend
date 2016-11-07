package com.omgproduction.dsport_application.builder;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.omgproduction.dsport_application.config.Keys;

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
    private String content_type = "application/json";

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

    public JSONRequest contentType(String content_type){
        this.content_type = content_type;
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
        request = new JsonObjectRequest(method,url, jsonObject,this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //Creating JSON Header
                headers.put(Keys.CONTENT_TYPE, content_type);
                return headers;
            }
        };
        return request;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
        Log.i(TAG, "Response: "+volleyError.toString());
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
