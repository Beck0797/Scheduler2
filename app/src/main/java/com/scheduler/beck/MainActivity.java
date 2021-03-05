package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;

import android.view.WindowManager;

import android.os.Handler;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        final String isSignedIn = sharedPref.getString("isSignedIn", "no");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent;
                if(isSignedIn.equals("no")){
                     mainIntent = new Intent(MainActivity.this, SignIn.class);
                }else{
                     mainIntent = new Intent(MainActivity.this, MenuActivity.class);

                }
                startActivity(mainIntent);
                finish();
            }
        }, 1000);
    }


}