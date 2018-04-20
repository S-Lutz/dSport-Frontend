package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;

import com.omgproduction.dsport_application.refactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.PostNode;

import java.io.File;

public class PostService {

    public void uploadPicture(Context context, File imgFile, PostNode postNode, BackendCallback<PostNode> callback) {
        BackendRequestExecutor.executePutPictureRequest(RouteGenerator.generateUploadPostFileRoute(postNode.getId()), PostNode.class, imgFile, null, PreferencesService.getToken(context), callback);
    }

    public void createPost(Context context, Long userId, PostNode postNode, BackendCallback<PostNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateCreatePostRoute(userId), PostNode.class, postNode,PreferencesService.getToken(context), callback);
    }
}