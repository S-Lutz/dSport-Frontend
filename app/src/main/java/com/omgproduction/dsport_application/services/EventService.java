package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.omgproduction.dsport_application.builder.BackendRequest;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.ResultWrapper;
import com.omgproduction.dsport_application.models.Event;

import org.json.JSONObject;

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
        BackendRequest backendRequest = new BackendRequest(ROUTE_CREATE_EVENT)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .param(APPLICATION_EVENT_OWNER_ID, eventBoardOwnerID)
                .param(APPLICATION_EVENT_PICTURE, picture)
                .param(APPLICATION_EVENT_EVENT_PICTURE, eventPicture)
                .param(APPLICATION_EVENT_EVENT_DATE, eventDate)
                .param(APPLICATION_EVENT_TEXT, eventText)
                .param(APPLICATION_EVENT_TITLE, title)
                .param(APPLICATION_EVENT_LOCATION, location)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        listener.onFinishQuery();
                        if (result.isOk()){
                            listener.onSuccess(null);
                        }else{
                            listener.onFailure(result.extractErrorCode()
                            );
                        }

                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(backendRequest.build());
    }

    public void getEventBoard(final String userID, final String ownerID, final IRequestFuture<List<Event>> listener) {

        listener.onStartQuery();
        BackendRequest backendRequest = new BackendRequest(ROUTE_GET_EVENTBOARD)
                .param(APPLICATION_USER_USER_ID, userID)
                .param(APPLICATION_EVENT_OWNER_ID, ownerID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listener.onFinishQuery();

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()) {
                            List<Event> events = result.extractArray(ConverterFactory.createJsonToEventConverter(), APPLICATION_EVENTS);
                            listener.onSuccess(null);
                            if(events==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(events);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(backendRequest.build());
    }

    public void getAllEvents(final String localUserID, final IRequestFuture<List<Event>> listener) {
        listener.onStartQuery();
        BackendRequest backendRequest = new BackendRequest(ROUTE_GET_EVENTS)
                .param(APPLICATION_USER_USER_ID,localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){
                            List<Event> events = result.extractArray(ConverterFactory.createJsonToEventConverter(), APPLICATION_EVENTS);
                            if(events == null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(events);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(backendRequest.build());

    }

    public void likeEvent(final String localUserID, final String event_id, final IRequestFuture<LikeResult> listener) {
        listener.onStartQuery();
        BackendRequest request = new BackendRequest(ROUTE_LIKE_EVENT)
                .param(APPLICATION_EVENT_EVENT_ID,event_id)
                .param(APPLICATION_USER_USER_ID, localUserID)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        ResultWrapper result = new ResultWrapper(context, jsonObject);

                        if(result.isOk()){
                            LikeResult likeResult = result.extractValue(ConverterFactory.createJsonToEventLikeResultConverter());

                            if(likeResult==null){
                                listener.onFinishQuery();
                                listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                            }else {
                                listener.onFinishQuery();
                                listener.onSuccess(likeResult);
                            }
                        }else{
                            listener.onFinishQuery();
                            listener.onFailure(result.extractErrorCode());
                        }
                    }
                })
                .errorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onFinishQuery();
                        listener.onFailure(BACKEND_SOMETHING_WENT_WRONG_ERROR);
                    }
                });

        executeRequest(request.build());
    }

    public void getEventDetail(final String localUserID, final String event_id, final IRequestFuture<Event> listener) {

    }


    public void getAllComments(String event_id, RequestFuture<List<Comment>> requestFuture) {
    }

    public void getAllLikes(String event_id, RequestFuture<List<Like>> requestFuture) {
    }

    public void createComment(String id, String event_id, String picture, String text, RequestFuture<Void> requestFuture) {
    }

    public void likeComment(String id, String comment_id, RequestFuture<LikeResult> requestFuture) {
    }
}
