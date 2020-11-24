package com.cmpt275.house.classDef;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cmpt275.house.HomeActivity;
import com.cmpt275.house.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
Usage Example:

Notification notification = notify.getNotification("Sign In", "You just tried to sign in! Welcome!", this);
notify.scheduleNotification(this, notification, 5000);
 */

public class notifications extends BroadcastReceiver {

    //
    // Class Attributes
    //
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    public static String GENERAL_CHANNEL_ID = "General";


    //
    // Class Functions
    //

    /////////////////////////////////////////////////////////
    //
    // Will create a notification channel for the user
    //
    /////////////////////////////////////////////////////////
    public static void createNotificationChannel(Context mContext) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "House Task Manager";//getString(R.string.channel_name);
            String description = "Notification Channel to help keep you on track."; //getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(GENERAL_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /////////////////////////////////////////////////////////
    //
    // Will create a notification based on the title and text
    //
    /////////////////////////////////////////////////////////
    public Notification getNotification(String title, String contentText, Context mContext) {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, GENERAL_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_day)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();
    }


    /////////////////////////////////////////////////////////
    //
    // Will setup the notification to be displayed when ready
    //
    /////////////////////////////////////////////////////////
    public void scheduleNotification(Context mContext, Notification notification, int msFromNow) {
        Intent notificationIntent = new Intent(mContext, notifications.class);
        notificationIntent.putExtra(notifications.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(notifications.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + msFromNow;
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        Log.d("notifications:", "alarmManager set to wake after " + msFromNow + " milliseconds");
    }


    /////////////////////////////////////////////////////////
    //
    // Will display the notification for the user
    //
    /////////////////////////////////////////////////////////
    public void onReceive(Context context, Intent intent) {

        Log.d("notifications:", "alarmManager woke up");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}


