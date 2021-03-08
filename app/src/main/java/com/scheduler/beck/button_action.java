package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scheduler.beck.Models.check_attendance_cons;
import com.scheduler.beck.Utils.ThemeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.scheduler.beck.RegisterClassActivity.TAG;

public class button_action extends BroadcastReceiver {
    private String className, status;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;



    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("attendance");

        int id = intent.getIntExtra("id", 0);

        if(intent.hasExtra("yes")){
            className = intent.getStringExtra("class");
            status = "attendance";
            Log.d(TAG, "Yes");

        }else if(intent.hasExtra("no")){
            className = intent.getStringExtra("class");
            status = "absence";
            Log.d(TAG, "No");
        } else if(intent.hasExtra("late")) {
            className = intent.getStringExtra("class");
            status = "tardiness";
            Log.d(TAG, "Late");
//            Toast.makeText(this, "Late button is clicked for" + className, Toast.LENGTH_SHORT).show();
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d(TAG, "current date" + date);
        check_attendance_cons checkAttendanceCons = new check_attendance_cons(className, status, date);
        databaseReference.push().setValue(checkAttendanceCons);
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // this closes the notification panel
        context.sendBroadcast(it);
        notificationManager.cancel(id);

//        Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
//        startActivity(i);

    }
}