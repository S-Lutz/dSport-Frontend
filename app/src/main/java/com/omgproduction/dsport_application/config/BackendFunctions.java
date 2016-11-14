package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 17.10.2016.
 */
public class BackendFunctions {

    //private static final String HOST = "http://www.herborn-software.com:9000";
    private static final String HOST = "http://www.daily-sport.de:9000";
    //private static final String HOST = "http://10.0.2.2:9000";
    //private static final String HOST = "http://localhost:9000";
    private static final String HOME = "/dsport";

    // Server user login url
    public static final String LOGIN = HOST+HOME+"/users/login/";
    // Server user register url
    public static final String REGISTER = HOST+HOME+"/users/signup/";
    // Server user register url
    public static final String GET_USER = HOST+HOME+"/users/getUser/";
    // Server user register url
    public static final String DEL_USER = HOST+HOME+"/users/deleteUser/";
    // Server user register url
    public static final String EDIT_USER = HOST+HOME+"/users/editUser/";
    // Get latest AGB
    public static final String GET_LATEST_AGB = HOST+HOME+"/users/get_latest_agb/";
    // Update AGB Version
    public static final String PUT_AGP_VERSION = HOST+HOME+"/users/put_agb_version/";
}

