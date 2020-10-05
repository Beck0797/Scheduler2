package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout register_window, schedule_window, assignment_window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle.syncState();
       Intent intent = new Intent(this, signIn.class);

       // register_window button
        register_window = findViewById(R.id.register_card);
        register_window.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), register_class.class);
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

        //  getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
           // case: R.id.si
        }
        return false;
    }
}