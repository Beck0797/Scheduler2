package com.scheduler.beck.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.scheduler.beck.MainActivity;
import com.scheduler.beck.R;
import static com.scheduler.beck.Alarm.NotificationChannels.CHANNEL_1_ID;
import static com.scheduler.beck.RegisterClassActivity.TAG;

public class AlarmStartBroadcast extends BroadcastReceiver {
    private static int id;
    private NotificationManagerCompat notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        id = (int)(Math.random() * 1000 + 1);
        Bundle bundle = intent.getExtras();
        notificationManager = NotificationManagerCompat.from(context);
        assert bundle != null;
        String startClass = bundle.getString("startClass");
        String link = bundle.getString("link");
        Log.d(TAG, "1. start Class " + startClass);

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.paper_guitar);
        String message = "The class starts soon!";

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.start_notification_layout);
        //assignment title
        contentView.setTextViewText(R.id.txtClass, startClass);

        Intent broadcastIntent = new Intent(context, AlarmStartReceiver.class);
        broadcastIntent.putExtra("link", link);
        broadcastIntent.putExtra("id", id);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, id+534,
                broadcastIntent, 0);

        contentView.setOnClickPendingIntent(R.id.btnGoToClass,
                pendingSwitchIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_1_ID);
        mBuilder.setSmallIcon(R.drawable.icon_notification);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.PRIORITY_DEFAULT;
        mBuilder.setSound(soundUri);



        mBuilder.setContent(contentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(CHANNEL_1_ID);
        }
        Notification notification = mBuilder.build();
        assert notificationManager != null;
        notificationManager.notify(id, notification);

    }
}
