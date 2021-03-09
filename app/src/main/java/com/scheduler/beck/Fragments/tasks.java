package com.scheduler.beck.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scheduler.beck.Alarm.AlarmAttendBroadcast;
import com.scheduler.beck.Alarm.AlarmStartBroadcast;
import com.scheduler.beck.Models.Course_Info;
import com.scheduler.beck.R;
import com.scheduler.beck.courseList;
import com.scheduler.beck.RegisterClassActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;
import static com.scheduler.beck.RegisterClassActivity.myMap;

public class tasks extends Fragment implements View.OnClickListener {
    private static final String TAG = "" ;
    private static List<Integer> days;
    private int id;
    private TextView M_time_start, M_time_end, M_time_alarm,
                     T_time_start, T_time_end, T_time_alarm,
                     W_time_start, W_time_end, W_time_alarm,
                     Th_time_start, Th_time_end, Th_time_alarm,
                     F_time_start, F_time_end, F_time_alarm;

    CheckBox Mon, Tue, Wed, Thu, Fri;
    Button btnContinue, btnRegister;
    LinearLayout linLytMon, linLytTue, linLytWed, linLytThu, linLytFri;
    private static String recieved_key, c_key;
    private static boolean done;
    private EditText subject_namem,professor_name,room_number,webex_link;
    private Course_Info course_info;
    private  static String roomNumber, webPageLink, sub_name, pf_name;
    private static boolean isMultiple;

    public static Map<String, PendingIntent> alarmStartMap;
    public static Map<String, PendingIntent> alarmAttendMap;


    private static int requestCode;


    private int h, m, hS, mS;
    private double startTime, endTime;
    private Intent intents;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;






    public tasks(int id, Intent intent) {
        this.id = id;
        this.intents = intent;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout1, container,
                false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");

        if (this.id == 2) {
            view = inflater.inflate(R.layout.layout2, container, false);
            initViewsL2(view);

            for (int i : days) {
                if (i == 1){
                    linLytMon.setVisibility(View.VISIBLE);
                    M_time_alarm.setOnClickListener(this);
                    M_time_end.setOnClickListener(this);
                    M_time_start.setOnClickListener(this);
                }
                if (i == 2){
                    linLytTue.setVisibility(View.VISIBLE);
                    T_time_alarm.setOnClickListener(this);
                    T_time_end.setOnClickListener(this);
                    T_time_start.setOnClickListener(this);
                }
                if (i == 3){
                    linLytWed.setVisibility(View.VISIBLE);
                    W_time_alarm.setOnClickListener(this);
                    W_time_end.setOnClickListener(this);
                    W_time_start.setOnClickListener(this);
                }
                if (i == 4) {
                    linLytThu.setVisibility(View.VISIBLE);
                    Th_time_alarm.setOnClickListener(this);
                    Th_time_end.setOnClickListener(this);
                    Th_time_start.setOnClickListener(this);
                }
                if (i == 5){
                    linLytFri.setVisibility(View.VISIBLE);
                    F_time_alarm.setOnClickListener(this);
                    F_time_end.setOnClickListener(this);
                    F_time_start.setOnClickListener(this);
                }
            }

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkFields2()){
                        createCourses();
                    }

                }
            });


        } else if (this.id == 1) {
            initViewsL1(view);
            btnContinue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    checkIsMultiple();

                    if(checkFields()){
                        sub_name = subject_namem.getText().toString().trim();
                        pf_name = professor_name.getText().toString().trim();

                        initFragment2();
                    }



                }
            });
        }


        return view;
    }

    private boolean checkFields2() {
        for(int i : days){
            if(i == 1){
                if(TextUtils.isEmpty(M_time_start.getText().toString())) {
                    Toast.makeText(getActivity(), "select starting time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(M_time_end.getText().toString())) {
                    Toast.makeText(getActivity(), "select ending time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(M_time_alarm.getText().toString())) {
                    Toast.makeText(getActivity(), "select alarming time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(i == 2){
                if(TextUtils.isEmpty(T_time_start.getText().toString())) {
                    Toast.makeText(getActivity(), "select starting time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(T_time_end.getText().toString())) {
                    Toast.makeText(getActivity(), "select ending time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(T_time_alarm.getText().toString())) {
                    Toast.makeText(getActivity(), "select alarming time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(i == 3){
                if(TextUtils.isEmpty(W_time_start.getText().toString())) {
                    Toast.makeText(getActivity(), "select starting time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(W_time_end.getText().toString())) {
                    Toast.makeText(getActivity(), "select ending time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(W_time_alarm.getText().toString())) {
                    Toast.makeText(getActivity(), "select alarming time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(i == 4){
                if(TextUtils.isEmpty(Th_time_start.getText().toString())) {
                    Toast.makeText(getActivity(), "select starting time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(Th_time_end.getText().toString())) {
                    Toast.makeText(getActivity(), "select ending time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(Th_time_alarm.getText().toString())) {
                    Toast.makeText(getActivity(), "select alarming time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if(i == 5){
                if(TextUtils.isEmpty(F_time_start.getText().toString())) {
                    Toast.makeText(getActivity(), "select starting time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(TextUtils.isEmpty(F_time_end.getText().toString())) {
                    Toast.makeText(getActivity(), "select ending time ", Toast.LENGTH_SHORT).show();
                    return false;

                }
                if(TextUtils.isEmpty(F_time_alarm.getText().toString())) {
                    Toast.makeText(getActivity(), "select alarming time ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;

    }

    private void checkIsMultiple() {
        int i = 0;
        if(Mon.isChecked()){
            days.add(1);
            i++;
        }
        if(Tue.isChecked()){
            i++;
            days.add(2);
        }
        if(Wed.isChecked()){
            i++;
            days.add(3);
        }
        if(Thu.isChecked()){
            i++;
            days.add(4);
        }if(Fri.isChecked()){
            i++;
            days.add(5);
        }
        Log.d("IsMuull", "I is : " + i);
        if(i > 1) {
            isMultiple = true;
        }



    }

    private void createCourses() {
        Log.d("IsMuull", "I is : " + isMultiple);

        for(int i : days){
            if(i == 1){
                saveCourse("Monday",
                        new Course_Info(
                                sub_name,
                                pf_name,
                                roomNumber,
                                "Monday",
                                M_time_start.getText().toString().trim(),
                                M_time_end.getText().toString().trim(),
                                M_time_alarm.getText().toString().trim(),
                                webPageLink,
                                isMultiple
                        )
                );
            }
            if(i == 2){
                saveCourse("Tuesday",
                        new Course_Info(
                                sub_name,
                                pf_name,
                                roomNumber,
                                "Tuesday",
                                T_time_start.getText().toString().trim(),
                                T_time_end.getText().toString().trim(),
                                T_time_alarm.getText().toString().trim(),
                                webPageLink,
                                isMultiple
                        )
                );

            }
            if(i == 3){
                saveCourse("Wednesday",
                        new Course_Info(
                                sub_name,
                                pf_name,
                                roomNumber,
                                "Wednesday",
                                W_time_start.getText().toString().trim(),
                                W_time_end.getText().toString().trim(),
                                W_time_alarm.getText().toString().trim(),
                                webPageLink,
                                isMultiple
                        )
                );

            }
            if(i == 4){
                saveCourse("Thursday",
                        new Course_Info(
                                sub_name,
                                pf_name,
                                roomNumber,
                                "Thursday",
                                Th_time_start.getText().toString().trim(),
                                Th_time_end.getText().toString().trim(),
                                Th_time_alarm.getText().toString().trim(),
                                webPageLink,
                                isMultiple
                        )
                );

            }
            if(i == 5){
                saveCourse("Friday",
                        new Course_Info(
                                sub_name,
                                pf_name,
                                roomNumber,
                                "Friday",
                                F_time_start.getText().toString().trim(),
                                F_time_end.getText().toString().trim(),
                                F_time_alarm.getText().toString().trim(),
                                webPageLink,
                                isMultiple
                        )
                );

            }
        }

    }

    private void saveCourse(String day, Course_Info course_inform) {

        if(isOverlaps(day)){
            Toast.makeText(getContext(), "It overlaps with another class", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(done){
                myRef.child(recieved_key).setValue(course_inform);
            }

            else{
                myRef.child(c_key).setValue(course_inform);
            }

            Toast.makeText(getContext(), "Class registered!", Toast.LENGTH_SHORT).show();
        }

        setAttendanceAlarm(day, h, m);// it will check attendance after five minutes once class has started.

        setAlarmStart(day, hS, mS-1);

        goToCourseList();

    }

    private void goToCourseList() {
        startActivity(new Intent(getContext(), courseList.class));
        getActivity().finish();
    }

    private boolean checkFields() {
        if(isMultiple && done){
            Toast.makeText(getActivity(), "Can not be updated for multiple days!", Toast.LENGTH_SHORT).show();
            isMultiple = false;
            setCheckBoxUnchecked();
            return false;
        }

        if(TextUtils.isEmpty(subject_namem.getText().toString())) {
            Toast.makeText(getActivity(), "enter class name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(professor_name.getText().toString())) {
            Toast.makeText(getActivity(), "enter professor name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(days.isEmpty()){
            Toast.makeText(getActivity(), "Choose class day to proceed", Toast.LENGTH_SHORT).show();
            return false;
        }
        webPageLink = webex_link.getText().toString().trim();
        roomNumber = room_number.getText().toString().trim();
        if(webPageLink.equals("")){
            webPageLink = "not given";
        }else if(!isLink(webPageLink)){
            Toast.makeText(getContext(), "Link should start with \"http\"", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(roomNumber.equals("")){
            roomNumber = "not given";
        }
        return true;
    }

    private boolean isLink(String lnk) {
        if(lnk.length() > 4){
            return lnk.startsWith("http");
        }
        return false;
    }

    private void setCheckBoxUnchecked() {
        Mon.setChecked(false);
        Tue.setChecked(false);
        Wed.setChecked(false);
        Thu.setChecked(false);
        Fri.setChecked(false);
        days = new ArrayList<>();

    }

    private void initFragment2() {
        Fragment fragment = new tasks(2, null);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lytFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        RegisterClassActivity.isLastFrame = false;

    }

    private void initViewsL1(View view) {
        Mon = view.findViewById(R.id.checkBoxMon);
        Tue = view.findViewById(R.id.checkBoxTue);
        Wed = view.findViewById(R.id.checkBoxWed);
        Thu = view.findViewById(R.id.checkBoxThu);
        Fri = view.findViewById(R.id.checkBoxFri);

        btnContinue = view.findViewById(R.id.btnCont);
        days = new ArrayList<>();

        subject_namem = view.findViewById(R.id.subject_name);
        professor_name = view.findViewById(R.id.professor_name);
        room_number = view.findViewById(R.id.room_number);
        webex_link = view.findViewById(R.id.webex_link);

        course_info = new Course_Info();
        isMultiple = false;


        try {
            recieved_key = intents.getStringExtra("class_key").toString();
            subject_namem.setText(intents.getStringExtra("class_name"));
            professor_name.setText(intents.getStringExtra("class_professor"));
            room_number.setText(intents.getStringExtra("class_room"));
            webex_link.setText(intents.getStringExtra("class_url_link"));
            done = true;
            c_key = recieved_key;



        } catch (NullPointerException e) {
            done = false;
            c_key =  myRef.push().getKey();

        }

        try{
            if(alarmStartMap == null){
            alarmStartMap = new HashMap<>();
            alarmAttendMap = new HashMap<>();

            requestCode = 100;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initViewsL2(View view) {

        linLytMon = view.findViewById(R.id.linLytMon);
        linLytTue = view.findViewById(R.id.linLytTue);
        linLytWed = view.findViewById(R.id.linLytWed);
        linLytThu = view.findViewById(R.id.linLytThu);
        linLytFri = view.findViewById(R.id.linLytFri);
        btnRegister = view.findViewById(R.id.btnRegister);

        M_time_start = view.findViewById(R.id.time_start_selectMon);
        M_time_end = view.findViewById(R.id.time_end_selectMon);
        M_time_alarm = view.findViewById(R.id.alarm_selectMon);

        T_time_start = view.findViewById(R.id.time_start_selectTue);
        T_time_end = view.findViewById(R.id.time_end_selectTue);
        T_time_alarm = view.findViewById(R.id.alarm_selectTue);

        W_time_start = view.findViewById(R.id.time_start_selectWed);
        W_time_end = view.findViewById(R.id.time_end_selectWed);
        W_time_alarm = view.findViewById(R.id.alarm_selectWed);

        Th_time_start = view.findViewById(R.id.time_start_selectThu);
        Th_time_end = view.findViewById(R.id.time_end_selectThu);
        Th_time_alarm = view.findViewById(R.id.alarm_selectThu);

        F_time_start = view.findViewById(R.id.time_start_selectFri);
        F_time_end = view.findViewById(R.id.time_end_selectFri);
        F_time_alarm = view.findViewById(R.id.alarm_selectFri);



    }

    @Override
    public void onClick(final View v) {
        Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int min = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (v.getId()) {
                    case R.id.time_start_selectMon:
                        OnStartSelect(M_time_start, hourOfDay, minute);
                        break;
                    case R.id.time_end_selectMon:
                        OnEndSelect(M_time_end, hourOfDay, minute);
                        break;
                    case R.id.alarm_selectMon:
                        OnAlarmSelects(M_time_alarm, hourOfDay, minute);
                        break;

                    case R.id.time_start_selectTue:
                        OnStartSelect(T_time_start, hourOfDay, minute);
                        break;
                    case R.id.time_end_selectTue:
                        OnEndSelect(T_time_end, hourOfDay, minute);
                        break;
                    case R.id.alarm_selectTue:
                        OnAlarmSelects(T_time_alarm, hourOfDay, minute);
                        break;

                    case R.id.time_start_selectWed:
                        OnStartSelect(W_time_start, hourOfDay, minute);
                        break;
                    case R.id.time_end_selectWed:
                        OnEndSelect(W_time_end, hourOfDay, minute);
                        break;
                    case R.id.alarm_selectWed:
                        OnAlarmSelects(W_time_alarm, hourOfDay, minute);
                        break;

                    case R.id.time_start_selectThu:
                        OnStartSelect(Th_time_start, hourOfDay, minute);
                        break;
                    case R.id.time_end_selectThu:
                        OnEndSelect(Th_time_end, hourOfDay, minute);
                        break;
                    case R.id.alarm_selectThu:
                        OnAlarmSelects(Th_time_alarm, hourOfDay, minute);
                        break;

                    case R.id.time_start_selectFri:
                        OnStartSelect(F_time_start, hourOfDay, minute);
                        break;
                    case R.id.time_end_selectFri:
                        OnEndSelect(F_time_end, hourOfDay, minute);
                        break;
                    case R.id.alarm_selectFri:
                        OnAlarmSelects(F_time_alarm, hourOfDay, minute);
                        break;
                }
            }
        },hour,min,android.text.format.DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();
    }

    private void OnAlarmSelects(TextView time_alarm, int hourOfDay, int minute) {
        String hour = "0", sMinute = "0", st;
        if(hourOfDay < 10){
            hour+=hourOfDay;
        }else{
            hour = ""+hourOfDay;
        }
        if(minute < 10){
            sMinute+=minute;
        }else{
            sMinute = ""+minute;
        }
        st = hour + ":" + sMinute;
        time_alarm.setText(st);
        hS = hourOfDay;
        mS = minute;
    }

    private void OnEndSelect(TextView time_end, int hourOfDay, int minute) {
        String temp1 = "" + hourOfDay + "." + minute;
        endTime = Double.parseDouble(temp1);
        String hour = "0", sMinute = "0", st;
        if(hourOfDay < 10){
            hour+=hourOfDay;
        }else{
            hour = ""+hourOfDay;
        }
        if(minute < 10){
            sMinute+=minute;
        }else{
            sMinute = ""+minute;
        }
        st = hour + ":" + sMinute;
        time_end.setText(st);
    }

    private void OnStartSelect(TextView time_start, int hourOfDay, int minute) {
        String temp = "" + hourOfDay + "." + minute;
        startTime = Double.parseDouble(temp);
        h = hourOfDay;
        m = minute;
        String hour = "0", sMinute = "0", st;
        if(hourOfDay < 10){
            hour+=hourOfDay;
        }else{
            hour = ""+hourOfDay;
        }
        if(minute < 10){
            sMinute+=minute;
        }else{
            sMinute = ""+minute;
        }
        st = hour + ":" + sMinute;
        time_start.setText(st);


    }

    public boolean isOverlaps(String day){

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

    private void setAttendanceAlarm(String day, int h, int m) {

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

        Intent intent = new Intent(getContext(), AlarmAttendBroadcast.class);
        intent.putExtra("class", sub_name);
        intent.putExtra("c_key", c_key);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ++requestCode, intent, 0);
        AlarmManager am = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        alarmAttendMap.put(c_key, pendingIntent);


        // going back to assignment list after setting alarm
        Intent i = new Intent(getContext(), courseList.class);
        startActivity(i);
        getActivity().finish();
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

        Intent iAlarmStart = new Intent(getContext(), AlarmStartBroadcast.class);
        iAlarmStart.putExtra("startClass", sub_name);
        iAlarmStart.putExtra("link", webPageLink );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ++requestCode, iAlarmStart, 0);
        AlarmManager am = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        alarmStartMap.put(c_key, pendingIntent);

        Log.d("cancelAlarm", "User Id in reg is "+ c_key);


        // going back to assignment list after setting alarm
        Intent i = new Intent(getContext(), courseList.class);
        startActivity(i);
        getActivity().finish();
    }

}
