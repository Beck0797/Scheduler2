package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
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

public class button_action extends AppCompatActivity {
    private String className, status;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_button_action);
        Log.d(TAG, "button action ");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("attendance");



        if(getIntent().hasExtra("yes")){
            className = getIntent().getStringExtra("class");
            status = "attendance";
            Log.d(TAG, "Yes");
            Toast.makeText(this, "Yes button is clicked for" + className, Toast.LENGTH_SHORT).show();

        }else if(getIntent().hasExtra("no")){
            className = getIntent().getStringExtra("class");
            status = "absence";
            Log.d(TAG, "No");
            Toast.makeText(this, "No button is clicked for" + className, Toast.LENGTH_SHORT).show();
        } else if(getIntent().hasExtra("late")) {
            className = getIntent().getStringExtra("class");
            status = "tardiness";
            Log.d(TAG, "Late");
            Toast.makeText(this, "Late button is clicked for" + className, Toast.LENGTH_SHORT).show();
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d(TAG, "current date" + date);
        check_attendance_cons checkAttendanceCons = new check_attendance_cons(className, status, date);
        databaseReference.push().setValue(checkAttendanceCons);
        Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
        startActivity(i);

    }
}