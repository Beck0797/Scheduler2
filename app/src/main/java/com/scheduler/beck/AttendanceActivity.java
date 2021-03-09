package com.scheduler.beck;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scheduler.beck.Adapters.AdapterData;
import com.scheduler.beck.Models.AtttendanceInfo;
import com.scheduler.beck.Models.Course_Info;
import com.scheduler.beck.Models.check_attendance_cons;
import com.scheduler.beck.Utils.ThemeUtils;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class AttendanceActivity extends AppCompatActivity {
    private static final String TAG = "attendance";
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private RecyclerView recView;
    private AdapterData adapter;
    private ArrayList<AtttendanceInfo> names;
    private PieChartView pieChartView;
    private AppBarLayout attendance_appbar;
    private Toolbar attendance_toolbar;
    private TextView attendance_bottom_title, attendance_bottom_goback;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean mIsTheTitleVisible = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<check_attendance_cons> check_list;
    private float total = 0;
    private TextView attendace_show, absence_show, tardiness;
    private HashMap<String, Point> att_calc;
    private float total_att = 0, total_abs = 0, total_tar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_attendance);
        // database initialize
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        // top head bar
        attendance_appbar = findViewById(R.id.appbar_attendance);
        attendance_toolbar = findViewById(R.id.toolbar_attendance);
        setSupportActionBar(attendance_toolbar);
        attendance_bottom_title = findViewById(R.id.attendance_bottom_title);
        attendance_bottom_goback = findViewById(R.id.attendance_bottom_goback);
          /*  AppbarChangeListener appbarChangeListener = new AppbarChangeListener();
            attendance_appbar.addOnOffsetChangedListener(appbarChangeListener);*/
        attendace_show = findViewById(R.id.attendance_show);
        absence_show = findViewById(R.id.absence_show);
        tardiness = findViewById(R.id.tardiness_show);
        names = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        int isThereList = bundle.getInt("isThereList");
        if(isThereList == 1) {
            check_list = (ArrayList<check_attendance_cons>) bundle.getSerializable("attendance_info");
        }else{
            check_list = new ArrayList<>();
        }


        Log.d(TAG, "size arraylist: " + check_list.size());
        if (!check_list.isEmpty()) {
            att_calc = new HashMap<String, Point>();
            for (int i = 0; i < check_list.size(); i++) {
                total += 1;
                int at = 0;
                int ab = 0;
                int tar = 0;
                Log.d(TAG, "class status" + check_list.get(i).getStatus().toString());
                if (check_list.get(i).getStatus().toString().equals("attendance")) {
                    at = 1;
                    total_att += 1;
                } else if (check_list.get(i).getStatus().toString().equals("absence")) {
                    ab = 1;
                    total_abs += 1;
                } else {
                    tar = 1;
                    total_tar += 1;
                }
                Log.d(TAG, "chek" + ab);
                try {
                    Point actual_point = new Point();
                    if (!att_calc.isEmpty() && att_calc.containsKey(check_list.get(i).getClass_name())) {
                        Point points = att_calc.get(check_list.get(i).getClass_name());
                        actual_point.setAtt(points.getAtt() + at);
                        actual_point.setAbs(points.getAbs() + ab);
                        actual_point.setTard(points.getTard() + tar);

                    } else {
                        actual_point.setAtt(at);
                        actual_point.setAbs(ab);
                        actual_point.setTard(tar);
                    }
                    Log.d(TAG, "printingg: " + check_list.get(i).getClass_name());
                    att_calc.put(check_list.get(i).getClass_name(), actual_point);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        databaseReference.child("all_class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        Course_Info course_info = dataSnapshot.getValue(Course_Info.class);
                        boolean check = false;
                        if (att_calc.containsKey(course_info.getCourse_name().toString())) {
                            Log.d(TAG, "print true: " + course_info.getCourse_name().toString());
                        } else {
                            Log.d(TAG, "print false: " + course_info.getCourse_name().toString());
                        }
                        if (att_calc.containsKey(course_info.getCourse_name().toString())) {
                            check = true;
                            Point p = att_calc.get(course_info.getCourse_name().toString());
                            float all = p.getAbs()+p.getAtt()+p.getTard();
                            String att_s = String.format("%.1f", p.getAtt() == 0 ? 0 : p.getAtt() / all * 100);
                            //Log.d(TAG, "another" + p.getAbs());
                            String abs_s = String.format("%.1f", p.getAbs() == 0 ? 0 : p.getAbs() / all * 100);
                            String tar_d = String.format("%.1f", p.getTard() == 0 ? 0 : p.getTard() / all * 100);
                            names.add(new AtttendanceInfo(course_info.getCourse_name().toString(), course_info.getCourse_day().toString()
                                    , course_info.getClassroom_number().toString(), course_info.getProfessor_name().toString(),
                                    att_s + "%", abs_s + "%", tar_d + "%"));
                        }

                        if (check == false) {
                            names.add(new AtttendanceInfo(course_info.getCourse_name().toString(), course_info.getCourse_day().toString()
                                    , course_info.getClassroom_number().toString(), course_info.getProfessor_name().toString(),
                                    "0%", "0%", "0%"));
                        }


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pieChartView = findViewById(R.id.chart);
        List pieData = new ArrayList<>();
        try {
            pieData.add(new SliceValue(total_att == 0 ? 0 : (total_att / total) * 100, Color.BLUE));
            pieData.add(new SliceValue(total_abs == 0 ? 0 : (total_abs / total) * 100, Color.RED));
            pieData.add(new SliceValue(total_tar == 0 ? 0 : (total_tar / total) * 100, Color.parseColor("#FF8C00")));
            attendace_show.setText("attendance" + String.format(" %.1f", total_att == 0 ? 0 : (total_att / total) * 100) + " %");
            absence_show.setText("absence" + String.format(" %.1f", total_abs == 0 ? 0 : (total_abs / total) * 100) + " %");
            tardiness.setText("tardiness" + String.format(" %.1f", total_tar == 0 ? 0 : (total_tar / total) * 100) + " %");
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        //pieChartData.setHasCenterCircle(true).setCenterText1(" ").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
        pieChartView.setPieChartData(pieChartData);

        recView = (RecyclerView) findViewById(R.id.list);
        adapter = new AdapterData(this, names);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setHasFixedSize(true);


    }

    public void onBack2ButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }


   /*  public class AppbarChangeListener implements AppBarLayout.OnOffsetChangedListener {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            int maxScroll = appBarLayout.getTotalScrollRange();
            float percentage = (float)Math.abs(verticalOffset) / (float)maxScroll;
            handleToolbarTitleVisibility(percentage);
        }
        public void handleToolbarTitleVisibility(float percentage) {
            if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if(!mIsTheTitleVisible) {
                    startAlphaAnimation(attendance_bottom_title, attendance_bottom_goback, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                    mIsTheTitleVisible = true;
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(attendance_bottom_title, attendance_bottom_goback, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                    mIsTheTitleVisible = false;
                }
            }
        }
        private void startAlphaAnimation(TextView text_title, TextView go_back_button, long duration, int visibility) {
            AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                    ? new AlphaAnimation(0f, 1f)
                    : new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(duration);
            alphaAnimation.setFillAfter(true);
            text_title.startAnimation(alphaAnimation);
            go_back_button.startAnimation(alphaAnimation);
        }
    }*/

    class Point {
        public Point() {
        }

        public Point(int att, int abs, int tard) {
            this.att = att;
            this.abs = abs;
            this.tard = tard;
        }

        public int getAtt() {
            return att;
        }

        public void setAtt(int att) {
            this.att = att;
        }

        public int getAbs() {
            return abs;
        }

        public void setAbs(int abs) {
            this.abs = abs;
        }

        public int getTard() {
            return tard;
        }

        public void setTard(int tard) {
            this.tard = tard;
        }

        int att;
        int abs;
        int tard;
    }
}