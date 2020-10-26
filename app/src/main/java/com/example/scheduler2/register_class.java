package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class register_class extends AppCompatActivity implements View.OnClickListener {
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private TextView time_start, time_end, time_alarm;
    public static final String TAG = "register_class";

    private Map<String, ArrayList<ArrayList<Double>>> myMap;
    private double startTime, endTime;
    String day;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private DatabaseReference myRef;
    private EditText subject_namem,professor_name,room_number,webex_link;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");


        myMap = new HashMap<>();
        //adding sample data
        ArrayList<ArrayList<Double>> monClasses = new ArrayList<>(); // Array list for Monday classes

        // monClasses{[9, 12], [13, 15], [17, 19]}

        myMap.put("Monday", monClasses); //

        // to select days of week
        Spinner spinner = (Spinner) findViewById(R.id.spinner_days);

        ArrayList<String> plantList= new ArrayList<String>(Arrays.asList(days));
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, plantList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelected(true);
        SpannerItem ob = new SpannerItem();
        spinner.setOnItemSelectedListener(ob);




    }
    // save class info
    public void onClickInsertClass(View view) {

        course_info = new Course_Info(subject_namem.getText().toString(),  professor_name.getText().toString(), room_number.getText().toString(),
                day, time_start.getText().toString(), time_end.getText().toString(), time_alarm.getText().toString(),webex_link.getText().toString());
        /*if(isOverlaps()){
            Toast.makeText(getApplicationContext(), "It overlaps with another class", Toast.LENGTH_SHORT).show();
        }else{
*/
            myRef.push().setValue(course_info);
            Toast.makeText(getApplicationContext(), "Class registered!", Toast.LENGTH_SHORT).show();
       /* }*/



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
                                break;
                            case R.id.time_end_select:
                                String temp1 = "" + hourOfDay + "." + minute;
                                endTime = Double.parseDouble(temp1);
                                time_end.setText(hourOfDay + ":" + minute);
                                break;
                            case R.id.alarm_select:
                                time_alarm.setText(hourOfDay + ":" + minute);
                                break;
                            default:
                                break;
                        }
                    }
                },hour,min,android.text.format.DateFormat.is24HourFormat(register_class.this));
        timePickerDialog.show();
    }


    public boolean isOverlaps(){
        ArrayList<ArrayList<Double>> classes = new ArrayList<>();
        classes = myMap.get(day);
        //Classes{[9, 12], [13, 15], [17, 19]}
        for(int i = 0; i< classes.size(); ++i){
            if(classes.get(i).get(0) <= endTime && classes.get(i).get(1) >= endTime){
                return true;
            }
            if(classes.get(i).get(0) <= startTime && classes.get(i).get(1) >= startTime){
                return true;
            }
        }
        return false;
    }


    public void backToCourseList(View view) {
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