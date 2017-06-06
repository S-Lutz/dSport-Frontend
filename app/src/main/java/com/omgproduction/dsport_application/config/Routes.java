package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 17.11.2016.
 */

public interface Routes {
    String ROUTE_HOST = "http://5.189.141.187:9000";
    //String ROUTE_HOST = "http://5.189.141.187:9001";
    String ROUTE_HOME = "/dsport";
    String ROUTE_LOGIN = ROUTE_HOST + ROUTE_HOME +"/users/login";
    String ROUTE_REGISTER = ROUTE_HOST + ROUTE_HOME +"/users/signup";
    String ROUTE_GET_USER = ROUTE_HOST + ROUTE_HOME +"/users/get";
    String ROUTE_EDIT_USER = ROUTE_HOST + ROUTE_HOME +"/users/edit";
    String ROUTE_GET_POSTS = ROUTE_HOST + ROUTE_HOME +"/posts/getAll";
    String ROUTE_CREATE_POST = ROUTE_HOST + ROUTE_HOME +"/posts/create"; //owner_id, user_id, picture, text, title

    String ROUTE_CREATE_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/create";
    String ROUTE_LIKE_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/like";
    String ROUTE_COMMENT_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/comment";
    String ROUTE_SHARE_EVENT = ROUTE_HOST + ROUTE_HOME +"/events/share";
    String ROUTE_GET_EVENTBOARD = ROUTE_HOST + ROUTE_HOME +"/events/getEventBoard";
    String ROUTE_GET_EVENT_COMMENTS = ROUTE_HOST + ROUTE_HOME + "/events/comments/getAll";
    String ROUTE_GET_EVENT_LIKES = ROUTE_HOST + ROUTE_HOME + "/events/likes/getAll";
    String ROUTE_GET_EVENTS = ROUTE_HOST + ROUTE_HOME +"/events/getAll";
    String ROUTE_GET_EVENT_DETAIL = ROUTE_HOST + ROUTE_HOME +"/events/getDetail";
    String ROUTE_PARTICIPATE_EVENT = ROUTE_HOST + ROUTE_HOME + "/events/participate";
    String ROUTE_POST_EVENT = ROUTE_HOST + ROUTE_HOME +"/posts/event";
    String ROUTE_GET_MEMBERS = ROUTE_HOST + ROUTE_HOME + "/events/members/getAll";

    String ROUTE_DISCARD_TOKEN = ROUTE_HOST + ROUTE_HOME +"/token/discard";

    String ROUTE_DELETE_USER = ROUTE_HOST + ROUTE_HOME +"/users/delete";
    String ROUTE_GET_AGB = ROUTE_HOST + ROUTE_HOME +"/agb/get";

    String ROUTE_GET_PINBOARD = ROUTE_HOST + ROUTE_HOME +"/posts/getPinboard";
    String ROUTE_GET_POST_DETAIL = ROUTE_HOST + ROUTE_HOME +"/posts/getDetail";
    String ROUTE_GET_COMMENTS = ROUTE_HOST + ROUTE_HOME + "/posts/comments/getAll";
    String ROUTE_LIKE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/like";
    String ROUTE_SHARE_POST = ROUTE_HOST + ROUTE_HOME + "/posts/share";
    String ROUTE_COMMENT_POST = ROUTE_HOST + ROUTE_HOME + "/posts/comment";
    String ROUTE_LIKE_COMMENT = ROUTE_HOST + ROUTE_HOME + "/posts/comments/like";
    String ROUTE_GET_LIKES = ROUTE_HOST + ROUTE_HOME + "/posts/likes/getAll";


    String ROUTE_SEARCH_ALL = ROUTE_HOST + ROUTE_HOME +"/search/all";

    String ROUTE_FRIENDS_GET_ALL = ROUTE_HOST + ROUTE_HOME + "/friends/getAll";
    String ROUTE_FRIENDS_DELETE = ROUTE_HOST + ROUTE_HOME +"/friends/delete ";
    String ROUTE_FRIENDS_SEND = ROUTE_HOST + ROUTE_HOME +"/friends/request/send ";
    String ROUTE_FRIENDS_ACCEPT = ROUTE_HOST + ROUTE_HOME +"/friends/request/accept ";
    String ROUTE_FRIENDS_DECLINE= ROUTE_HOST + ROUTE_HOME +"/friends/request/decline ";

    String ROUTE_EXERCISES_CREATE  =  ROUTE_HOST + ROUTE_HOME +"/exercises/create";
    String ROUTE_EXERCISES_GET_ALL  =  ROUTE_HOST + ROUTE_HOME +"/exercises/getAll";

    String ROUTE_EXERCISE_UNIT_CREATE_TYPE0  =  ROUTE_HOST + ROUTE_HOME +"/exercises/units/create/type0";
    String ROUTE_EXERCISE_UNIT_CREATE_TYPE1  =  ROUTE_HOST + ROUTE_HOME +"/exercises/units/create/type1";
    String ROUTE_EXERCISE_UNIT_CREATE_TYPE2  =  ROUTE_HOST + ROUTE_HOME +"/exercises/units/create/type2";
    String ROUTE_EXERCISE_UNIT_GET_ALL  =  ROUTE_HOST + ROUTE_HOME +"/exercises/units/getAll";
    String ROUTE_EXERCISE_UNIT_LIKE =  ROUTE_HOST + ROUTE_HOME +"/exercises/units/like";
}
