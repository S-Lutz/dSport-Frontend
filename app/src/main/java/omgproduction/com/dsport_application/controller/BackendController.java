package omgproduction.com.dsport_application.controller;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import omgproduction.com.dsport_application.config.URL;

/**
 * Created by Florian on 17.10.2016.
 *
 * BackendController to Control interactions with the dSport Backend System
 */
public class BackendController {

    //Activity-Tag for Log or something else
    public static final String TAG = BackendController.class.getSimpleName();
    //Instance of this BackendController to Interact from other Classes
    private static BackendController mInstance;

    static {
        //Instantiate on Application-Start
        mInstance = new BackendController();
    }

    /**
     * Get BackendController instance to interact with BackendController
     */
    public static synchronized BackendController getInstance() {
        return mInstance;
    }

    /**
     * Register user with the Backend System
     * @param username Users username
     * @param firstname Users real firstname
     * @param lastname Users real lastname
     * @param email Users email-address
     * @param password Users Password
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    public void registerUser(String username, String firstname, String lastname, String email, String password, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        try {
            //Create User-JSON-Object to send it in JSON structure to the Backend System
            JSONObject user = new JSONObject();

            //Insert User-Data
            user.put("username", username);
            user.put("firstname", firstname);
            user.put("lastname", lastname);
            user.put("email", email);
            user.put("password", hash(password));

            //Send User- informations to the Backend System
            send(URL.REGISTER, user, responseListener, errorListener);

        } catch (JSONException e) {
            //Handle some JSON Error
            e.printStackTrace();
        }
    }

    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    public void loginUser(String username, String password, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        try {
            //Create User-JSON-Object to send it in JSON structure to the Backend System
            JSONObject user = new JSONObject();

            //Insert User-Data
            user.put("username", username);
            user.put("password", hash(password));

            //Send User- informations to the Backend System
            send(URL.LOGIN, user, responseListener, errorListener);

        } catch (JSONException e) {
            //Handle some JSON Error
            e.printStackTrace();
        }
    }

    /**
     * Hash the Userpassword
     * @param password Password to Hash
     * @return Hashed Password
     */
    private String hash(String password) {
        //TODO HASH
        return password;
    }

    /**
     * Send some Data to the Server
     * Data is send as JSON Object
     *
     * @param url URL to the Server (Backendsystems Route-File-Path)
     * @param data Data to send to the Backend System
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    private void send(String url, JSONObject data, final Response.Listener<JSONObject> responseListener, final Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //After recieve a response Log it to the Console
                        Log.d(TAG, response.toString());
                        //Send response to calling Class
                        responseListener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //If some Connection Error Log it to the Console and send the Error to rhe calling Class
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                errorListener.onErrorResponse(error);
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //Creating JSON Header
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}
