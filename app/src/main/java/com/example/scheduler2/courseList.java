package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class courseList extends AppCompatActivity {
    public static final String TAG = "courseList";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ArrayList<Course_display> course_displays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        recyclerView = (RecyclerView) findViewById(R.id.courseRecycler_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");
        course_displays = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Iterable<DataSnapshot> dataSnapshots = snapshot.getChildren();
                for(DataSnapshot data: snapshot.getChildren()) {
                    Course_Info course_info = data.getValue(Course_Info.class);

                    String fetch_course_name = course_info.getCourse_name().toString();
                    String fetch_course_day = course_info.getCourse_day().toString();
                    String fetch_course_classroom = course_info.getClassroom_number().toString();
                    String fetch_course_professor = course_info.getProfessor_name().toString();
                    //Log.d(TAG, "class name: " + fetch_course_name);
                    //Log.d(TAG, "class name: " + fetch_course_day);
                    //Log.d(TAG, "class name: " + fetch_course_classroom);
                    //Log.d(TAG, "class name: " + fetch_course_professor);
                    Course_display course = new Course_display(fetch_course_name, fetch_course_day, fetch_course_classroom, fetch_course_professor);
                    //Log.d(TAG, "internal: " + course.getCourse_display_name());
                    course_displays.add(course);
                   // Log.d(TAG, "output size:  " + course_displays.size());
                }

                course_displays.add(new Course_display("C lang", "Tuesday", "304", "Kim"));
                courses_adapter_data adapter_data = new courses_adapter_data(getApplicationContext(), course_displays);
                recyclerView.setAdapter(adapter_data);
                adapter_data.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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