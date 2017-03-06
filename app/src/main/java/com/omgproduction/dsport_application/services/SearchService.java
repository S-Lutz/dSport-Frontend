package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.holder.SearchResultHolder;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.ResultWrapper;

import org.json.JSONObject;

/**
 * Created by Florian on 07.11.2016.
 */

public class SearchService extends AbstractService{

    public SearchService(Context context) {
        super(context);
    }

    public void searchQuery(final String localUserID, final String query, final IRequestFuture<SearchResultHolder> listener){

        listener.onStartQuery();

        BackendRequest request = new BackendRequest(ROUTE_SEARCH_ALL)
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                })
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ResultWrapper result = new ResultWrapper(context, jsonObject);
                        if(result.isOk()){

                            SearchResultHolder resultHolder = result.extractValue(ConverterFactory.createJsonToSearchResultHolderConverter());

                            listener.onFinishQuery();
                            if(resultHolder==null){
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onSuccess(resultHolder);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .param(APPLICATION_USER_USER_ID, localUserID)
                .param(APPLICATION_QUERY,query);
        executeRequest(request.build());

    }

}
