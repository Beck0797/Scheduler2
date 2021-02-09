package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class register_class extends AppCompatActivity implements View.OnClickListener {
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private TextView time_start, time_end, time_alarm;
    public static final String TAG = "register_class";

    private Map<String, ArrayList<List<Double>>> myMap;
    private double startTime, endTime;
    String day;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    ArrayAdapter adapter;
    String recieved_key;
    private   boolean done = false;
    private DatabaseReference myRef;
    private EditText subject_namem,professor_name,room_number,webex_link;
    private String alarmTime, courseName;
    private int h, m, hS, mS;
    private String time_s, time_e;
    Course_Info course_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);
        // time picker

        time_start  = (TextView)findViewById(R.id.time_start_select);
        time_end  = (TextView)findViewById(R.id.time_end_select);
        time_alarm  = (TextView)findViewById(R.id.alarm_select);
        subject_namem = findViewById(R.id.subject_name);
        professor_name = findViewById(R.id.professor_name);
        room_number = findViewById(R.id.room_number);
        webex_link = findViewById(R.id.webex_link);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        time_alarm.setOnClickListener(this);
//        courseName = subject_namem.getText().toString();

        try {
            Intent intent = getIntent();
            recieved_key = intent.getStringExtra("class_key").toString();
            Log.d(TAG, "id recieved here");
            subject_namem.setText(intent.getStringExtra("class_name"));
            professor_name.setText(intent.getStringExtra("class_professor"));
            room_number.setText(intent.getStringExtra("class_room"));
            time_start.setText(intent.getStringExtra("class_start_time"));
            time_end.setText(intent.getStringExtra("class_end_time"));
            time_alarm.setText(intent.getStringExtra("class_alarm_time"));
            webex_link.setText(intent.getStringExtra("class_url_link"));
            done = true;


        } catch (NullPointerException e) {
            done = false;
            System.err.println("Null pointer exception");
        }



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");


        myMap = new HashMap<>();
        //adding sample data
        /*ArrayList<
                List<Double>> monClasses = new ArrayList<List<Double>>() {{
            add(Arrays.<Double>asList(9.15, 12.5));

            add(Arrays.<Double>asList(13.30, 15.6));
            add(Arrays.<Double>asList(17.4, 19.50));
        }};*/
        myMap =(HashMap<String, ArrayList<List<Double>>>) getIntent().getSerializableExtra("hash_map");

        //myMap.put("Monday", monClasses); //

        // to select days of week
        spinner = (Spinner) findViewById(R.id.spinner_days);

        ArrayList<String> plantList= new ArrayList<String>(Arrays.asList(days));
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, plantList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelected(true);
        SpannerItem ob = new SpannerItem();
        spinner.setOnItemSelectedListener(ob);




    }
    // save class info
    public void onClickInsertClass(View view) {
        if(TextUtils.isEmpty(subject_namem.getText().toString())) {
            Toast.makeText(getApplicationContext(), "enter class name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(professor_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "enter professor name", Toast.LENGTH_SHORT).show();
            return;
        }
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
        course_info = new Course_Info(subject_namem.getText().toString(),  professor_name.getText().toString(), room_number.getText().toString(),
                day, time_start.getText().toString(), time_end.getText().toString(), time_alarm.getText().toString(),webex_link.getText().toString(), date);

        courseName = subject_namem.getText().toString();

        if(isOverlaps()){
            Toast.makeText(getApplicationContext(), "It overlaps with another class", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(done)
                myRef.child(recieved_key).setValue(course_info);
            else
                myRef.push().setValue(course_info);
            Toast.makeText(getApplicationContext(), "Class registered!", Toast.LENGTH_SHORT).show();
        }

        setAlarm(day, h, m);
        setAlarmStart(day, hS, mS);
        Toast.makeText(this, "Set alarm", Toast.LENGTH_SHORT).show();




    }

    // time picker method
    @Override
    public void onClick(final View v) {
        Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int min = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(register_class.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (v.getId()) {
                    case R.id.time_start_select:
                        String temp = "" + hourOfDay + "." + minute;
                        startTime = Double.parseDouble(temp);
                        time_start.setText(hourOfDay + ":" + minute);
                        h = hourOfDay;
                        m = minute;
                        break;
                    case R.id.time_end_select:
                        String temp1 = "" + hourOfDay + "." + minute;
                        endTime = Double.parseDouble(temp1);
                        time_end.setText(hourOfDay + ":" + minute);
                        break;
                    case R.id.alarm_select:
                        time_alarm.setText(hourOfDay + ":" + minute);
                        hS = hourOfDay;
                        mS = minute;
                        break;
                    default:
                        break;
                }
            }
        },hour,min,android.text.format.DateFormat.is24HourFormat(register_class.this));
        timePickerDialog.show();
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

        Intent i = new Intent(getApplicationContext(), courseList.class);
        startActivity(i);
        finish();
    }
    private void setAlarm(String day, int h, int m) {

        Calendar currentDate = Calendar.getInstance();
        switch (day){
            case "Monday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Tuesday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Wednesday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Friday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;

        }

        currentDate.set(Calendar.HOUR_OF_DAY, h);
        currentDate.set(Calendar.MINUTE, m);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("class", courseName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);


        // going back to assignment list after setting alarm
        Intent i = new Intent(getApplicationContext(), courseList.class);
        startActivity(i);
        finish();
    }

    private void setAlarmStart(String day, int h, int m) {

        Calendar currentDate = Calendar.getInstance();
        switch (day){
            case "Monday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Tuesday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Wednesday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;
            case "Friday":
                while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    currentDate.add(Calendar.DATE, 1);
                }
                break;

        }

        currentDate.set(Calendar.HOUR_OF_DAY, h);
        currentDate.set(Calendar.MINUTE, m);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(getApplicationContext(), StartAlarmBrodcast.class);
        intent.putExtra("startClass", courseName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);


        // going back to assignment list after setting alarm
        Intent i = new Intent(getApplicationContext(), courseList.class);
        startActivity(i);
        finish();
    }




    class SpannerItem implements  AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            day = days[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

}