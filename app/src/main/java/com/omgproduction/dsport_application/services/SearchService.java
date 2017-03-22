package com.omgproduction.dsport_application.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JsResult;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.herbornsoftware.omnet.JSONRequest;
import com.herbornsoftware.omnet.JSONResponse;
import com.herbornsoftware.omnet.ResultConverter;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.SearchUser;
import com.omgproduction.dsport_application.models.User;
import com.omgproduction.dsport_application.utils.Converter;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.ResultWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.omgproduction.dsport_application.config.ResultKeys.RESULT_VALUE_VALUE;

/**
 * Created by Florian on 07.11.2016.
 */

public class SearchService extends AbstractService{

    public SearchService(Context context) {
        super(context);
    }

    public void searchQuery(final String localUserID, final String query, final IRequestFuture<SearchResultHolder> listener){

        listener.onStartQuery();

        try {
            final JSONRequest request = new JSONRequest(ROUTE_SEARCH_ALL)
                    .setDebug(true)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_QUERY, query)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if (response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToSearchResultHolderConverter()));
                            } else {
                                listener.onFailure(response.getResponseCode(),response.getResponseMessage());
                            }

                        }});

            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }


            //BackendRequest request = new BackendRequest(ROUTE_SEARCH_ALL)
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        })
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
        //                if(result.isOk()){
//
        //                    SearchResultHolder resultHolder = result.extractValue(ConverterFactory.createJsonToSearchResultHolderConverter());
//
        //                    listener.onFinishQuery();
        //                    if(resultHolder==null){
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        listener.onSuccess(resultHolder);
        //                    }
        //                }else{
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        })
        //        .param(APPLICATION_USER_USER_ID, localUserID)
        //        .param(APPLICATION_QUERY,query);
        //executeRequest(request.build());

    }

}
