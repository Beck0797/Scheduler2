package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class courseList extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        recyclerView = (RecyclerView) findViewById(R.id.courseRecycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new courses_adapter_data(getApplicationContext(), "Assingments"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onCourseAddButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), register_class.class);
        startActivity(intent);
    }

    public void onBack2ButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), menu.class);
        startActivity(intent);
        finish();
    }
}