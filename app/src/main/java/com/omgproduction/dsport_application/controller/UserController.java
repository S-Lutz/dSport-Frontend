package com.omgproduction.dsport_application.controller;

import android.content.Context;

import com.android.volley.Response;
import com.omgproduction.dsport_application.exceptions.UserNotFoundException;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;

import org.json.JSONObject;

/**
 * Created by Florian on 07.11.2016.
 */

public class UserController {
    private static UserController instance;
    private UserController () {}
    public static synchronized UserController getInstance () {
        if (UserController.instance == null) {
            UserController.instance = new UserController ();
        }
        return UserController.instance;
    }

    public void savePicture(Context context, String stringFromBitmap, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws UserNotFoundException{

        String userID = Preferences.getInstance(context)
                .getStringDetail(Keys.USERID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(BackendFunctions.EDIT_USER)
                    .errorListener(errorListener)
                    .responseListener(listener)
                    .param(Keys.USERID, userID)
                    .param(Keys.PICTURE,stringFromBitmap);
            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            throw new UserNotFoundException();
        }

    }

    public void getUser(Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws UserNotFoundException{

        String userID = Preferences.getInstance(context)
                .getStringDetail(Keys.USERID,"");

        if(!userID.trim().isEmpty()){
            JSONRequest request = new JSONRequest(BackendFunctions.GET_USER)
                    .param(Keys.USERID,userID)
                    .responseListener(listener)
                    .errorListener(errorListener);

            ApplicationController.getInstance().addToRequestQueue(request.build());
        }else{
            throw new UserNotFoundException();
        }

    }
}
