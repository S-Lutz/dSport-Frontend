package com.omgproduction.dsport_application.refactored.services;

import android.content.Context;

import com.google.gson.Gson;
import com.omgproduction.dsport_application.refactored.connection.BackendRequestExecutor;
import com.omgproduction.dsport_application.refactored.connection.RouteGenerator;
import com.omgproduction.dsport_application.refactored.helper.Converter;
import com.omgproduction.dsport_application.refactored.listeners.BackendCallback;
import com.omgproduction.dsport_application.refactored.models.nodes.CommentNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;
import com.omgproduction.dsport_application.refactored.models.resultnodes.SocialResultPair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class CommentService {

    public void getComments(Context context, Long socialNodeId, BackendCallback<ArrayList<SocialResultPair>> callback) {
        BackendRequestExecutor.executePostListRequest(RouteGenerator.generateGetCommentsRoute(socialNodeId), SocialResultPair.class, null, PreferencesService.getToken(context), callback,
                new Converter<JSONObject, SocialResultPair>() {
                    @Override
                    public SocialResultPair convert(JSONObject input) {
                        try {
                            Gson gson = new Gson();

                            SocialResultPair socialResultPair = new SocialResultPair();
                            socialResultPair.setUserNode(gson.fromJson(input.getJSONObject("userNode").toString(), UserNode.class));
                            socialResultPair.setType(input.getString("type"));
                            socialResultPair.setLikesSocialNode(input.getBoolean("likesSocialNode"));

                            if(socialResultPair.getType().equals("COMMENTNODE")){
                                socialResultPair.setSocialNode(gson.fromJson(input.getJSONObject("socialNode").toString(), CommentNode.class));
                            }
                            return socialResultPair;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                });
    }

    public void createComment(Context context, Long socialNodeId, CommentNode commentNode, BackendCallback<CommentNode> callback){
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateCreateCommentRoute(socialNodeId), CommentNode.class, commentNode,PreferencesService.getToken(context), callback);
    }

    public void uploadPicture(Context context, File imgFile, CommentNode commentNode, BackendCallback<CommentNode> callback) {
        BackendRequestExecutor.executePutPictureRequest(RouteGenerator.generateUploadCommentFileRoute(commentNode.getId()), CommentNode.class, imgFile, null, PreferencesService.getToken(context), callback);
    }
}