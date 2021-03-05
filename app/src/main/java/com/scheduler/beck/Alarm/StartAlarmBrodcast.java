package com.scheduler.beck.Alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.scheduler.beck.MainActivity;
import com.scheduler.beck.R;

import static com.scheduler.beck.Alarm.NotificationChannels.CHANNEL_1_ID;
import static com.scheduler.beck.RegisterClassActivity.TAG;

public class StartAlarmBrodcast extends BroadcastReceiver {
    private static int id;
    private NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        id = 57;
        Bundle bundle = intent.getExtras();
        notificationManager = NotificationManagerCompat.from(context);
        assert bundle != null;
        String startClass = bundle.getString("startClass");
        String link = bundle.getString("link");
        Log.d(TAG, "1. start Class " + startClass);

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.paper_guitar);
        String message = "The class starts soon!";

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                ++id, activityIntent, 0);

        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.putExtra("link", link);
        broadcastIntent.putExtra("id", id);


        PendingIntent actionIntent = PendingIntent.getBroadcast(context,
                id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(startClass)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Open class link", actionIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(soundUri)
                .build();

        notificationManager.notify(id, notification);

    }
}
