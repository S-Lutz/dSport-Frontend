package com.omgproduction.dsport_application.aaRefactored.services;

import android.content.Context;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.aaRefactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.aaRefactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.aaRefactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.aaRefactored.models.resultnodes.SocialResultPair;
import com.omgproduction.dsport_application.utils.Converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class EventService {
    
    public void createEvent(Context context, EventNode eventNode, BackendCallback<EventNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateCreateEventRoute(), EventNode.class, eventNode,PreferencesService.getToken(context), callback);
    }

    public void uploadPicture(Context context, File imgFile, EventNode eventNode, BackendCallback<EventNode> callback) {
        BackendRequestExecutor.executePutPictureRequest(RouteGenerator.generateUploadEventFileRoute(eventNode.getId()), EventNode.class, imgFile, null, PreferencesService.getToken(context), callback);
    }

    public void getEvents(Context context, long userId, BackendCallback<ArrayList<SocialResultPair>> callback){
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetEventsRoute(userId), SocialResultPair.class, null, PreferencesService.getToken(context), callback,
                new Converter<JSONObject, SocialResultPair>() {
                    @Override
                    public SocialResultPair convert(JSONObject input) {
                        try {
                            Gson gson = new Gson();

                            SocialResultPair socialResultPair = new SocialResultPair();
                            socialResultPair.setUserNode(gson.fromJson(input.getJSONObject("userNode").toString(), UserNode.class));
                            socialResultPair.setType(input.getString("type"));
                            socialResultPair.setLikesSocialNode(input.getBoolean("likesSocialNode"));

                            if(socialResultPair.getType().equals("EVENT")){
                                socialResultPair.setSocialNode(gson.fromJson(input.getJSONObject("socialNode").toString(), EventNode.class));
                            }
                            return socialResultPair;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                });

    }


}