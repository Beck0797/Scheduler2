package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class assignment extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new adapter_data(getApplicationContext(), "Assingments"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // specify an adapter (see also next example)

    }

    public void onAddButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), addAssignment.class);
        startActivity(intent);
    }
}
// use a linear layout manager

