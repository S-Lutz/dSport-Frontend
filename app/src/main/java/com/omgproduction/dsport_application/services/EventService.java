package com.omgproduction.dsport_application.services;

import android.content.Context;

import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.Comment;
import com.omgproduction.dsport_application.models.Event;
import com.omgproduction.dsport_application.models.Like;
import com.omgproduction.dsport_application.models.LikeResult;
import com.omgproduction.dsport_application.models.Participate;
import com.omgproduction.dsport_application.models.ParticipateResult;
import com.omgproduction.dsport_application.utils.ConverterFactory;
import com.omgproduction.dsport_application.utils.DateConverter;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;

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
                    .addParam(APPLICATION_EVENT_CREATED, new DateConverter().convertNow())
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

    }

    public void likeEvent(final String localUserID, final String event_id, final IRequestFuture<LikeResult> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_LIKE_EVENT)
                    .addParam(APPLICATION_EVENT_EVENT_ID,event_id)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToEventLikeResultConverter()));
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

    }

    public void getEventDetail(final String localUserID, final String event_id, final IRequestFuture<Event> listener) {
       listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_EVENT_DETAIL)
                    .addParam(APPLICATION_EVENT_EVENT_ID, event_id)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if (response.isOk()) {
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToEventConverter()));
                            } else {
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();

        }
    }


    public void getAllComments(String event_id,final RequestFuture<List<Comment>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_EVENT_COMMENTS)
                    .addParam(APPLICATION_EVENT_EVENT_ID,event_id)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToCommentConverter(), APPLICATION_COMMENTS));
                            }else{
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }
    }

    public void getAllLikes(String event_id, final RequestFuture<List<Like>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_EVENT_LIKES)
                    .addParam(APPLICATION_EVENT_EVENT_ID,event_id)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToLikeConverter(), APPLICATION_LIKES));
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
    }

    public void createComment(String localUserID, String event_id, String picture, String text, final RequestFuture<Void> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_COMMENT_EVENT)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .addParam(APPLICATION_EVENT_EVENT_ID, event_id)
                    .addParam(APPLICATION_COMMENT_PICTURE, picture)
                    .addParam(APPLICATION_COMMENT_TEXT, text)
                    .addParam(APPLICATION_COMMENT_CREATED, new DateConverter().convertNow())
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(null);
                            }else{
                                listener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFinishQuery();
        }

    }

    public void likeComment(String localUserID, String comment_id,final RequestFuture<LikeResult> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_LIKE_COMMENT)
                    .addParam(APPLICATION_COMMENT_ID,comment_id)
                    .addParam(APPLICATION_USER_USER_ID, localUserID)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getValue(ConverterFactory.createJsonToCommentLikeResultConverter()));
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
    }

   public void participateEvent(String localUserID, String event_id,final RequestFuture<ParticipateResult> listener){
       listener.onStartQuery();

       try {
           JSONRequest request = new JSONRequest(ROUTE_PARTICIPATE_EVENT)
                   .addParam(APPLICATION_EVENT_EVENT_ID,event_id)
                   .addParam(APPLICATION_USER_USER_ID, localUserID)
                   .setOnResultListener(new JSONRequest.OnResultListener() {
                       @Override
                       public void onResult(JSONResponse response) {
                           listener.onFinishQuery();
                           if(response.isOk()){
                               listener.onSuccess(response.getValue(ConverterFactory.createJsonToEventParticipateResultConverter()));
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

   }

    public void getAllMember(String event_id, final RequestFuture<List<Participate>> listener) {
        listener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_GET_MEMBERS)
                    .addParam(APPLICATION_EVENT_EVENT_ID,event_id)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            listener.onFinishQuery();
                            if(response.isOk()){
                                listener.onSuccess(response.getArray(ConverterFactory.createJsonToParticipateConverter(), APPLICATION_MEMBERS));
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
    }
}
