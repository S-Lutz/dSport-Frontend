package com.omgproduction.dsport_application.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.activities.main.MainActivity;
import com.omgproduction.dsport_application.config.ApplicationKeys;
import com.omgproduction.dsport_application.config.NotificationKeys;
import com.omgproduction.dsport_application.controller.ResourceController;
import com.omgproduction.dsport_application.utils.ConnectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Florian on 19.10.2016.
 */
public class NotificationReceiver extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final int NEW_POSTS_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage message) {
        try {
            JSONObject json = new JSONObject(message.getNotification().getBody());
            String notificationCode = ConnectionUtils.extractNotificationCode(json);
            JSONObject value = ConnectionUtils.extractJSONValue(json);
            switch (notificationCode){
                case NotificationKeys.NEW_POSTS:
                    newPosts(value);
                    break;
            }
        } catch (JSONException|NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void newPosts(JSONObject value){

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_social)
                .setContentTitle(getString(R.string.notification_title_new_posts))
                .setContentText(getString(R.string.notification_text_new_posts));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NotificationKeys.NEW_POSTS,NEW_POSTS_ID,builder.build());
    }
}
