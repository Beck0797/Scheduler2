package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class schedule extends AppCompatActivity {
    private static final String TAG = "schedule";
    private LinearLayout courseInfo;
    private RelativeLayout myLayout;
    private ArrayList<String> usedColors;
    private TextView popUpCourseName, popUpProfessor, popUpTime, popUpClassroom;
    private String professorName, courseName, classroom;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        usedColors = new ArrayList<>();
        courseInfo = findViewById(R.id.courseInfo);
        popUpCourseName = findViewById(R.id.popUpCourseName);
        popUpProfessor = findViewById(R.id.popUpProfessor);
        popUpTime = findViewById(R.id.popUpCourseTime);
        popUpClassroom = findViewById(R.id.popUpClassroom);

       /*
        professorName = initialize from database
        courseName = initialize from database
        classroom = initialize from database
       */
       firebaseAuth = FirebaseAuth.getInstance();
       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   Course_Info course_info = dataSnapshot.getValue(Course_Info.class);
                   String start_time = course_info.getStart_time();
                   String end_time = course_info.getEnd_time();
                   char [] s_time  = start_time.toCharArray();



                   Pair<Integer, Integer> pair1 = pairFind(start_time);
                   Pair<Integer, Integer> pair2 = pairFind(end_time);
                   //Log.d( TAG, " this is " + pair1.first + " : " + pair1.second);
                   addToTimeTable((int)pair1.first, (int)pair1.second, (int)pair2.first, (int)pair2.second, course_info.getCourse_day().toString(),
                           course_info.getProfessor_name().toString(), course_info.getCourse_name().toString(), course_info.getClassroom_number().toString());

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

/*
        addToTimeTable(13, 30,17,30, "Thursday");
        addToTimeTable(9, 30,12,30, "Thursday");
        addToTimeTable(9, 0,12,0, "Monday");
*/

    }
    public static Pair<Integer, Integer> pairFind (String start_time) {

           char [] s_time  = start_time.toCharArray();
        int int_start_hour=0, int_start_min;
        boolean notdone = false;
        start_time = "";
        for(char x : s_time) {
            if(x == ':') {
                notdone = true;
                int_start_hour =  Integer.parseInt(start_time);
                start_time = "";
                continue;
            }
            if(!notdone) {
                start_time += x;
            }

            if(notdone) {
                start_time += x;
            }
        }

        int_start_min = Integer.parseInt(start_time);
        Pair<Integer, Integer> pair = new Pair<>(int_start_hour, int_start_min);
        return pair;
    }
    void addToTimeTable(final int startTimeHour, final int startTimeMin, final int endTimeHour, final int endTimeMin, final String weekday,
                        final String professorName, final String courseName, final String classroom){
        Log.d(TAG, "printing" + startTimeHour + " : " + startTimeMin + " and " + endTimeHour + " : " + endTimeMin);
        ArrayList<String> colorsList = new ArrayList<String>();
        colorsList.add("#ADD8E6");//Light Blue
        colorsList.add("#F08080");//Light Coral
        colorsList.add("#FFB6C1");//Light Pink
        colorsList.add("#20B2AA");//Light Sea Green
        colorsList.add("#87CEFA");//Light Sky Blue
        colorsList.add("#FFA500");//Orange
        colorsList.add("#DA70D6");//Orchid
        colorsList.add("#87CEEB");//Sky Blue
        colorsList.add("#6A5ACD");//Slate Blue
        colorsList.add("#40E0D0");//Turquoise
        colorsList.add("#EE82EE");//Violet
        colorsList.add("#F0E68C");//Khaki
        colorsList.add("#BEBEBE");//Gray
        colorsList.add("#5F9EA0");//Cadet Blue
        colorsList.add("#00FFFF");//Cyan
        colorsList.add("#00FFFF");//purple
        colorsList.add("#66CDAA");//Medium Aquamarine
        colorsList.add("#D8BFD8");//Thistle
        colorsList.add("#FF6347");//Tomato
        colorsList.add("#7FFFD4");//Aqua
        colorsList.add("#D2691E");//Chocolate
        colorsList.add("#6495ED");//Cornflower Blue
        colorsList.add("#FF00FF");//Fuchsia
        colorsList.add("#d700ff");//Magenta2
        colorsList.add("#972b91");//LightMadeByMe

        Random rand = new Random();
        int randomNum = rand.nextInt((10) + 1);

        Resources r = getResources();
        LinearLayout v = new LinearLayout(schedule.this);
        v.setOrientation(LinearLayout.VERTICAL);

        v.setGravity(Gravity.CENTER);

        TextView cN = new TextView(getApplicationContext());
        TextView cR = new TextView(getApplicationContext());

        cN.setText(courseName);
        cN.setTextColor(Color.WHITE);
        cN.setTypeface(cN.getTypeface(), Typeface.BOLD);

        cR.setText(classroom);
        cR.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER;

        //params.setMarginStart(20);
        cR.setLayoutParams(params);
        cN.setLayoutParams(params);
        v.addView(cN);
        v.addView(cR);

        String color = colorsList.get(randomNum);
        while(usedColors.contains(color)){
            randomNum = rand.nextInt((10) + 1);
            color = colorsList.get(randomNum);
        }

        v.setBackgroundColor(Color.parseColor(color));
        usedColors.add(color);

        //start time should be in 24 hour (13, 14, 15...)
        int marginTop = (startTimeHour - 8) * 60 + startTimeMin;
        int heightValue = (endTimeHour - startTimeHour) *60 + (endTimeMin-startTimeMin);
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightValue, r.getDisplayMetrics());
        switch(weekday){
            case "Monday":
                myLayout = findViewById(R.id.mondayRelativeLayout);
                break;
            case "Tuesday":
                myLayout = findViewById(R.id.tuesdayRelativeLayout);
                break;
            case "Wednesday":
                myLayout = findViewById(R.id.wednesdayRelativeLayout);
                break;
            case "Thursday":
                myLayout = findViewById(R.id.thursdayRelativeLayout);
                break;
            case "Friday":
                myLayout = findViewById(R.id.fridayRelativeLayout);
                break;
        }

        v.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int)height));
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginTop,
                r.getDisplayMetrics()
        );
        p.setMargins(0, px,0 , 0);
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                courseInfo.setVisibility(View.VISIBLE);
                String s ="";
                if(startTimeMin == 0){
                    s = "Time: " + startTimeHour + ":00";
                }else{
                    s = "Time: " + startTimeHour + ":" + startTimeMin;
                }
                if(endTimeMin == 0) {
                    s = s + " - " + endTimeHour + ":00";
                }else{
                    s = s + " - "+ endTimeHour + ":" + endTimeMin;
                }
                popUpTime.setText(s);

//                need to be initialized from database
                popUpClassroom.setText("Classroom: " +classroom);
                popUpCourseName.setText("Course name: " +courseName);
                popUpProfessor.setText("Professor: " + professorName);

            }
        });
        myLayout.addView(v);

    }

    public void backToMenu(View view) {
        Intent i = new Intent(getApplicationContext(), menu.class);
        startActivity(i);
        finish();
    }


    public void onOkClicked(View view) {
        courseInfo.setVisibility(View.INVISIBLE);

    }
}

