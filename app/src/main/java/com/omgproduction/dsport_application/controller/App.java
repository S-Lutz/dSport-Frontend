package com.omgproduction.dsport_application.controller;

/**
 * Created by Florian on 17.10.2016.
 *
 * APPController to Control App-Specific Data
 *
 * This is the first Class which is running on App-Start
 */

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static Context getContext(){
        return App.applicationContext;
    }
}
