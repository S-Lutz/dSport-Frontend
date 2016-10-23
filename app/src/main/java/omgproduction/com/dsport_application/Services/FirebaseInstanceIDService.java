package omgproduction.com.dsport_application.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import omgproduction.com.dsport_application.controller.SessionController;

/**
 * Created by Florian on 19.10.2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        SessionController.getInstance().registerToken(token);
    }
}
