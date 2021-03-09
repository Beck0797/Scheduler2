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
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.scheduler.beck.R;
import com.scheduler.beck.AssignmentActivity;

import static com.scheduler.beck.Alarm.NotificationChannels.CHANNEL_2_ID;
import static com.scheduler.beck.RegisterClassActivity.TAG;

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
            Toast.makeText(context, "It is assignment. Claaas is " + claass, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "It is assignment. Claaas is " + claass);

            Intent intent1 = new Intent(context, AssignmentActivity.class);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            //assignment title
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.course, courseName);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");
            mBuilder.setSmallIcon(R.drawable.icon_notification);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.build().flags = Notification.PRIORITY_DEFAULT;

            PendingIntent pendingIntent = PendingIntent.getActivity(context, nId, intent1, PendingIntent.FLAG_ONE_SHOT);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mBuilder.setContent(contentView);
            mBuilder.setContentIntent(pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "channel_id";
                NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }
            Notification notification = mBuilder.build();
            assert notificationManager != null;
            notificationManager.notify(nId, notification);


        } else {


            // it is class
            request_code++;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle(claass)
                    .setContentText("Are you attending the class?")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setColor(Color.BLUE)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true) // not removed in swipe
                    .addAction(R.drawable.ic_launcher_foreground, "Yes", PiY)
                    .addAction(R.drawable.ic_launcher_foreground, "No", PiN)
                    .addAction(R.drawable.ic_launcher_foreground, "Late", PiL)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(uri)
                    .build();

            notificationManager.notify(nId, notification);


        }

    }}
