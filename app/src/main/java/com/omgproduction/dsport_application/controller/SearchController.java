package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.activities.main.SearchResultActivity;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.BackendConfig;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.listeners.interfaces.OnResultListener;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.ConnectionUtils;
import com.omgproduction.dsport_application.utils.Converter;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.value;

/**
 * Created by Florian on 07.11.2016.
 */

public class SearchController {
    private static SearchController instance;
    private SearchController () {}
    public static synchronized SearchController getInstance () {
        if (SearchController.instance == null) {
            SearchController.instance = new SearchController ();
        }
        return SearchController.instance;
    }

    public void searchQuery(final String localUserID, final String query, final OnResultListener<SearchResultHolder> listener){

        listener.onStartQuery();

        JSONRequest request = new JSONRequest(BackendConfig.SEARCH_ALL)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onConnectionError(volleyError);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            listener.onFinishQuery();
                            try {
                                listener.onSuccess(Converter.convertSearchResult(ConnectionUtils.extractJSONValue(jsonObject)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onJSONException(e);
                            }
                        }else{
                            listener.onBackendError(ConnectionUtils.extractErrorCode(jsonObject));
                        }
                    }
                })
                .param(ApplicationKeys.USER_USER_ID, localUserID)
                .param(ApplicationKeys.QUERY,query);
        ApplicationController.getInstance().addToRequestQueue(request.build());

    }

}
