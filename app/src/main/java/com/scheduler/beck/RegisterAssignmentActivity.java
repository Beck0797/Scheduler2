package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.scheduler.beck.Alarm.AlarmAttendBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.scheduler.beck.Models.AssignmentCons;
import com.scheduler.beck.Utils.ThemeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterAssignmentActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "addAssignment.this";
    private TextView btnTime_start_select, btnAlarm_select, btnAddAssignmentDateselect, btnAddAssignmentAlarmDateselect;
    private Uri mCurrentSelectedUri;
    private CalendarView mCalendarView;
    private boolean isAssignmentDate, isAlarmDate;
    private EditText assignmentTitle;
    private Spinner spinner;
    private ArrayAdapter adapter;
    String take_class;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public HashMap<String, String> hashMap;
    private ArrayList<String> list_of_classes;
    private String timeTonotify, alarmTime, alarmDate, title, courseName;
    public static Map<String, PendingIntent> assignAlarmMap;
    private static int reqCode;
    Calendar c, cDue;
    private String take;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_assignment);

        try {
            if (assignAlarmMap == null) {
                assignAlarmMap = new HashMap<>();
                reqCode = 568;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // firesbase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("arraylist");
        list_of_classes = new ArrayList<>();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            list_of_classes.add(entry.getKey());
        }
        isAssignmentDate = false;
        isAlarmDate = false;
        c = Calendar.getInstance();
        cDue = Calendar.getInstance();
        spinner = findViewById(R.id.spinner_subjects);
        mCalendarView = findViewById(R.id.calendarView);
        btnAlarm_select = findViewById(R.id.btnAlarmTime);
        btnAddAssignmentAlarmDateselect = findViewById(R.id.btnAddAssignmentAlarmDateselect);
        btnTime_start_select = findViewById(R.id.btnDueTime_select);
        btnAddAssignmentDateselect = findViewById(R.id.btnAddAssignmentDateselect);
        assignmentTitle = findViewById(R.id.assignmentTitle);

        btnAddAssignmentDateselect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnAddAssignmentDateselect.setText("");
                Toast.makeText(RegisterAssignmentActivity.this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
                isAssignmentDate = true;
                return true;
            }
        });

        btnAddAssignmentAlarmDateselect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnAddAssignmentAlarmDateselect.setText("");
                Toast.makeText(RegisterAssignmentActivity.this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
                isAlarmDate = true;
                return true;
            }
        });

        btnAlarm_select.setOnClickListener(this);
        btnTime_start_select.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;


                Log.d(TAG, "month and date: " + dayOfMonth + month);
                String date = year + "-" + (month < 10 ? "0" + month : month) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
                if (isAssignmentDate) {
                    btnAddAssignmentDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentDateselect.setText(date);
                    isAssignmentDate = false;
                    cDue.set(Calendar.YEAR, year);
                    cDue.set(Calendar.MONTH, month-1);
                    cDue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
                if (isAlarmDate) {
                    btnAddAssignmentAlarmDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentAlarmDateselect.setText(date);
                    alarmDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
                    isAlarmDate = false;
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month-1);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }


            }
        });

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, list_of_classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelected(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (list_of_classes.isEmpty() == false) {
                    take_class = list_of_classes.get(position);
                    courseName = take_class;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    // save assigment data


    // time picker method
    @Override
    public void onClick(final View v) {
        Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int min = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(RegisterAssignmentActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (v.getId()) {
                    case R.id.btnDueTime_select:
                        btnTime_start_select.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
                        cDue.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cDue.set(Calendar.MINUTE, minute);
                        cDue.set(Calendar.SECOND, 0);
                        break;
                    case R.id.btnAlarmTime:
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        timeTonotify = hourOfDay + ":" + minute;
                        btnAlarm_select.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
                        alarmTime = FormatTime(hourOfDay, minute);
//                        Log.d(TAG, "111111111111111111" + "timeTonotify is " + timeTonotify);
//                        Toast.makeText(addAssignment.this, "Time to notify is " + timeTonotify, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        }, hour, min, android.text.format.DateFormat.is24HourFormat(RegisterAssignmentActivity.this));
        timePickerDialog.show();
    }

    public void onAddAssignmentClicked(View view) {
        String key = databaseReference.push().getKey();
        title = assignmentTitle.getText().toString().trim();

        if(!checkField()){
            return;
        }

        startAlarmInFlow(key);
//            setAlarm(alarmDate, alarmTime, key);
        Toast.makeText(this, "Alarm set to " + alarmDate + " " + alarmTime, Toast.LENGTH_SHORT).show();


        Log.d(TAG, "working" + hashMap.get(take_class));
        AssignmentCons assignmentCons = new AssignmentCons(take_class, assignmentTitle.getText().toString().trim(), btnAddAssignmentDateselect.getText().toString(), btnTime_start_select.getText().toString());
        String cKey = hashMap.get(take_class);

        databaseReference =  databaseReference.child(cKey);
        databaseReference.child("assignments").child(key).setValue(assignmentCons);

        Intent i = new Intent(this, AssignmentActivity.class);
        startActivity(i);
        finish();
    }

    private boolean checkField() {
        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Assignment title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(btnAddAssignmentDateselect.getText().equals("")){
            Toast.makeText(this, "Choose due date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(btnTime_start_select.getText().equals("")){
            Toast.makeText(this, "Set due time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(btnAddAssignmentAlarmDateselect.getText().equals("")){
            Toast.makeText(this, "Choose alarm date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(btnAlarm_select.getText().equals("")) {
            Toast.makeText(this, "Set alarm time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cDue.before(Calendar.getInstance())) {
            Toast.makeText(this, "Assignment can not be due to past", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (c.before(Calendar.getInstance())) {
            Toast.makeText(this, "Alarm can not be set to past", Toast.LENGTH_SHORT).show();
            return false;
        }

            return true;
    }

    public void onbtnAddAssignmentDateselectClicked(View view) {
        if (btnAddAssignmentDateselect.getText().toString().equals("")) {
            Toast.makeText(this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
            isAssignmentDate = true;

        } else {
            Toast.makeText(this, "Long Press to change the date", Toast.LENGTH_SHORT).show();

        }

    }


    public void onbtnAddAssignmentAlarmDateselectClicked(View view) {

        if (btnAddAssignmentAlarmDateselect.getText().toString().equals("")) {
            Toast.makeText(this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
            isAlarmDate = true;
        } else {
            Toast.makeText(this, "Long Press to change the date", Toast.LENGTH_SHORT).show();

        }
    }

    public void onBackButtonClicked(View view) {
        Intent i = new Intent(getApplicationContext(), AssignmentActivity.class);
        startActivity(i);
        finish();
    }


    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    private void startAlarmInFlow(String key) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmAttendBroadcast.class);
        intent.putExtra("title", title);
        intent.putExtra("courseName", courseName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ++reqCode, intent, 0);

        assignAlarmMap.put(key, pendingIntent);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, AssignmentActivity.class);
            startActivity(i);
            finish();
            return true;

        }
        return super.onKeyDown( keyCode, event );
    }
}

