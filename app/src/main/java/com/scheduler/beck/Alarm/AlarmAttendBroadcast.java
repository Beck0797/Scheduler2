package com.scheduler.beck.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.scheduler.beck.R;
import com.scheduler.beck.AssignmentActivity;

import static com.scheduler.beck.Alarm.NotificationChannels.CHANNEL_1_ID;
import static com.scheduler.beck.Alarm.NotificationChannels.CHANNEL_2_ID;

public class AlarmAttendBroadcast extends BroadcastReceiver {
    private static int request_code, nId;

    PendingIntent PiY, PiN, PiL;
    @Override
    public void onReceive(Context context, Intent intent) {
        request_code = (int)(Math.random() * 1000 + 1);
        nId = (int)(Math.random() * 1000 + 1);

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String title = bundle.getString("title");
        String courseName = bundle.getString("courseName");
        String claass = bundle.getString("class");
        String c_key = bundle.getString("c_key");

        //Click on Notification

        if(claass == null) {
            // it is assignment
            nId++;
            Intent intent1 = new Intent(context, AssignmentActivity.class);
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.paper_guitar);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.assign_notification_layout);
            //assignment title
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.course, courseName + ": ");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");
            mBuilder.setSmallIcon(R.drawable.icon_notification);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.build().flags = Notification.PRIORITY_DEFAULT;
            mBuilder.setSound(soundUri);


            PendingIntent pendingIntent = PendingIntent.getActivity(context, nId, intent1, PendingIntent.FLAG_ONE_SHOT);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mBuilder.setContent(contentView);
            mBuilder.setContentIntent(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelId = "channel_id";
                NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(CHANNEL_1_ID);
            }
            Notification notification = mBuilder.build();
            assert notificationManager != null;
            notificationManager.notify(nId, notification);


        } else {


            // it is class
            request_code++;
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.attend_notification_layout);
            //assignment title
            contentView.setTextViewText(R.id.txtClass2, claass);

            Intent iYes = new Intent(context, AlarmAttendReceiver.class);
            iYes.putExtra("num", 1);
            iYes.putExtra("class", claass);
            iYes.putExtra("c_key", c_key);
            iYes.putExtra("id", nId);

            PiY = PendingIntent.getBroadcast(context, ++request_code, iYes, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent iNo = new Intent(context, AlarmAttendReceiver.class);
            iNo.putExtra("num", 2);
            iNo.putExtra("class", claass);
            iNo.putExtra("c_key", c_key);
            iNo.putExtra("id", nId);

            PiN = PendingIntent.getBroadcast(context, ++request_code, iNo, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent iLate = new Intent(context, AlarmAttendReceiver.class);
            iLate.putExtra("num", 3);
            iLate.putExtra("class", claass);
            iLate.putExtra("c_key", c_key);
            iLate.putExtra("id", nId);

            PiL = PendingIntent.getBroadcast(context, ++request_code, iLate, PendingIntent.FLAG_UPDATE_CURRENT);

            contentView.setOnClickPendingIntent(R.id.btnYes, PiY);
            contentView.setOnClickPendingIntent(R.id.btnNo, PiN);
            contentView.setOnClickPendingIntent(R.id.btnLate, PiL);


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_2_ID);
            mBuilder.setSmallIcon(R.drawable.icon_notification);
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(true); // not removed in swipe
            mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
            mBuilder.build().flags = Notification.PRIORITY_DEFAULT;
            mBuilder.setContent(contentView);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelId = "channel_id";
                NotificationChannel channel = new NotificationChannel(CHANNEL_2_ID, "Channel 2", NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(CHANNEL_2_ID);
            }

            Notification notification = mBuilder.build();
            assert notificationManager != null;
            notificationManager.notify(nId, notification);


        }

    }}
