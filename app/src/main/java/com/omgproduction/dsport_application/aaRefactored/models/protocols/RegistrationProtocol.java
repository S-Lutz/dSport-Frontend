package com.omgproduction.dsport_application.aaRefactored.models.protocols;

public class RegistrationProtocol {

    private String username, email, firstname, lastname, password = "";

    public RegistrationProtocol(String username, String email, String firstname, String lastname, String password) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }
}