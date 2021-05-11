package com.example.notificationtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notificationtest.Services.NotificationActionService;

public class CreateNotification {
    public static final String CHANNEL_ID = "Channel_id";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_STOP = "actionstop";

    public static Notification notification;

    public static void createNotification(Context context, Track track, int playbutton, int pos, int size){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());

            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,0,
                    intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentStop = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_STOP);
            PendingIntent pendingIntentStop = PendingIntent.getBroadcast(context,0,
                    intentStop, PendingIntent.FLAG_UPDATE_CURRENT);

            //create notification
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                    .setContentTitle("TITLE")
                    .setContentText("TEXT")
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(playbutton, "play", pendingIntentPlay)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            notificationManagerCompat.notify(1,notification);
        }

    }
}


















