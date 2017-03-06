package com.omgproduction.dsport_application.models;

import java.io.Serializable;

/**
 * Created by Florian on 21.12.2016.
 */

public class SearchUser  implements Serializable{
    private String id, username, picture, firstname, lastname;
    private boolean friend, request_received, request_sended;

    public SearchUser(String id, String username, String picture, String firstname, String lastname, boolean friend, boolean request_received, boolean request_sended) {
        this.id = id;
        this.username = username;
        this.picture = picture;
        this.firstname = firstname;
        this.lastname = lastname;
        this.friend = friend;
        this.request_received = request_received;
        this.request_sended = request_sended;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isRequest_received() {
        return request_received;
    }

    public void setRequest_received(boolean request_received) {
        this.request_received = request_received;
    }

    public boolean isRequest_sended() {
        return request_sended;
    }

    public void setRequest_sended(boolean request_sended) {
        this.request_sended = request_sended;
    }
}
