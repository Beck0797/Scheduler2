package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scheduler.beck.Models.check_attendance_cons;
import com.scheduler.beck.Utils.ThemeUtils;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "menu";
    private CardView register_window, schedule_window, assignment_window, attendance_window, cardViewExit, web_page;
    private ImageView image_profile;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReference1;
    public ArrayList<check_attendance_cons> check_attendance_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_menu);

        check_attendance_list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("web_link");
        Intent intent = new Intent(this, SignIn.class);

        // register_window button
        register_window = findViewById(R.id.register_card);
        register_window.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), courseList.class);
                startActivity(intent1);
            }
        });
        schedule_window = findViewById(R.id.schedule_card);
        schedule_window.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), TimetableActivity.class);
                startActivity(intent1);
            }
        });

        // asssignemnt window
        assignment_window = findViewById(R.id.assignment_card);
        assignment_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssignmentActivity.class);
                startActivity(intent);
            }
        });

        image_profile = findViewById(R.id.profile_icon);
        image_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        // attendance read database
        databaseReference1 = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        check_attendance_list = new ArrayList<check_attendance_cons>();
        databaseReference1.child("attendance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        check_attendance_cons check = dataSnapshot.getValue(check_attendance_cons.class);
                        check_attendance_list.add(new check_attendance_cons(check.getClass_name().toString(), check.getStatus().toString(), check.getDate().toString()));
                    }catch (NullPointerException e){
                        e.printStackTrace();
//                        check_attendance_list = new ArrayList<>();
                    }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        attendance_window = findViewById(R.id.attendance_window);
        attendance_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                //Log.d(TAG, "sizeL: " + check_attendance_list.size());
                Bundle bundle = new Bundle();
                if(!check_attendance_list.isEmpty()){

                    bundle.putSerializable("attendance_info", check_attendance_list);
                    bundle.putInt("isThereList", 1);

                }else{
                    bundle.putInt("isThereList", 0);
                }
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });


        // exitbutton and webpage
        web_page = findViewById(R.id.webPageCard);
        web_page.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MenuActivity.this, "Long press to change the link", Toast.LENGTH_SHORT).show();

                // check if user webPage attribute is empty. If it is, then register
                Intent intent = new Intent(getApplicationContext(), WebpageActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        });
        web_page.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Long press to change the link", Toast.LENGTH_SHORT).show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            String link;
                            link = snapshot.getValue().toString();

                            Log.d(TAG, "link" + link);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);

                        } catch (NullPointerException e) {
                            Toast.makeText(MenuActivity.this, "Please save your school webpage link at first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), WebpageActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MenuActivity.this, "Please insert the link at first", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        cardViewExit = findViewById(R.id.cardViewExit);
        cardViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
                System.exit(0);
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(MenuActivity.this, SignIn.class);
            startActivity(intent);
            finish();
        }

    }

    public void onNotificationClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), notification.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleBackToExitPressedOnce) {
                finish();
                finishAffinity();
                System.exit(0);
                return true;
            }else{
                Toast.makeText(this, "Press back one more time to exit the program", Toast.LENGTH_SHORT).show();
                doubleBackToExitPressedOnce = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }

        }
        return false;
    }

}