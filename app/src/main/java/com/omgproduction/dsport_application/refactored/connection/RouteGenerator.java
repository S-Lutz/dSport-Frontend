package com.omgproduction.dsport_application.refactored.connection;

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

    public static String generateCreatePostRoute(Long userId){return ROUTE_HOST + "/user/"+userId+"/pinned";}

    public static String generateUploadPostFileRoute(Long postId){return ROUTE_HOST + "/posts/picture/update/"+postId;}

    public static String generateCreateEventRoute() {return ROUTE_HOST + "/event";}

    public static String generateUploadEventFileRoute(Long eventId) {return ROUTE_HOST + "/event/picture/update/"+eventId;}

    public static String generateGetPinboardRoute(Long pinboardId) {return ROUTE_HOST + "/user/"+pinboardId+"/pinboard";}

    public static String generateGetNewsFeedRoute(Long userId) {return ROUTE_HOST + "/user/"+userId+"/newsfeed";}

    public static String generateGetCommentsRoute(Long socialNodeId) {return ROUTE_HOST + "/social/"+socialNodeId+"/comment/get";}

    public static String generateGetLikesRoute(Long socialNodeId) {return ROUTE_HOST + "/likeable/"+socialNodeId+"/likes/get";}

    public static String generateLikesRoute(Long socialNodeId) {return ROUTE_HOST + "/likeable/"+socialNodeId+"/likes";}

    public static String generateParticipateRoute(Long eventId) {return ROUTE_HOST + "/event/"+eventId+"/participate";}

    public static String generateCreateCommentRoute(Long socialNodeId) { return ROUTE_HOST + "/social/"+socialNodeId+"/comment";}

    public static String generateUploadCommentFileRoute(Long socialNodeId) {return ROUTE_HOST + "/comment/picture/update/"+socialNodeId;}

    public static String generateGetEventsRoute(Long userId) {return ROUTE_HOST + "/user/"+userId+"/events" ;}

    public static String generateGetExercisesRoute(long userId) {return ROUTE_HOST + "/exercise/"+userId+"/exercises" ;}

    public static String generateCreateExerciseRoute() {return ROUTE_HOST + "/exercise/create" ;}

    public static String generateCreateExerciseUnitRoute(Long ofExerciseId) {return ROUTE_HOST + "/exercise/"+ofExerciseId+"/units/add" ;}

    public static String generateAddSetRoute(Long exerciseUnitId)  {return ROUTE_HOST + "/exerciseunits/"+exerciseUnitId+"/sets/add" ;}

    public static String generateGetExerciseUnitsRoute(Long id) {return ROUTE_HOST + "/exerciseUnits/"+id+"/get" ;}

    public static String generateGetExerciseUnitDetailRoute(Long exerciseUnitId)  {return ROUTE_HOST + "/exerciseunits/"+exerciseUnitId+"/sets/get" ;}
}