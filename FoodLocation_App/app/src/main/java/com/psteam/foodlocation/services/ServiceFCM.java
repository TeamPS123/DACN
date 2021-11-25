package com.psteam.foodlocation.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.psteam.foodlocation.R;

public class ServiceFCM extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String action = remoteMessage.getNotification().getClickAction();

        //notification of foreground
        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), action);
    }

    public void notify(String title, String message, String action) {
        // create the intent and set the action
//        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
//        intent.setAction("SOME_ACTION");

        // create a pending intent
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());
    }
}
