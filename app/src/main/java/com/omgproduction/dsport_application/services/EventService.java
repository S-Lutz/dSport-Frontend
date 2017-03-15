package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.herbornsoftware.omnet.JSONRequest;
import com.herbornsoftware.omnet.JSONResponse;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.models.Event;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Florian on 21.10.2016.
 */
public class EventService extends AbstractService{

    public EventService(Context context) {
        super(context);
    }

    public void createEvent(final String localUserID, final String eventBoardOwnerID,final String picture, final String eventPicture, final String eventText, final String title, final String eventDate, final String location, final IRequestFuture<Void> listener) {

        listener.onStartQuery();

        try {
            final JSONRequest request = new JSONRequest(ROUTE_CREATE_EVENT)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EVENT_OWNER_ID, eventBoardOwnerID)
                    .addParam(APPLICATION_EVENT_PICTURE, picture)
                    .addParam(APPLICATION_EVENT_EVENT_PICTURE, eventPicture)
                    .addParam(APPLICATION_EVENT_EVENT_DATE, eventDate)
                    .addParam(APPLICATION_EVENT_TEXT, eventText)
                    .addParam(APPLICATION_EVENT_TITLE, title)
                    .addParam(APPLICATION_EVENT_LOCATION, location)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
                            }else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }


        //BackendRequest backendRequest = new BackendRequest(ROUTE_CREATE_EVENT)
        //        .param(APPLICATION_USER_USER_ID, localUserID)
        //        .param(APPLICATION_EVENT_OWNER_ID, eventBoardOwnerID)
        //        .param(APPLICATION_EVENT_PICTURE, picture)
        //        .param(APPLICATION_EVENT_EVENT_PICTURE, eventPicture)
        //        .param(APPLICATION_EVENT_EVENT_DATE, eventDate)
        //        .param(APPLICATION_EVENT_TEXT, eventText)
        //        .param(APPLICATION_EVENT_TITLE, title)
        //        .param(APPLICATION_EVENT_LOCATION, location)
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
//
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                listener.onFinishQuery();
        //                if (result.isOk()){
        //                    listener.onSuccess(null);
        //                }else{
        //                    listener.onFailure(result.extractErrorCode());
        //                }
//
        //            }
        //        })
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        });
//
        //executeRequest(backendRequest.build());
    }

    public void getEventBoard(final String userID, final String ownerID, final IRequestFuture<List<Event>> listener) {

        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_EVENTBOARD)
                    .addParam(APPLICATION_USER_USER_ID, userID)
                    .addParam(APPLICATION_EVENT_OWNER_ID, ownerID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToEventConverter(), APPLICATION_EVENTS));
                            }else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }


        //BackendRequest backendRequest = new BackendRequest(ROUTE_GET_EVENTBOARD)
        //        .param(APPLICATION_USER_USER_ID, userID)
        //        .param(APPLICATION_EVENT_OWNER_ID, ownerID)
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
        //                listener.onFinishQuery();
//
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                if(result.isOk()) {
        //                    List<Event> events = result.extractArray(ConverterFactory.createJsonToEventConverter(), APPLICATION_EVENTS);
        //                    listener.onSuccess(null);
        //                    if(events==null){
        //                        listener.onFinishQuery();
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        listener.onFinishQuery();
        //                        listener.onSuccess(events);
        //                    }
        //                }else{
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        })
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        });
//
        //executeRequest(backendRequest.build());
    }

    public void getAllEvents(final String localUserID, final IRequestFuture<List<Event>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_EVENTS)
                    .addParam(APPLICATION_USER_USER_ID,localUserID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToEventConverter(),APPLICATION_EVENTS));
                            }else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }


        //BackendRequest backendRequest = new BackendRequest(ROUTE_GET_EVENTS)
        //        .param(APPLICATION_USER_USER_ID,localUserID)
        //        .responseListener(new Response.Listener<JSONObject>() {
        //            @Override
        //            public void onResponse(JSONObject jsonObject) {
        //                ResultWrapper result = new ResultWrapper(context, jsonObject);
//
        //                if(result.isOk()){
        //                    List<Event> events = result.extractArray(ConverterFactory.createJsonToEventConverter(), APPLICATION_EVENTS);
        //                    if(events == null){
        //                        listener.onFinishQuery();
        //                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //                    }else {
        //                        listener.onFinishQuery();
        //                        listener.onSuccess(events);
        //                    }
        //                }else{
        //                    listener.onFinishQuery();
        //                    listener.onFailure(result.extractErrorCode());
        //                }
        //            }
        //        })
        //        .errorListener(new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                listener.onFinishQuery();
        //                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
        //            }
        //        });
//
        //executeRequest(backendRequest.build());

    }


}
