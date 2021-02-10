package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, profile_setFragment.BottomSheetListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    private FloatingActionButton floatingActionButton;
    private TextView text_title, go_back_button, go_back_button2, profileName, studentNumber;
    private AppBarLayout barLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private boolean fabVisible = true;
    private int barOffset;
    protected boolean appBarLocked;
    private CircleImageView circleImageView;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean mIsTheTitleVisible          = false;
    private LinearLayout setting_profile_name, setting_student_number;
    private String imgName;
    SharedPreferences sharedPref;
    String imgUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        floatingActionButton = findViewById(R.id.add);
        barLayout = findViewById(R.id.app_bar_layout);
        barLayout.addOnOffsetChangedListener(this);
        go_back_button = findViewById(R.id.go_back);
        go_back_button2 = findViewById(R.id.go_back2);
        ButtonClick buttonClick = new ButtonClick();
        go_back_button.setOnClickListener(buttonClick);
        go_back_button2.setOnClickListener(buttonClick);
        text_title = findViewById(R.id.toolbar_profile_name);
        circleImageView = findViewById(R.id.ava);
        studentNumber = findViewById(R.id.phone);
        profileName = findViewById(R.id.profile_name);
        setting_profile_name = findViewById(R.id.setting);
        setting_student_number = findViewById(R.id.student_number_click);
        setting_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_setFragment profile_setFragment= new profile_setFragment();
                profile_setFragment.show(getSupportFragmentManager(), "test");

            }
        });
        setting_student_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_stnumberFragment profile_stnumberFragment = new profile_stnumberFragment();
                profile_stnumberFragment.show(getSupportFragmentManager(), "test1");
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        storage = FirebaseStorage.getInstance();

        imgName =""+ firebaseAuth.getCurrentUser().getUid();
        sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        imgUrl = sharedPref.getString(imgName, "no");

        if(!imgUrl.equals("no"))
        {
            Log.d("Url", imgUrl);
            circleImageView.setImageURI(Uri.parse(imgUrl));
        }

        displayProfileName(); // display profile name
        // set profile photo button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    @Override
    public void onButtonClicked(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(profile.this, signIn.class);
            startActivity(intent);
            finish();
        }

    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }

    }
    // set image
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri originalUri = null;
            if (Build.VERSION.SDK_INT < 19) {
                originalUri = data.getData();
            } else {
                originalUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    this.getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            imgUrl = originalUri.toString();
            editor.putString(imgName, imgUrl);
            editor.apply();
            circleImageView.setImageURI(originalUri);
        }
    }

       // display profile name
    private void displayProfileName() {

            myRef.child("user_name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Upload upload = snapshot.getValue(Upload.class);


                        try {


                            profileName.setText(upload.getName().toString());
                            text_title.setText(upload.getName().toString());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            myRef.child("student_number").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            st_num st = ds.getValue(st_num.class);
                            studentNumber.setText(st.getStudent_number().toString());
                        }
                    }catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startAlphaAnimation(text_title, go_back_button,0, View.INVISIBLE);

        }

    private void startAlphaAnimation(TextView text_title, TextView go_back_button, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        text_title.startAnimation(alphaAnimation);
        go_back_button.startAnimation(alphaAnimation);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(text_title, go_back_button, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(text_title, go_back_button, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int dy = barOffset - verticalOffset;
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float)Math.abs(verticalOffset) / (float)maxScroll;
        barOffset = verticalOffset;
        if (dy > 0 && fabVisible) {

            // scrolling up
            toggleButtons(false);
        } else if (dy < 0 && !fabVisible) {
            // scrolling down
            toggleButtons(true);
        }


        floatingActionButton.setClickable(true);
        handleToolbarTitleVisibility(percentage);
    }


    private void toggleButtons(boolean visible) {
        if (visible) {
            floatingActionButton.show();
            fabVisible = true;

            text_title.setVisibility(View.INVISIBLE);
        } else {
            floatingActionButton.hide();

            text_title.setVisibility(View.VISIBLE);
            fabVisible = false;
        }

        floatingActionButton.setClickable(visible);
    }

    public void logout_button(View view) {
        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("isSignedIn", "no");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(profile.this, signIn.class);
        startActivity(intent);
    }

    class ButtonClick implements  View.OnClickListener {

          @Override
          public void onClick(View v) {
               switch (v.getId()) {
                   case R.id.go_back:
                       Intent intent = new Intent(profile.this, menu.class );
                       startActivity(intent);
                       break;
                   case R.id.go_back2:
                       Intent intent1 = new Intent(profile.this, menu.class);
                       startActivity(intent1);
                       break;
                   default:
                       System.out.println("Button clicked, but can't start activity");
               }
          }
      }


}