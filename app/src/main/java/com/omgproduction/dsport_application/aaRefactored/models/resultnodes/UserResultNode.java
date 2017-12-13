package com.omgproduction.dsport_application.aaRefactored.models.resultnodes;

import com.omgproduction.dsport_application.aaRefactored.models.nodes.UserNode;

public class UserResultNode extends UserNode {

    private Boolean isFriend;

    private Boolean hasRequest;

    private Boolean isRequested;

    public Boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Boolean isFriend) {
        this.isFriend = isFriend;
    }

    public Boolean getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Boolean hasRequest) {
        this.hasRequest = hasRequest;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public Boolean getIsRequested() {
        return isRequested;
    }

    public void setRequested(Boolean requested) {
        isRequested = requested;
    }
}