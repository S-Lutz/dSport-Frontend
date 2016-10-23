package omgproduction.com.dsport_application.controller;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import omgproduction.com.dsport_application.activities.LoginActivity;
import omgproduction.com.dsport_application.activities.MainActivity;
import omgproduction.com.dsport_application.builder.JsonObjectRequestBuilder;
import omgproduction.com.dsport_application.config.URL;
import omgproduction.com.dsport_application.utils.ConnectionUtils;
import omgproduction.com.dsport_application.utils.StringUtils;

/**
 * Created by Florian on 21.10.2016.
 */
public class SessionController {
    private static SessionController instance;
    private SessionController () {}
    public static synchronized SessionController getInstance () {
        if (SessionController.instance == null) {
            SessionController.instance = new SessionController ();
        }
        return SessionController.instance;
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

        JsonObjectRequestBuilder requestBuilder = new JsonObjectRequestBuilder(URL.REGISTER)
                .param("username", username)
                .param("firstname", firstname)
                .param("lastname", lastname)
                .param("email", email)
                .param("password", StringUtils.hash(password))
                .responseListener(responseListener)
                .errorListener(errorListener);
        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }
    /**
     * Login user with the Backend System
     * @param username Users username
     * @param password Users Password
     * @param responseListener Listener to do something after recieve response
     * @param errorListener Listener to handle some Errors
     */
    public void loginUser(final Context context, String username, String password, final Response.Listener<JSONObject> responseListener, final Response.ErrorListener errorListener) {

        JsonObjectRequestBuilder requestBuilder = new JsonObjectRequestBuilder(URL.LOGIN)
                .param("username", username)
                .param("password", StringUtils.hash(password))
                .errorListener(errorListener)
                .responseListener(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(ConnectionUtils.Success(jsonObject)){
                            JSONObject user = ConnectionUtils.extractJSONValue(jsonObject);
                            try {
                                SharedPreferencesController preferencesController = new SharedPreferencesController(context)
                                        .putBoolean(SharedPreferencesController.IS_LOGIN, true)
                                        .putString(SharedPreferencesController.KEY_USERID, user.getString("user_id"))
                                        .putString(SharedPreferencesController.KEY_USERNAME, user.getString("username"))
                                        .putString(SharedPreferencesController.KEY_EMAIL, user.getString("email"))
                                        .putString(SharedPreferencesController.KEY_FIRSTNAME, user.getString("firstname"))
                                        .putString(SharedPreferencesController.KEY_LASTNAME,  user.getString("lastname"))
                                        .putString(SharedPreferencesController.KEY_CREATED, user.getString("created"))
                                        .putString(SharedPreferencesController.KEY_AGBVERSION, user.getString("agbversion"))
                                        .putString(SharedPreferencesController.KEY_PICTURE, user.getString("picture"));

                                // commit changes
                                preferencesController.commit();

                                //Start Main Activity
                                goToMainActivity(context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        responseListener.onResponse(jsonObject);
                    }
                });

        ApplicationController.getInstance().addToRequestQueue(requestBuilder.build());
    }
    public void registerToken(String token) {
        //Send Token to the Server and Save it into User Table...
        //We will need it to send Notifications to the Client

        //Tutorial Videos:
        //https://www.youtube.com/watch?v=LiKCEa5_Cs8
        //https://www.youtube.com/watch?v=MYZVhs6T_W8

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin(Context context) {

        boolean loggedIn = new SharedPreferencesController(context).getBooleanDetail(SharedPreferencesController.IS_LOGIN,false);
        // Check login status
        if (!loggedIn) {
            goToLoginActivity(context);
        }

    }

    /**
     * Clear session details
     */
    public void logoutUser(Context context) {

        new SharedPreferencesController(context)
                .clear()
                .commit();

        goToLoginActivity(context);
    }

    private void goToMainActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void goToLoginActivity(Context context){
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
