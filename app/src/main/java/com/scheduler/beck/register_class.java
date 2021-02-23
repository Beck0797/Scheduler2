package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.scheduler.beck.Alarm.AlarmBrodcast;
import com.scheduler.beck.Alarm.StartAlarmBrodcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.CookieHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class register_class extends AppCompatActivity{
    private TextView time_start, time_end, time_alarm;
    public static final String TAG = "register_class";
    public static boolean isLastFrame;
    public static Map<String, ArrayList<List<Double>>> myMap;
    private double startTime, endTime;
    String day;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    ArrayAdapter adapter;
    String recieved_key;
    private boolean done = false;
    private DatabaseReference myRef;
    private String alarmTime, courseName;
    private int h, m, hS, mS;
    private String time_s, time_e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_register_class);
        // time picker
        init();
        setFragment(1);


//        try {
//            Intent intent = getIntent();
//            recieved_key = intent.getStringExtra("class_key").toString();
//            Log.d(TAG, "id recieved here");
//            subject_namem.setText(intent.getStringExtra("class_name"));
//            professor_name.setText(intent.getStringExtra("class_professor"));
//            room_number.setText(intent.getStringExtra("class_room"));
//            time_start.setText(intent.getStringExtra("class_start_time"));
//            time_end.setText(intent.getStringExtra("class_end_time"));
//            time_alarm.setText(intent.getStringExtra("class_alarm_time"));
//            webex_link.setText(intent.getStringExtra("class_url_link"));
//            done = true;
//
//
//        } catch (NullPointerException e) {
//            done = false;
//            System.err.println("Null pointer exception");
//        }




        //adding sample data
        /*ArrayList<
                List<Double>> monClasses = new ArrayList<List<Double>>() {{
            add(Arrays.<Double>asList(9.15, 12.5));

            add(Arrays.<Double>asList(13.30, 15.6));
            add(Arrays.<Double>asList(17.4, 19.50));
        }};*/
        myMap =(HashMap<String, ArrayList<List<Double>>>) getIntent().getSerializableExtra("hash_map");

        //myMap.put("Monday", monClasses); //
    }
    // save class info
    public void onClickInsertClass(View view) {

        if(TextUtils.isEmpty(time_start.getText().toString())) {
            Toast.makeText(getApplicationContext(), "select starting time", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(time_end.getText().toString())) {
            Toast.makeText(getApplicationContext(), "select ending time ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(time_alarm.getText().toString())) {
            Toast.makeText(getApplicationContext(), "select alarming time ", Toast.LENGTH_SHORT).show();
            return;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //String time_s = Integer.parseInt(time_start.getText().toString()) < 10 ? "0" + time_start.getText().toString() : time_start.getText().toString();
        //String time_e = Integer.parseInt(time_end.getText().toString()) < 10 ? "0"

//
//        if(isOverlaps()){
//            Toast.makeText(getApplicationContext(), "It overlaps with another class", Toast.LENGTH_SHORT).show();
//            return;
//        }else{
//            if(done)
//                myRef.child(recieved_key).setValue(course_info);
//            else
//                myRef.push().setValue(course_info);
//            Toast.makeText(getApplicationContext(), "Class registered!", Toast.LENGTH_SHORT).show();
//        }

//        setAlarm(day, h, m);
//        setAlarmStart(day, hS, mS);
        Toast.makeText(this, "Set alarm", Toast.LENGTH_SHORT).show();




    }

//    // time picker method
//    @Override
//    public void onClick(final View v) {
//
//    }
    private void setFragment(int id) {
        isLastFrame = true;
        Fragment fragment = new Fragment();

        if(id == 2){
            isLastFrame = false;
            fragment = new tasks(2);
        }else if(id == 1){
            fragment = new tasks(1);
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lytFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void init(){
        isLastFrame = false;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");


        myMap = new HashMap<>();
    }


    public boolean isOverlaps(){

        //Classes{[9, 12], [13, 15], [17, 19]}
        try {
            ArrayList<List<Double>> classes = new ArrayList<>();
            classes = myMap.get(day);
            Log.d(TAG, "class day:" + day);
            for(int i = 0; i < classes.size(); i++) {
                for(int j = 0; j < classes.get(i).size(); j++) {
                    Log.d(TAG, "class time: " + classes.get(i).get(j));
                }
            }
            Log.d(TAG, "Current start_time: " + startTime);
            Log.d(TAG, "Current end_time" + endTime);
            if (!classes.isEmpty()) {
                for (int i = 0; i < classes.size(); ++i) {
                    if(classes.get(i).get(0) > startTime && classes.get(i).get(0) <= endTime) {
                        return true;
                    }else if(classes.get(i).get(0) <= startTime && classes.get(i).get(1) >= endTime) {
                        return true;
                    }else if(classes.get(i).get(1) > startTime && classes.get(i).get(1) < endTime) {
                        return true;
                    }
                }
            }
        }catch (NullPointerException e) {
            return false;
        }
        return false;
    }


    public void backToCourseList(View view) {
        if (isLastFrame) {
            isLastFrame = false;
            Intent i = new Intent(getApplicationContext(), courseList.class);
            startActivity(i);
            finish();

        }else{
            onBackPressed();

        }


    }

    @Override
    public void onBackPressed() {
        if (isLastFrame) {
                isLastFrame = false;
                Intent i = new Intent(getApplicationContext(), courseList.class);
                startActivity(i);
                finish();

        }else{
                isLastFrame = true;
            super.onBackPressed();


        }


    }







}