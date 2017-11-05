package com.omgproduction.dsport_application.models.uploadModels;

public class RegistrationUser {

    private String username, email, firstname, lastname, password = "";

    public RegistrationUser(String username, String email, String firstname, String lastname, String password) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }
}