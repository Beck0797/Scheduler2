package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class assignment extends AppCompatActivity {
    private static final String TAG = "assignment";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReference1;
    private HashMap<String, String> list_class;
    private ArrayList<AssignmentCons> assignmentFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentFetch = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");

        // fetch data
        assignmentFetch = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        DataSnapshot getdata = dataSnapshot.child("assignments");
                       for(DataSnapshot innerdata : getdata.getChildren()) {
                           Log.d(TAG, "print: " + innerdata);

                           AssignmentCons assignmentCons = innerdata.getValue(AssignmentCons.class);

                           assignmentFetch.add(new AssignmentCons(innerdata.getKey().toString(), assignmentCons.getAssign_coursename(),assignmentCons.getAssign_title().toString(),
                                   assignmentCons.getAssign_duedate().toString(), assignmentCons.getAssign_duetime().toString()));


                       }

                   }
                   adapter_data data = new adapter_data(getApplicationContext(), assignmentFetch, databaseReference);
                   recyclerView.setAdapter(data);
                   data.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list_class = new HashMap<>();
        fillList(databaseReference);





        // firesbase setup

        // specify an adapter (see also next example)

    }

    private void fillList(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    Course_Info courseInfo = data.getValue(Course_Info.class);
                    list_class.put(courseInfo.getCourse_name().toString(), data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onAddButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), addAssignment.class);
        intent.putExtra("arraylist", list_class);
        startActivity(intent);
        finish();
    }

    public void goBacktoMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), menu.class);
        startActivity(intent);
        finish();
    }
}
// use a linear layout manager

