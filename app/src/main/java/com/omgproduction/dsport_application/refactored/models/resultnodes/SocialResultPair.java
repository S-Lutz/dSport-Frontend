package com.omgproduction.dsport_application.refactored.models.resultnodes;

import com.omgproduction.dsport_application.refactored.models.nodes.SocialNode;
import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;

public class SocialResultPair {

    private boolean likesSocialNode;

    private SocialNode socialNode;
    private UserNode userNode;
    private String type;

    public SocialNode getSocialNode() {
        return socialNode;
    }

    public void setSocialNode(SocialNode socialNode) {
        this.socialNode = socialNode;
    }

    public UserNode getUserNode() {
        return userNode;
    }

    public void setUserNode(UserNode userNode) {
        this.userNode = userNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLikesSocialNode() {
        return likesSocialNode;
    }

    public void setLikesSocialNode(boolean likesSocialNode) {
        this.likesSocialNode = likesSocialNode;
    }

    public void switchState() {
        if (likesSocialNode)
            likesSocialNode = false;
        else likesSocialNode = true;
    }
}