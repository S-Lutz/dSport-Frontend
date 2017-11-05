package com.omgproduction.dsport_application.utils;

public class RouteGenerator {

    private static String ROUTE_HOST = "https://daily-sport.de:8002";

    public static String  createRegisterRoute(){
        return ROUTE_HOST + "/registration";
    }

    public static String generateLoginRoute(){
        return ROUTE_HOST +"/login";
    }
} 