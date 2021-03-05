package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scheduler.beck.Models.Course_Info;
import com.scheduler.beck.Models.Course_display;
import com.scheduler.beck.Utils.ThemeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class courseList extends AppCompatActivity implements courses_adapter_data.OnItemClickListener {
    public static final String TAG = "courseList";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReference1;
    ArrayList<Course_display> course_displays;
    private courses_adapter_data adapter_data;
    private Map<String, ArrayList<List<Double>>> myMap;
    private ArrayList<List<Double>> list_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_course_list);

        recyclerView = (RecyclerView) findViewById(R.id.courseRecycler_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");

        databaseReference1 = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class").child("assignments");

        course_displays = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Iterable<DataSnapshot> dataSnapshots = snapshot.getChildren()
                //;
                course_displays.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    try {
                        Course_Info course_info = data.getValue(Course_Info.class);

                        assert course_info != null;
                        String fetch_course_name = course_info.getCourse_name().toString();
                        String fetch_course_day = course_info.getCourse_day().toString();
                        String fetch_course_classroom = course_info.getClassroom_number().toString();
                        String fetch_course_professor = course_info.getProfessor_name().toString();
                        Log.d(TAG, "class name: " + fetch_course_name);
                        Log.d(TAG, "class name: " + fetch_course_day);
                        Log.d(TAG, "class name: " + fetch_course_classroom);
                        Log.d(TAG, "class name: " + fetch_course_professor);
                        Course_display course = new Course_display(data.getKey(), fetch_course_name, fetch_course_day, fetch_course_classroom, fetch_course_professor,
                                course_info.getStart_time().toString(), course_info.getEnd_time().toString(), course_info.getAlarm_time(), course_info.getUrl_name().toString());

                        //Log.d(TAG, "internal: " + course.getCourse_display_name());
                        course_displays.add(course);
                        // Log.d(TAG, "output size:  " + course_displays.size());
                    }catch(NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "no class exist yet!", Toast.LENGTH_SHORT).show();
                    }
                }

                //course_displays.add(new Course_display("C lang", "Tuesday", "304", "Kim"));
                adapter_data = new courses_adapter_data(getApplicationContext(), course_displays);
                recyclerView.setAdapter(adapter_data);
                adapter_data.setOnItemClickListener(courseList.this);
                adapter_data.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // class over data filled
        myMap = new HashMap<>();
        list_double = new ArrayList<>();
        databaseReference1 = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    try {
                        Course_Info courseinfo = data.getValue(Course_Info.class);
                        String start_time = courseinfo.getStart_time();
                        String end_time = courseinfo.getEnd_time();
                        //Log.d(TAG, "class checking" + start_time);
                        final Double s_time = makeDouble(start_time);
                        final Double e_time = makeDouble(end_time);
                        //Log.d(TAG, "time checking" + s_time);
                        //Log.d(TAG, "time checking" + e_time);
                        ArrayList<
                                List<Double>> monClasses = new ArrayList<List<Double>>() {{
                            add(Arrays.<Double>asList(s_time, e_time));
                        }};
                        myMap.put(courseinfo.getCourse_day().toString(), monClasses);
                    }catch(DatabaseException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public Double makeDouble(String time) {
         //Log.d(TAG, "check extra: " + time);
        boolean half_done = false;
        String hour="";
        String minute="";
        for(int i = 0; i < time.length(); i++) {
            if(time.charAt(i) == ':') {
                half_done = true;
                continue;
            }
            if(half_done == false) {
                hour = hour + time.charAt(i);
            }
            if(half_done) {
                minute += time.charAt(i);
            }
        }
        int t_s = Integer.parseInt(hour);
        int t_e = Integer.parseInt(minute);
        Double ans = Double.parseDouble(t_s + "." + t_e);
        return ans;
    }


    public void onCourseAddButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterClassActivity.class);
        intent.putExtra("hash_map", (Serializable) myMap);
        startActivity(intent);
    }

    public void onBack2ButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Press and hold for options menu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Course_display selected_item = course_displays.get(position);
        Log.d(TAG, "this is course " + selected_item.getCourse_display_name());
        Intent intent = new Intent(this, RegisterClassActivity.class);
        intent.putExtra("class_key", selected_item.getIdkey());
        intent.putExtra("class_name", selected_item.getCourse_display_name());
        intent.putExtra("class_day", selected_item.getCourse_day_name());
        intent.putExtra("class_room", selected_item.getCourse_display_classroom());
        intent.putExtra("class_professor", selected_item.getCourse_display_professor_name());
        intent.putExtra("class_start_time", selected_item.getStart_display_time());
        intent.putExtra("class_end_time", selected_item.getEnd_display_time());
        intent.putExtra("class_alarm_time", selected_item.getAlarm_display_time());
        intent.putExtra("class_url_link", selected_item.getUrl_display_name());

        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        Course_display selected_item = course_displays.get(position);
        String selected_key = selected_item.getIdkey();
        databaseReference.child(selected_key).removeValue();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack2ButtonClicked(null);
            return true;

        }
        return super.onKeyDown( keyCode, event );
    }
}