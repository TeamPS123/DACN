package com.psteam.foodlocation.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
        String channelId = "location_notification_channel";

        Intent intent = new Intent(getApplicationContext(), UserReserveTableDetailsActivity.class);
        intent.putExtra("response", response);
        intent.setAction("SOME_ACTION");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        MainActivity.updateNotification(response.getReserveTableId());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icon_tasty)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());
    }
}
