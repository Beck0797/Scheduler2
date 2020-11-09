package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scheduler2.Alarm.AlarmBrodcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addAssignment extends AppCompatActivity  implements View.OnClickListener {
    public static final String TAG = "addAssignment.this";
    private TextView btnTime_start_select, btnAlarm_select, btnChooseSound, btnAddAssignmentDateselect, btnAddAssignmentAlarmDateselect;
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
    private String take;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        // firesbase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");
        hashMap =(HashMap<String, String>) getIntent().getSerializableExtra("arraylist");
        list_of_classes = new ArrayList<>();
        for(Map.Entry<String, String> entry : hashMap.entrySet()) {
                list_of_classes.add(entry.getKey());
        }
        isAssignmentDate = false;
        isAlarmDate = false;
        spinner = findViewById(R.id.spinner_subjects);
        mCalendarView = findViewById(R.id.calendarView);
        btnAlarm_select = findViewById(R.id.btnAlarmTime);
        btnAddAssignmentAlarmDateselect = findViewById(R.id.btnAddAssignmentAlarmDateselect);
        btnChooseSound = findViewById(R.id.btnChooseSound);
        btnTime_start_select = findViewById(R.id.btnDueTime_select);
        btnAddAssignmentDateselect = findViewById(R.id.btnAddAssignmentDateselect);
        assignmentTitle = findViewById(R.id.assignmentTitle);

        btnAddAssignmentDateselect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnAddAssignmentDateselect.setText("");
                Toast.makeText(addAssignment.this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
                isAssignmentDate = true;
                return true;
            }
        });

        btnAddAssignmentAlarmDateselect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnAddAssignmentAlarmDateselect.setText("");
                Toast.makeText(addAssignment.this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
                isAlarmDate = true;
                return true;
            }
        });

        btnAlarm_select.setOnClickListener(this);
        btnTime_start_select.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month +1;


                Log.d(TAG, "month and date: " + dayOfMonth + month);
                String date = year +"-"+ (month < 10 ? "0" + month : month) + "-"+ (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) ;
                if(isAssignmentDate){
                    btnAddAssignmentDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentDateselect.setText(date);
                    isAssignmentDate = false;
                }
                if(isAlarmDate){
                    btnAddAssignmentAlarmDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentAlarmDateselect.setText(date);
                    alarmDate = year +"-"+ (month < 10 ? "0" + month : month) + "-"+ (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) ;
                    isAlarmDate = false;
                }


            }
        });

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item , list_of_classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelected(true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if(list_of_classes.isEmpty() == false) {
                   take_class = list_of_classes.get(position).toString();
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(addAssignment.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (v.getId()) {
                    case R.id.btnDueTime_select:
                        btnTime_start_select.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10? "0" + minute : minute));
                        break;
                    case R.id.btnAlarmTime:
                        timeTonotify = hourOfDay + ":" + minute;
                        btnAlarm_select.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10? "0" + minute : minute));
                        alarmTime = FormatTime(hourOfDay, minute);
//                        Log.d(TAG, "111111111111111111" + "timeTonotify is " + timeTonotify);
//                        Toast.makeText(addAssignment.this, "Time to notify is " + timeTonotify, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        }, hour, min, android.text.format.DateFormat.is24HourFormat( addAssignment.this));
        timePickerDialog.show();
    }

    public void choose_audio(View view) {

        //Application needs read storage permission for Builder.TYPE_MUSIC .
        if (ActivityCompat.checkSelfPermission(addAssignment.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog
                    .Builder(addAssignment.this, getSupportFragmentManager())

                    //Set title of the dialog.
                    //If set null, no title will be displayed.
                    .setTitle("Select ringtone")

                    //set the currently selected uri, to mark that ringtone as checked by default.
                    //If no ringtone is currently selected, pass null.
                    .setCurrentRingtoneUri(mCurrentSelectedUri)

                    //Allow user to select default ringtone set in phone settings.
                    .displayDefaultRingtone(true)

                    //Allow user to select silent (i.e. No ringtone.).
                    .displaySilentRingtone(true)

                    //set the text to display of the positive (ok) button.
                    //If not set OK will be the default text.
                    .setPositiveButtonText("SET RINGTONE")

                    //set text to display as negative button.
                    //If set null, negative button will not be displayed.
                    .setCancelButtonText("CANCEL")

                    //Set flag true if you want to play the sample of the clicked tone.
                    .setPlaySampleWhileSelection(true)

                    //Set the callback listener.
                    .setListener(new RingtonePickerListener() {
                        @Override
                        public void OnRingtoneSelected(@NonNull String ringtoneName, Uri ringtoneUri) {
                            mCurrentSelectedUri = ringtoneUri;
                            btnChooseSound.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                            btnChooseSound.setText(String.format("%s", ringtoneName));
                        }
                    });
            ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);



            //Display the dialog.
            ringtonePickerBuilder.show();
        } else {
            Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(addAssignment.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
    }

    public void onAddAssignmentClicked(View view) {
        title = assignmentTitle.getText().toString().trim();
        if(title.isEmpty()){
            Toast.makeText(this, "Enter Assignment title", Toast.LENGTH_SHORT).show();
        }else{
            setAlarm(alarmDate, alarmTime);
            Toast.makeText(addAssignment.this, "Alarm set to " + alarmDate + " " + alarmTime, Toast.LENGTH_SHORT).show();

        }

        Log.d(TAG, "working" + hashMap.get(take_class));
        AssignmentCons assignmentCons = new AssignmentCons(take_class,assignmentTitle.getText().toString(), btnAddAssignmentDateselect.getText().toString(), btnTime_start_select.getText().toString());
        databaseReference.child(hashMap.get(take_class)).child("assignments").push().setValue(assignmentCons);
    }
    public void onbtnAddAssignmentDateselectClicked(View view) {
        if(btnAddAssignmentDateselect.getText().toString().equals("")){
            Toast.makeText(this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
            isAssignmentDate = true;

        }else{
            Toast.makeText(this, "Long Press to change the date", Toast.LENGTH_SHORT).show();

        }

    }


    public void onbtnAddAssignmentAlarmDateselectClicked(View view) {

        if(btnAddAssignmentAlarmDateselect.getText().toString().equals("")){
            Toast.makeText(this, "Choose Date from Calendar", Toast.LENGTH_SHORT).show();
            isAlarmDate = true;
        }
        else{
            Toast.makeText(this, "Long Press to change the date", Toast.LENGTH_SHORT).show();

        }
    }

    public void onBackButtonClicked(View view) {
        Intent i = new Intent(getApplicationContext(), assignment.class);
        startActivity(i);
        finish();
    }

    private void setAlarm(String date, String time) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);

        intent.putExtra("title", title);
        intent.putExtra("courseName", courseName);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // going back to assignment list after setting alarm
        Intent i = new Intent(getApplicationContext(), assignment.class);
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
}

