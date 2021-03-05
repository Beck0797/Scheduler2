package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scheduler.beck.Fragments.tasks;
import com.scheduler.beck.Utils.ThemeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterClassActivity extends AppCompatActivity{
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

        Intent intent = new Intent();
        try {
            intent = getIntent();
            myMap =(HashMap<String, ArrayList<List<Double>>>) getIntent().getSerializableExtra("hash_map");

        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        setFragment(intent);


    }
    private void setFragment(Intent i) {

        Fragment fragment = new tasks(1, i);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.lytFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void init(){
        isLastFrame = true;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");

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