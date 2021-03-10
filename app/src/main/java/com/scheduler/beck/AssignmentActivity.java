package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scheduler.beck.Adapters.adapter_data;
import com.scheduler.beck.Models.AssignmentCons;
import com.scheduler.beck.Models.Course_Info;
import com.scheduler.beck.Utils.ThemeUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentActivity extends AppCompatActivity {
    private static final String TAG = "assignment";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReference1;
    private HashMap<String, String> list_class;
    private ArrayList<AssignmentCons> assignmentFetch;
    private adapter_data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_assignment);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                   data = new adapter_data(getApplicationContext(), assignmentFetch, databaseReference);
                   recyclerView.setAdapter(data);
                   data.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list_class = new HashMap<>();
        fillList(databaseReference);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);





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
        Intent intent = new Intent(getApplicationContext(), RegisterAssignmentActivity.class);
        intent.putExtra("arraylist", list_class);
        startActivity(intent);
        finish();
    }

    public void goBacktoMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(getApplicationContext(), "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//            Toast.makeText(getApplicationContext(), "on Swiped ", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            myCustomSnackbar(viewHolder);

        }
    };

    public void myCustomSnackbar(final RecyclerView.ViewHolder viewHolder)
    {
        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0,0,0,0);
        // Hide the text
//        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);

        LayoutInflater mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snack_bar, null);
        // Configure the view
        TextView textViewOne = (TextView) snackView.findViewById(R.id.txtOne);
        textViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                AssignmentCons a = assignmentFetch.get(position);
                String key = a.getAssign_key();

                //remove from list
                assignmentFetch.remove(position);
                //delete from DB
                deleteAssignment(key);

                data.notifyDataSetChanged();
                
                snackbar.dismiss();


            }
        });

        TextView textViewTwo = (TextView) snackView.findViewById(R.id.txtTwo);
        textViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Two", "Second one is clicked");
                snackbar.dismiss();
            }
        });

        // Add the view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);
        // Show the Snackbar
        snackbar.show();
    }

    private void deleteAssignment(final String key) {
        databaseReference1 = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("all_class");

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    DataSnapshot getdata = dataSnapshot.child("assignments");
                    getdata.getRef().child(key).removeValue();
                }
            }
        });
    }
}


