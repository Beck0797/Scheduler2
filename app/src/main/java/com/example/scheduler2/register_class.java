package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class register_class extends AppCompatActivity implements View.OnClickListener {
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private TextView time_start, time_end, time_alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);
        // time picker
        time_start  = (TextView)findViewById(R.id.time_start_select);
        time_end  = (TextView)findViewById(R.id.time_end_select);
        time_alarm  = (TextView)findViewById(R.id.alarm_select);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
        time_alarm.setOnClickListener(this);

        // to select days of week
        Spinner spinner = (Spinner) findViewById(R.id.spinner_days);
        ArrayList<String> plantList= new ArrayList<String>(Arrays.asList(days));
        final ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, plantList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // time picker method
    @Override
    public void onClick(final View v) {
        Calendar cal = Calendar.getInstance();
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final int min = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(register_class.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch (v.getId()) {
                            case R.id.time_start_select:
                                time_start.setText(hourOfDay + ":" + minute);
                                break;
                            case R.id.time_end_select:
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
}