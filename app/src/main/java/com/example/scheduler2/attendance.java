package com.example.scheduler2;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class attendance extends AppCompatActivity {
        private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
        private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
        private static final int ALPHA_ANIMATIONS_DURATION              = 200;
        private RecyclerView recView;
        private AdapterData adapter;
        private ArrayList<AtttendanceInfo> names;
        private PieChartView pieChartView;
        private AppBarLayout attendance_appbar;
        private Toolbar attendance_toolbar;
        private TextView attendance_bottom_title, attendance_bottom_goback;
        private boolean mIsTheTitleContainerVisible = true;
        private boolean mIsTheTitleVisible          = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_attendance);
            // top head bar
            attendance_appbar = findViewById(R.id.appbar_attendance);
            attendance_toolbar = findViewById(R.id.toolbar_attendance);
            setSupportActionBar(attendance_toolbar);
            attendance_bottom_title = findViewById(R.id.attendance_bottom_title);
            attendance_bottom_goback = findViewById(R.id.attendance_bottom_goback);
          /*  AppbarChangeListener appbarChangeListener = new AppbarChangeListener();
            attendance_appbar.addOnOffsetChangedListener(appbarChangeListener);*/
            names = new ArrayList<>();
            names.add(new AtttendanceInfo("Math", "Tue", "303", "Hwang", "78 %", "13 %", "8 %"));
            names.add(new AtttendanceInfo("Java", "Fri", "403", "Hwang", "78 %", "13 %", "8 %"));
            names.add(new AtttendanceInfo("C++", "Wed", "303", "Hwang", "78 %", "13 %", "8 %"));
            names.add(new AtttendanceInfo("Discrete Mathematics", "Mon", "303", "Hwang", "78 %", "13 %", "8 %"));

            pieChartView = findViewById(R.id.chart);
            List pieData = new ArrayList<>();
            pieData.add(new SliceValue(55, Color.BLUE));
            pieData.add(new SliceValue(15, Color.RED));
            pieData.add(new SliceValue(10, Color.parseColor("#FF8C00")));

            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);
            //pieChartData.setHasCenterCircle(true).setCenterText1(" ").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
            pieChartView.setPieChartData(pieChartData);

            recView = (RecyclerView)findViewById(R.id.list);
            adapter = new AdapterData(this, names);
            recView.setAdapter(adapter);
            recView.setLayoutManager(new LinearLayoutManager(this));
            recView.setHasFixedSize(true);


        }

    public void onBack2ButtonClicked(View view) {
            Intent intent = new Intent(getApplicationContext(), menu.class);
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
}