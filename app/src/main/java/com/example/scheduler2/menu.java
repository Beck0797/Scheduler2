package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class menu extends AppCompatActivity {

    private CardView register_window, schedule_window, assignment_window, attendance_window, cardViewExit, web_page;
    private ImageView image_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



       Intent intent = new Intent(this, signIn.class);

       // register_window button
        register_window = findViewById(R.id.register_card);
        register_window.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), courseList.class);
                startActivity(intent1);
            }
        });
        schedule_window = findViewById(R.id.schedule_card);
        schedule_window.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), schedule.class);
                startActivity(intent1);
            }
        });

        // asssignemnt window
        assignment_window = findViewById(R.id.assignment_card);
        assignment_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), assignment.class);
                startActivity(intent);
            }
        });

        image_profile = findViewById(R.id.profile_icon);
        image_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
            }
        });
        attendance_window = findViewById(R.id.attendance_window);
        attendance_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), attendance.class);
                startActivity(intent);
            }
        });


        // exitbutton and webpage
        web_page = findViewById(R.id.webPageCard);
        web_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(menu.this, "Long press to change the link", Toast.LENGTH_SHORT).show();

                // check if user webPage attribute is empty. If it is, then register
                Intent intent = new Intent(getApplicationContext(), webpage.class);
                startActivity(intent);
            }
        });
        web_page.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), webpage.class);
                startActivity(intent);
                return true;
            }
        });

        cardViewExit = findViewById(R.id.cardViewExit);
        cardViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
                System.exit(0);
            }
        });
    }


    public void onNotificationClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), notification.class);
        startActivity(intent);
    }


}