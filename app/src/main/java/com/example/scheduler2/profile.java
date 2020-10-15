package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private FloatingActionButton floatingActionButton;
    private TextView text_title, go_back_button;
    private AppBarLayout barLayout;
    private boolean fabVisible = true;
    private int barOffset;
    protected boolean appBarLocked;

    private boolean mIsTheTitleContainerVisible = true;
    private boolean mIsTheTitleVisible          = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        floatingActionButton = findViewById(R.id.add);
        barLayout = findViewById(R.id.app_bar_layout);
        barLayout.addOnOffsetChangedListener(this);
        go_back_button = findViewById(R.id.go_back);
        TextView phoneInput = findViewById(R.id.phone);
        TextView nicknameInput = findViewById(R.id.nickname);
        text_title = findViewById(R.id.title);

        startAlphaAnimation(text_title, go_back_button,0, View.INVISIBLE);

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



    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(text_title, go_back_button, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(text_title, go_back_button, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int dy = barOffset - verticalOffset;
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float)Math.abs(verticalOffset) / (float)maxScroll;
        barOffset = verticalOffset;
        if (dy > 0 && fabVisible) {
            // scrolling up
            toggleButtons(false);
        } else if (dy < 0 && !fabVisible) {




            // scrolling down
            toggleButtons(true);

        }


        floatingActionButton.setClickable(true);
        handleToolbarTitleVisibility(percentage);
    }




    private void toggleButtons(boolean visible) {
        if (visible) {
            floatingActionButton.show();
            fabVisible = true;

            text_title.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton.hide();

            text_title.setVisibility(View.VISIBLE);
            fabVisible = false;
        }

        floatingActionButton.setClickable(visible);
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }


}