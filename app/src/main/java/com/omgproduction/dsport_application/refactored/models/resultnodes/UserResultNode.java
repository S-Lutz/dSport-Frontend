package com.omgproduction.dsport_application.refactored.models.resultnodes;

import com.omgproduction.dsport_application.refactored.models.nodes.UserNode;



public class UserResultNode extends UserNode {

    private Boolean isFriend;

    private Boolean hasRequest;

    private Boolean isRequested;

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public Boolean getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Boolean hasRequest) {
        this.hasRequest = hasRequest;
    }

    public Boolean getRequested() {
        return isRequested;
    }

    public void setRequested(Boolean requested) {
        isRequested = requested;
    }
}