package com.omgproduction.dsport_application.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.controller.SessionController;


/**
 * Created by Florian on 17.10.2016.
 *
 * MainActivity for the first Access when user open the App and Login
 *
 */
public class MainActivity extends Activity{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        if(!SessionController.getInstance().checkLogin(this)){
            goToMainActivity(this);
        }


        //Register Client to Firebase Cloud and Ask for Token
        //After response Token it will automatically send to Backend because of FirebaseInstanceIDService
        //FirebaseMessaging.getInstance().subscribeToTopic("Register");
        //FirebaseInstanceId.getInstance().getToken();

        // readAGB();

        startActivity(new Intent(this, LoginActivity.class));

    }

    private void goToMainActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void readAGB() {
        /* Must be Refactored after implementing Firebase Cloud Messaging
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                BackendController.getInstance().getLatestAGB(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if(jsonObject.getString("error").equals("OK")){
                                JSONObject agb = jsonObject.getJSONObject("value");

                                String agb_version = agb.getString("version");
                                String agb_text = agb.getString("text");

                                SessionControllerOld sessionController = new SessionControllerOld(context);
                                String clientsAGBVersion = sessionController.getUserDetails().get(SessionControllerOld.KEY_AGBVERSION);

                                if(!agb_version.equals(clientsAGBVersion)){
                                    Intent i = new Intent(context, AGBActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.putExtra("agb",agb.toString());
                                    context.startActivity(i);
                                }

                            }else{
                                String errorCode = jsonObject.getString("value");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
            }
        });
        */
    }
}
