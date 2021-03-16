package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
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

import static com.scheduler.beck.Fragments.tasks.alarmStartMap;
import static com.scheduler.beck.RegisterAssignmentActivity.assignAlarmMap;

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
    private TextView noAssign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_assignment);

        noAssign = findViewById(R.id.txtNoAssign);
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot getdata = dataSnapshot.child("assignments");
                    for (DataSnapshot innerdata : getdata.getChildren()) {
                        Log.d(TAG, "print: " + innerdata);

                        AssignmentCons assignmentCons = innerdata.getValue(AssignmentCons.class);

                        assignmentFetch.add(new AssignmentCons(innerdata.getKey(), assignmentCons.getAssign_coursename(), assignmentCons.getAssign_title(),
                                assignmentCons.getAssign_duedate(), assignmentCons.getAssign_duetime()));


                    }

                }
                data = new adapter_data(getApplicationContext(), assignmentFetch, databaseReference);
                recyclerView.setAdapter(data);
                data.notifyDataSetChanged();
                if (data.getItemCount() == 0) {
                    findViewById(R.id.txtNoAssign).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.txtNoAssign).setVisibility(View.GONE);

                }

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
                for (DataSnapshot data : snapshot.getChildren()) {
                    Course_Info courseInfo = data.getValue(Course_Info.class);
                    list_class.put(courseInfo.getCourse_name(), data.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onAddButtonClicked(View view) {
        if(list_class.isEmpty()){
            Toast.makeText(this, "There is no class to add assignment. Register class first!", Toast.LENGTH_SHORT).show();
            return;
        }
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

    public void myCustomSnackbar(final RecyclerView.ViewHolder viewHolder) {
        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0, 0, 0, 0);
        // Hide the text
//        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot getdata = dataSnapshot.child("assignments");
                    getdata.getRef().child(key).removeValue();
                }
            }
        });

        cancelAlarm(key);
    }

    private void cancelAlarm(String key) {
        try {
            PendingIntent pendingIntent = assignAlarmMap.get(key);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if(pendingIntent != null){
                alarmManager.cancel(pendingIntent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
            return true;

        }
        return super.onKeyDown( keyCode, event );
    }
}


