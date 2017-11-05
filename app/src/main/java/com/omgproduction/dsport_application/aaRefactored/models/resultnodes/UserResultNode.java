package com.omgproduction.dsport_application.aaRefactored.models.resultnodes;

import com.omgproduction.dsport_application.aaRefactored.models.nodes.AbstractNode;

public class UserResultNode extends AbstractNode {
    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String picture;

    private Boolean isFriend;

    private Boolean hasRequest;

    private Boolean isRequested;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

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