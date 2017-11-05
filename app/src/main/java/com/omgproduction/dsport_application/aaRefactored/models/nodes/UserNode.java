package com.omgproduction.dsport_application.aaRefactored.models.nodes;

public class UserNode extends AbstractNode {

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String picture;

    private String password;

    private String agbVersion = "1";

    public UserNode(String username, String password) {
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

    public String getAgbVersion() {
        return agbVersion;
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
}