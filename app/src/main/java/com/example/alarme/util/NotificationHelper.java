package com.example.alarme.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "canal_musica";
    public static Notification createNotification(Context context) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Music Service",
                NotificationManager.IMPORTANCE_LOW
        );
        manager.createNotificationChannel(channel);

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Alarme Loterico")
                .setContentText("Resolva o desafio para parar a música!")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .build();
    }
}