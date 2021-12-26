package com.psteam.foodlocation.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.activities.MainActivity;
import com.psteam.foodlocation.activities.UserReserveTableDetailsActivity;
import com.psteam.foodlocation.socket.models.BodySenderFromRes;
import com.psteam.foodlocation.ultilities.CustomToast;

public class ServiceFCM extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //String action = remoteMessage.getNotification().getClickAction();
        BodySenderFromRes response = new BodySenderFromRes();

        response.setReserveTableId(remoteMessage.getData().get("reserveTableId"));
        response.setNotification(remoteMessage.getData().get("notification"));

        //notification of foreground
        notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), response);
    }

    public void notify(String title, String message, BodySenderFromRes response) {
        // create the intent and set the action
        Intent intent = new Intent(getApplicationContext(), UserReserveTableDetailsActivity.class);
        intent.putExtra("response", response);
        intent.setAction("SOME_ACTION");

        //create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        MainActivity.updateNotification(response.getReserveTableId());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.icon_tasty)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());
    }
}
