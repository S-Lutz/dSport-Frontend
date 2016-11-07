package com.omgproduction.dsport_application.controller;

import com.android.volley.Response;
import com.omgproduction.dsport_application.builder.JSONRequest;
import com.omgproduction.dsport_application.config.BackendFunctions;
import com.omgproduction.dsport_application.config.Keys;

import org.json.JSONObject;

/**
 * Created by Florian on 21.10.2016.
 */
public class ResourceController {
    private static ResourceController instance;
    private ResourceController () {}
    public static synchronized ResourceController getInstance () {
        if (ResourceController.instance == null) {
            ResourceController.instance = new ResourceController ();
        } return ResourceController.instance;
    }


    /**
     * Get Latest AGB from Database
     * @param responseListener
     * @param errorListener
     */
    public void getLatestAGB (Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.GET_LATEST_AGB)
                .responseListener(responseListener)
                .errorListener(errorListener);
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());

    }


    /**
     * Get Latest Data privacy from Database
     * @param responseListener
     * @param errorListener
     */
    public void getLatestDataPrivacy (Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.GET_LATEST_AGB)
                .responseListener(responseListener)
                .errorListener(errorListener);
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());

    }

    /**
     * Put current AGB Version from User
     * @param user_id
     * @param agb_version
     * @param responseListener
     * @param errorListener
     */
    public void putAGBVersion(String user_id, String agb_version, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {

        JSONRequest requestBuilder = new JSONRequest(BackendFunctions.PUT_AGP_VERSION)
                .param(Keys.USERID, user_id)
                .param(Keys.AGB_VERSION, agb_version)
                .responseListener(responseListener)
                .errorListener(errorListener);
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }
}
