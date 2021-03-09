package com.scheduler.beck.Alarm;

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
import java.util.HashMap;
import java.util.Map;

import static com.scheduler.beck.RegisterClassActivity.TAG;

public class AlarmAttendReceiver extends BroadcastReceiver {
    private String className, status;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;
    public static Map<String, String> attendMap;



    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("attendance");

        try{
            if(attendMap == null){
                attendMap = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int id = intent.getIntExtra("id", 0);
        int num = intent.getIntExtra("num", 4);
        String c_key = intent.getStringExtra("c_key");
        Log.d("attt", "Id is " + id);


        if(num == 1){
            className = intent.getStringExtra("class");
            status = "attendance";
            Log.d("attt", "Yes");
        }else if(num == 2){
            className = intent.getStringExtra("class");
            status = "absence";
            Log.d("attt", "No");
        }else if(num == 3){
            className = intent.getStringExtra("class");
            status = "tardiness";
            Log.d("attt", "Late");
        }else if(num == 4){
            Log.d("attt", "none");

        }

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d(TAG, "current date " + date);
        check_attendance_cons checkAttendanceCons = new check_attendance_cons(className, status, date);
        String attKey = databaseReference.push().getKey();

        databaseReference.child(attKey).setValue(checkAttendanceCons);
        attendMap.put(c_key, attKey);
        Log.d("DelAttend", "att key is " + attKey);


        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // this closes the notification panel
        context.sendBroadcast(it);
        notificationManager.cancel(id);

    }
}