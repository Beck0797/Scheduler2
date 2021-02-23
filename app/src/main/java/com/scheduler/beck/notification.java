package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class notification extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_notification);

        recyclerView = (RecyclerView) findViewById(R.id.notificationRecycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new adapterNotification(getApplicationContext(), "Notification"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onBack2ButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), menu.class);
        startActivity(intent);
    }
}