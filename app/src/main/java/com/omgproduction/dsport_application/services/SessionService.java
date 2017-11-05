package com.omgproduction.dsport_application.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.omgproduction.dsport_application.activities.main.LoginActivity;
import com.omgproduction.dsport_application.builder.Preferences;
import com.omgproduction.dsport_application.listeners.adapters.RequestFuture;
import com.omgproduction.dsport_application.listeners.callbacks.BackendCallback;
import com.omgproduction.dsport_application.listeners.interfaces.IRequestFuture;
import com.omgproduction.dsport_application.models.backendModels.RegistrationNode;
import com.omgproduction.dsport_application.models.backendModels.UserNode;
import com.omgproduction.dsport_application.models.uploadModels.RegistrationUser;
import com.omgproduction.dsport_application.utils.BackendRequestExecutor;
import com.omgproduction.dsport_application.utils.JSONRequest;
import com.omgproduction.dsport_application.utils.JSONResponse;
import com.omgproduction.dsport_application.utils.RouteGenerator;

import java.net.MalformedURLException;

/**
 * Created by Florian on 21.10.2016.
 */
public class SessionService extends AbstractService{


    public SessionService(Context context) {
        super(context);
    }

    public void registerUser(final RegistrationUser registrationUser, final BackendCallback<RegistrationNode> callback){
        BackendRequestExecutor.executePostRequest(RouteGenerator.createRegisterRoute(), RegistrationNode.class, registrationUser, callback);
    }


    public void validateUser(final UserNode user, final BackendCallback<UserNode> callback) {
        BackendRequestExecutor.executePostRequest(RouteGenerator.generateLoginRoute(), UserNode.class, user, callback);
    }


    public void saveLocalToken(final String token) {
        Preferences preferences = new Preferences(context);
        preferences.putString(APPLICATION_TOKEN, token).commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public boolean checkLogin() {
        return
                new Preferences(context).getBooleanDetail(APPLICATION_IS_LOGIN,false);

    }

    /**
     * Clear session details
     */
    public void logout() {

        Log.e("LOGOUT", "LOGOUT");

        discardToken(new RequestFuture<Void>());

        //new Preferences(context).clear().commit();

        new Preferences(context)
                .putBoolean(APPLICATION_IS_LOGIN, false)
                .putLong(APPLICATION_USER_USER_ID, -1)
                .putString(APPLICATION_USER_USERNAME, "")
                .putString(APPLICATION_USER_EMAIL, "")
                .putString(APPLICATION_USER_FIRSTNAME, "")
                .putString(APPLICATION_USER_LASTNAME,  "")
                .putString(APPLICATION_USER_CREATED, "")
                .putString(APPLICATION_USER_UPDATED, "")
                .putString(APPLICATION_USER_AGBVERSION, "")
                .putString(APPLICATION_USER_PICTURE, "")
                .clear()
                .commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(i);
    }

    public void discardToken(final IRequestFuture<Void> onResultListener) {

        final String token = new Preferences(context).getStringDetail(APPLICATION_TOKEN, "");

        onResultListener.onStartQuery();

        try {
            JSONRequest request = new JSONRequest(ROUTE_DISCARD_TOKEN)
                    .addParam(APPLICATION_TOKEN, token)
                    .setOnResultListener(new JSONRequest.OnResultListener() {
                        @Override
                        public void onResult(JSONResponse response) {
                            onResultListener.onFinishQuery();
                            if (response.isOk()) {
                                onResultListener.onSuccess(null);
                            } else {
                                onResultListener.onFailure(response.getResponseCode(), response.getResponseMessage());
                            }
                        }
                    });
            request.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onResultListener.onFinishQuery();
        }
    }
}
