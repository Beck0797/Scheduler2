package com.scheduler.beck.Alarm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scheduler.beck.R;

public class NotificationMessage extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
//        textView = findViewById(R.id.tv_message);
//        Bundle bundle = getIntent().getExtras();
//        textView.setText(bundle.getString("message"));

    }
}
