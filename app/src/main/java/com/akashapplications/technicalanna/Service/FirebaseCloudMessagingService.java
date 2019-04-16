package com.akashapplications.technicalanna.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.akashapplications.technicalanna.R;
import com.akashapplications.technicalanna.Splash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    private int notificationID = 100;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification());
//        showNotification(remoteMessage.getData());
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.i("checking", "key, " + key + " value " + value);
        }
    }

    private void showNotification(RemoteMessage.Notification notification) {

        Log.i("checking","firebase title : "+notification.getTitle());
        Log.i("checking","firebase body : "+notification.getBody());
//        Log.i("checking","firebase  : "+notification.toString());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        Intent notificationIntent = new Intent(getBaseContext(), Splash.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.small_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.com_smart_login_code));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.small_icon);
        }


        Notification systemNotification = notificationBuilder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Wission",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notificationID, systemNotification);
    }


    private void showNotification(Map<String,String> notification) {
        Log.i("checking","firebase title : "+notification.get("title"));
        Log.i("checking","firebase body : "+notification.get("body"));
//        Log.i("checking","firebase  : "+notification.toString());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        Intent notificationIntent = new Intent(getBaseContext(), Splash.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder
                .setContentTitle(notification.get("title"))
                .setContentText(notification.get("body"))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.small_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.com_smart_login_code));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.small_icon);
        }


        Notification systemNotification = notificationBuilder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Wission",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notificationID, systemNotification);
    }


}
