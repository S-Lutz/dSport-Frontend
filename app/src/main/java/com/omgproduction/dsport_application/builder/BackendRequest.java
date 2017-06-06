package com.omgproduction.dsport_application.builder;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.omgproduction.dsport_application.config.ResultKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florian on 21.10.2016.
 */
public class BackendRequest implements Response.Listener<JSONObject>, Response.ErrorListener, ResultKeys{

    private static String TAG = BackendRequest.class.getSimpleName();

    private JsonObjectRequest request;

    private int method = Request.Method.POST;
    private String url;
    private JSONObject jsonObject = new JSONObject();
    private Response.Listener<JSONObject> responseListener;
    private Response.ErrorListener errorListener;
    private String content_type = "application/json";
    private Map<String, Object> params;

    public BackendRequest(String url) {
        this.url = url;
        params = new HashMap<>();
    }

    public BackendRequest method(int method) {
        this.method = method;
        return this;
    }

    public BackendRequest jsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public BackendRequest contentType(String content_type){
        this.content_type = content_type;
        return this;
    }

    public BackendRequest param(String key, Object value){
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BackendRequest responseListener(Response.Listener<JSONObject> responseListener) {
        this.responseListener = responseListener;
        return this;
    }

    public BackendRequest errorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public JsonObjectRequest build(){

        Log.e("BUILD REQUEST",url);

        request = new JsonObjectRequest(method,url, jsonObject,this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //Creating JSON Header
                headers.put(RESULT_CONTENT_TYPE_KEY, content_type);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = super.getParams();
                params.putAll(BackendRequest.this.params);
                return params;
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
        Log.e(TAG, "Response: "+jsonObject.toString());
        if(responseListener!=null){
            responseListener.onResponse(jsonObject);
        }
    }
}
