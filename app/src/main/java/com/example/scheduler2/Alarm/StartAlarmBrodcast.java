package com.example.scheduler2.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.scheduler2.R;
import com.example.scheduler2.courseList;

import static com.example.scheduler2.register_class.TAG;

public class StartAlarmBrodcast extends BroadcastReceiver {
    private static int id;
    @Override
    public void onReceive(Context context, Intent intent) {
        id = 57;
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String startClass = bundle.getString("startClass");
        Log.d(TAG, "1. start Class " + startClass);


        Intent intent1;
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);

        intent1 = new Intent(context, courseList.class);
        contentView.setTextViewText(R.id.title, startClass);
        contentView.setTextViewText(R.id.course, "The class starts soon!");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");
        mBuilder.setSmallIcon(R.drawable.icon_notification);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.PRIORITY_HIGH;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, ++id, intent1, PendingIntent.FLAG_ONE_SHOT);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Log.d(TAG, "Uri is " + uri);
//        mBuilder.setSound(uri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        Notification notification = mBuilder.build();
        assert notificationManager != null;
        notificationManager.notify(++id, notification);

    }
}
