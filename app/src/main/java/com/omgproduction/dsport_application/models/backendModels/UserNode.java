package com.omgproduction.dsport_application.models.backendModels;

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
}