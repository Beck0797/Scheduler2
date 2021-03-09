package com.scheduler.beck.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.scheduler.beck.SignIn;
import com.scheduler.beck.SignUp;
import com.scheduler.beck.courseList;


public class AlarmStartReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);

        Intent i = new Intent();
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // this closes the notification panel

        String link = intent.getStringExtra("link");
        int id = intent.getIntExtra("id", 0);
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        if(link.equals("not given")){
            i = new Intent(context, courseList.class);
            i.putExtra("alert", 1);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.sendBroadcast(it);
            context.startActivity(i);
//            Toast.makeText(context, "Course link is not given, Please register the course link by updating the class!", Toast.LENGTH_LONG).show();
        }else{
            Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
            i = new Intent(Intent.ACTION_VIEW, uri);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.sendBroadcast(it);
            context.startActivity(i);
        }
        notificationManager.cancel(id);


    }

}

