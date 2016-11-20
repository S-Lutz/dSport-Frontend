package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 17.11.2016.
 */

public class BackendConfig {
    public static final String HOST = "http://www.daily-sport.de:9000";
    public static final String HOME = "/dsport";
    public static final String LOGIN = HOST+HOME+"/users/login/";
    public static final String REGISTER = HOST+HOME+"/users/signup/";
    public static final String GET_USER = HOST+HOME+"/users/get/";
    public static final String DELETE_USER = HOST+HOME+"/users/delete/";
    public static final String EDIT_USER = HOST+HOME+"/users/edit/";
    public static final String GET_AGB = HOST+HOME+"/agb/get/";
    public static final String GET_POSTS = HOST+HOME+"/posts/getAll/";
    public static final String ADD_POST = HOST+HOME+"/posts/add/";
    public static final String GET_POST_DETAIL = HOST+HOME+"/posts/getDetail/";
}
