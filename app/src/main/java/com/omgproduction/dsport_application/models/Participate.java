package com.omgproduction.dsport_application.models;

/**
 * Created by Florian on 01.12.2016.
 */

public class Participate {
    private String user_id, username;

    public Participate(String user_id, String username) {
        this.user_id = user_id;
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }
}
