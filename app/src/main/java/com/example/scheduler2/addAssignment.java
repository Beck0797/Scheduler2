package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;

import java.util.Calendar;

public class addAssignment extends AppCompatActivity implements View.OnClickListener {
    private TextView btnTime_start_select, btnAlarm_select, btnChooseSound, btnAddAssignmentDateselect, btnAddAssignmentAlarmDateselect;
    private Uri mCurrentSelectedUri;
    private CalendarView mCalendarView;
    private boolean isAssignmentDate, isAlarmDate;
    private EditText assignmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        isAssignmentDate = false;
        isAlarmDate = false;
        mCalendarView = findViewById(R.id.calendarView);
        btnAlarm_select = findViewById(R.id.btnAlarm_select);
        btnTime_start_select = findViewById(R.id.btnTime_start_select);
        btnChooseSound = findViewById(R.id.btnChooseSound);
        btnAddAssignmentDateselect = findViewById(R.id.btnAddAssignmentDateselect);
        btnAddAssignmentAlarmDateselect = findViewById(R.id.btnAddAssignmentAlarmDateselect);
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
                String date = dayOfMonth +". "+ month+". "+ year;
//                switch (view.getId()){
//
//                    case R.id.btnAddAssignmentDateselect:
//                        btnAddAssignmentDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
//                        btnAddAssignmentDateselect.setText(date);
//                        break;
//
//                    case R.id.btnAddAssignmentAlarmDateselect:
//                        btnAddAssignmentAlarmDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
//                        btnAddAssignmentAlarmDateselect.setText(date);
//                        break;
//                    default:
//                        break;
//                }
                if(isAssignmentDate == true){
                    btnAddAssignmentDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentDateselect.setText(date);
                    isAssignmentDate = false;
                }
                if(isAlarmDate == true){
                    btnAddAssignmentAlarmDateselect.setBackground(getResources().getDrawable(R.drawable.edittextunderline));
                    btnAddAssignmentAlarmDateselect.setText(date);
                    isAlarmDate = false;
                }


            }
        });

    }


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
                    case R.id.btnTime_start_select:
                        btnTime_start_select.setText(hourOfDay + ":" + minute);
                        break;
                    case R.id.btnAlarm_select:
                        btnAlarm_select.setText(hourOfDay + ":" + minute);
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
        Toast.makeText(this, "Add button is clicked", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getApplicationContext(), menu.class);
        startActivity(i);
        finish();
    }
}

