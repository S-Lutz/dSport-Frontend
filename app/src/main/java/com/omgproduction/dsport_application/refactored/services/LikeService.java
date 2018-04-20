package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;

import com.omgproduction.dsport_application.refactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.EventNode;
import com.omgproduction.dsport_application.refactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;

import java.util.ArrayList;

public class LikeService {

    public void getLikes(Context context, Long socialNodeId, BackendCallback<ArrayList<UserNode>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetLikesRoute(socialNodeId), UserNode.class, null, PreferencesService.getToken(context), callback);
    }

    public void likeSocialNode(Context context, Long socialNodeId, BackendCallback<SocialNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateLikesRoute(socialNodeId), SocialNode.class, null,PreferencesService.getToken(context), callback);
    }

    public void participateEvent(Context context, Long eventId, BackendCallback<EventNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateParticipateRoute(eventId), EventNode.class, null,PreferencesService.getToken(context), callback);
    }
}