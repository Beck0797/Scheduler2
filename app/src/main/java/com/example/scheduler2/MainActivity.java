package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    ImageView logo;
    TextView name, slogan;
    Animation topAnimation;
    Animation bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        name = findViewById(R.id.splashName);
        slogan = findViewById(R.id.splashSlogan);

        logo = findViewById(R.id.splashLogo);

        logo.setAnimation(topAnimation);

        name.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this, signIn.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3300);
    }


}