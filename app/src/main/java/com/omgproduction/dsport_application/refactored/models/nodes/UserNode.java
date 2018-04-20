package com.omgproduction.dsport_application.refactored.models.nodes;

import java.io.Serializable;

public class UserNode extends AbstractNode implements Serializable {

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String picture;

    private String password;


    public UserNode(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }


    public UserNode() {
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}