package com.omgproduction.dsport_application.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Florian on 19.10.2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        //Log.e("TOKEN","RECEIVE: "+token);
        if(token!=null&&!token.isEmpty()){
            SessionService.getInstance().saveLocalToken(token);
        }
    }
}
