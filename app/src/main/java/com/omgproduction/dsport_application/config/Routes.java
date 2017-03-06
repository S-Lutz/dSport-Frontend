package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 17.11.2016.
 */

public interface Routes {
    //String ROUTE_HOST = "http://www.daily-sport.de:9000";
    //String ROUTE_HOST = "http://5.189.141.187:9001";
    //String ROUTE_HOME = "/dsport";
    //String ROUTE_LOGIN = ROUTE_HOST + ROUTE_HOME +"/users/login/";
    //String ROUTE_REGISTER = ROUTE_HOST + ROUTE_HOME +"/users/signup/";
    //String ROUTE_GET_USER = ROUTE_HOST + ROUTE_HOME +"/users/get/";
    //String ROUTE_EDIT_USER = ROUTE_HOST + ROUTE_HOME +"/users/edit/";
    //String ROUTE_GET_POSTS = ROUTE_HOST + ROUTE_HOME +"/posts/getAll/";
    //String ROUTE_CREATE_POST = ROUTE_HOST + ROUTE_HOME +"/posts/create/"; //owner_id, user_id, picture, text, title
//
    //String ROUTE_CREATEANDPOST_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/createAndPost/";
    //String ROUTE_CREATE_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/create/";
    //String ROUTE_POST_EVENT = ROUTE_HOST + ROUTE_HOME +"/posts/event/";
//
    //String ROUTE_CREATEANDPOST_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/exercise_unit/createAndPost/";
    //String ROUTE_CREATE_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/exercise_unit/create/";
    //String ROUTE_POST_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/posts/exercise_unit/";
//
    //String ROUTE_DISCARD_TOKEN = ROUTE_HOST + ROUTE_HOME +"/token/discard/";
//
    //String ROUTE_DELETE_USER = ROUTE_HOST + ROUTE_HOME +"/users/delete/";
    //String ROUTE_GET_AGB = ROUTE_HOST + ROUTE_HOME +"/agb/get/";
    //String ROUTE_GET_PINBOARD = ROUTE_HOST + ROUTE_HOME +"/posts/getPinboard/";
    //String ROUTE_GET_POST_DETAIL = ROUTE_HOST + ROUTE_HOME +"/posts/getDetail/";
    //String ROUTE_GET_COMMENTS = ROUTE_HOST + ROUTE_HOME + "/posts/comments/getAll/";
    //String ROUTE_LIKE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/like/";
    //String ROUTE_SHARE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/share/";
    //String ROUTE_COMMENT_POST = ROUTE_HOST + ROUTE_HOME + "/posts/comment/";
    //String ROUTE_GET_LIKES = ROUTE_HOST + ROUTE_HOME + "/posts/likes/getAll/";
    //String ROUTE_LIKE_COMMENT = ROUTE_HOST + ROUTE_HOME + "/posts/comments/like/";


    //String ROUTE_HOST = "http://www.daily-sport.de:9000";
    String ROUTE_HOST = "http://5.189.141.187:9001";
    String ROUTE_HOME = "/dsport";
    String ROUTE_LOGIN = ROUTE_HOST + ROUTE_HOME +"/users/login";
    String ROUTE_REGISTER = ROUTE_HOST + ROUTE_HOME +"/users/signup";
    String ROUTE_GET_USER = ROUTE_HOST + ROUTE_HOME +"/users/get";
    String ROUTE_EDIT_USER = ROUTE_HOST + ROUTE_HOME +"/users/edit";
    String ROUTE_GET_POSTS = ROUTE_HOST + ROUTE_HOME +"/posts/getAll";
    String ROUTE_CREATE_POST = ROUTE_HOST + ROUTE_HOME +"/posts/create"; //owner_id, user_id, picture, text, title

    String ROUTE_CREATEANDPOST_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/createAndPost";
    String ROUTE_CREATE_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/create";
    String ROUTE_POST_EVENT = ROUTE_HOST + ROUTE_HOME +"/posts/event";

    String ROUTE_CREATEANDPOST_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/exercise_unit/createAndPost";
    String ROUTE_CREATE_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/exercise_unit/create";
    String ROUTE_POST_EXERCISE_UNIT = ROUTE_HOST + ROUTE_HOME +"/posts/exercise_unit";

    String ROUTE_DISCARD_TOKEN = ROUTE_HOST + ROUTE_HOME +"/token/discard";

    String ROUTE_DELETE_USER = ROUTE_HOST + ROUTE_HOME +"/users/delete";
    String ROUTE_GET_AGB = ROUTE_HOST + ROUTE_HOME +"/agb/get";
    String ROUTE_GET_PINBOARD = ROUTE_HOST + ROUTE_HOME +"/posts/getPinboard";
    String ROUTE_GET_POST_DETAIL = ROUTE_HOST + ROUTE_HOME +"/posts/getDetail";
    String ROUTE_GET_COMMENTS = ROUTE_HOST + ROUTE_HOME + "/posts/comments/getAll";
    String ROUTE_LIKE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/like";
    String ROUTE_SHARE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/share";
    String ROUTE_COMMENT_POST = ROUTE_HOST + ROUTE_HOME + "/posts/comment";
    String ROUTE_GET_LIKES = ROUTE_HOST + ROUTE_HOME + "/posts/likes/getAll";
    String ROUTE_LIKE_COMMENT = ROUTE_HOST + ROUTE_HOME + "/posts/comments/like";
}
