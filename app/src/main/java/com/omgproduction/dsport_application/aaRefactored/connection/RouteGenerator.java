package com.omgproduction.dsport_application.aaRefactored.connection;

public class RouteGenerator {

    private static String ROUTE_HOST = "https://daily-sport.de:8002";
    //private static String ROUTE_HOST = "http://192.168.0.206:9000";
    //private static String ROUTE_HOST = "10.0.2.2:9000";

    public static String  generateRegisterRoute(){
        return ROUTE_HOST + "/registration";
    }

    public static String generateLoginRoute(){
        return ROUTE_HOST +"/login";
    }

    public static String generateUpdateUserRoute(){
        return ROUTE_HOST + "/user";
    }

    public static String generateUploadFileRoute(){
        return ROUTE_HOST + "/user/picture/update  ";
    }

    public static String generateSearchUsersRoute(){return ROUTE_HOST + "/users/find"; }

    public static String generateSearchEventsRoute(){
        return ROUTE_HOST + "/event/find";
    }

    public static String generateSearchPostsRoute(){
        return ROUTE_HOST + "/posts/find";
    }

    public static String generateRequestFriendshipRoute(Long userId){return ROUTE_HOST + "/friendship/user/"+userId;}

    public static String generateAcceptFriendshipRoute(Long userId){return ROUTE_HOST + "/friendship/"+userId+"/accept";}

    public static String generateDeleteFriendshipRoute(Long userId){return ROUTE_HOST + "/friendship/"+userId+"/delete";}

    public static String generateDeclineFriendshipRoute(Long userId){return ROUTE_HOST + "/friendship/"+userId+"/decline";}

    public static String generateGetRequestRoute(){return ROUTE_HOST + "/users/friends/getFriendRequests";}

    public static String generateGetFriendsRoute(){return ROUTE_HOST + "/users/friends/getFriends";}

} 