package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, profile_setFragment.BottomSheetListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private FloatingActionButton floatingActionButton;
    private TextView text_title, go_back_button, go_back_button2, profileName, studentNumber;
    private AppBarLayout barLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUrl;
    private boolean fabVisible = true;
    private int barOffset;
    protected boolean appBarLocked;
    private CircleImageView circleImageView;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean mIsTheTitleVisible          = false;
    private LinearLayout setting_profile_name, setting_student_number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        storageReference = storage.getReference();
        displayProfileName(); // display profile name
        // set profile photo button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // after image uploaded, then dispay
       displayImage();
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
     private void displayImage() {
        storageReference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile_Image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
             public void onSuccess( final Uri uri) {
                // Picasso.with(getApplicationContext()).load(uri).fit().centerCrop().into(circleImageView);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .preload();
                Glide.with(getApplicationContext())
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // ALL works here too
                        .into(circleImageView);
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                 // Handle any errors
             }
         });
               /* Picasso.with(getApplicationContext())
                        .load(uri)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(circleImageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(getApplicationContext())
                                        .load(uri)
                                        .error(R.drawable.icon_person)
                                        .into(circleImageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            }
                        });*/

     }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }
    // set image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUrl = data.getData();
            circleImageView.setImageURI(imageUrl);
            uploadImage();
        }
    }
    private void uploadImage() {
        StorageReference imageReference = storageReference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile_Image");  //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imageUrl);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(), "Upload successful!", Toast.LENGTH_SHORT).show();
            }
        });

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

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    public void logout_button(View view) {
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