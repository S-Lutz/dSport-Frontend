package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 17.11.2016.
 */

public class BackendConfig {
    public static final String HOST = "http://www.daily-sport.de:9000";
    //public static final String HOST = "http://5.189.141.187:9001";
    public static final String HOME = "/dsport";
    public static final String LOGIN = HOST+HOME+"/users/login/";
    public static final String REGISTER = HOST+HOME+"/users/signup/";
    public static final String GET_USER = HOST+HOME+"/users/get/";
    public static final String EDIT_USER = HOST+HOME+"/users/edit/";
    public static final String GET_POSTS = HOST+HOME+"/posts/getAll/";
    public static final String CREATE_POST = HOST+HOME+"/posts/create/"; //owner_id, user_id, picture, text, title

    public static final String CREATEANDPOST_EVENT = HOST+HOME+"/events/createAndPost/";
    public static final String CREATE_EVENT = HOST+HOME+"/events/create/";
    public static final String POST_EVENT = HOST+HOME+"/posts/event/";

    public static final String CREATEANDPOST_EXERCISE_UNIT = HOST+HOME+"/exercise_unit/createAndPost/";
    public static final String CREATE_EXERCISE_UNIT  = HOST+HOME+"/exercise_unit/create/";
    public static final String POST_EXERCISE_UNIT = HOST+HOME+"/posts/exercise_unit/";

    public static final String DISCARD_TOKEN = HOST+HOME+"/token/discard/";

    public static final String DELETE_USER = HOST+HOME+"/users/delete/";
    public static final String GET_AGB = HOST+HOME+"/agb/get/";
    public static final String GET_PINBOARD = HOST+HOME+"/posts/getPinboard/";
    public static final String GET_POST_DETAIL = HOST+HOME+"/posts/getDetail/";
    public static final String GET_COMMENTS = HOST + HOME + "/posts/comments/getAll/";
    public static final String LIKE_POST = HOST + HOME + "/posts/like/";
    public static final String SHARE_POST = HOST + HOME + "/posts/share/";
    public static final String COMMENT_POST = HOST + HOME + "/posts/comment/";
    public static final String GET_LIKES = HOST + HOME + "/posts/likes/getAll/";
    public static final String LIKE_COMMENT = HOST + HOME + "/posts/comments/like/";


    public static final String DATE_FORMAT="yyyy.MM.dd.HH.mm.ss";
}
