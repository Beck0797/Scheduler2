package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signIn extends AppCompatActivity {
    private final static  String TAG = "signIn";
    private FirebaseAuth mAuth;
    private Button login_button;
    private EditText login_edit, password_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // login authenticate
        mAuth = FirebaseAuth.getInstance();
        login_button = findViewById(R.id.signin_button);
        login_edit = findViewById(R.id.signin_email);
        password_edit = findViewById(R.id.signin_password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siginFunction();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
        System.exit(0);
    }
    public void siginFunction() {
      String email = login_edit.getText().toString().trim();
      String password = password_edit.getText().toString().trim();
      if(TextUtils.isEmpty(email)) {
          Toast.makeText(signIn.this, "email is empty",
                  Toast.LENGTH_SHORT).show();
          return;
      }
      if(TextUtils.isEmpty(password)) {
          Toast.makeText(signIn.this, "password is empty.",
                  Toast.LENGTH_SHORT).show();
          return;
      }
        Log.d(TAG, "internet" + isNetworkConnected());
      if(isNetworkConnected()) {
          mAuth.signInWithEmailAndPassword(email, password)
                  .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              // Sign in success, update UI with the signed-in user's information
                              Log.d(TAG, "signInWithEmail:success");
                              FirebaseUser user = mAuth.getCurrentUser();
                              checkIfEmailVerified();



                              //updateUI(user);
                          } else {
                              // If sign in fails, display a message to the user.
                              Log.w(TAG, "signInWithEmail:failure", task.getException());
                              Toast.makeText(signIn.this, "Invalid login",
                                      Toast.LENGTH_SHORT).show();
                              // updateUI(null);
                          }

                          // ...
                      }
                  });
      } else {
          Snackbar.make(findViewById(R.id.signin_activity), "NO INTERNET CONNECTION", Snackbar.LENGTH_LONG)
                  .setAction("CLOSE", new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {

                      }
                  })
                  .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                  .show();
      }
    }
    public void to_register_activity(View view) {
        Intent intent = new Intent(getApplicationContext(), register.class);
        startActivity(intent);
        finish();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("isSignedIn", "yes");
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), menu.class);
            startActivity(intent);
            finish();
        }
        else
        {
            showDialog();

        }
    }
    private void showDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Check you email for verification");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Resend Email",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendVerificationEmail();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Change Email",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteCurrentUser();
                        Intent i = new Intent(signIn.this, register.class);
                        startActivity(i);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteCurrentUser() {
       final FirebaseUser usera = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        usera.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        usera.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            Toast.makeText(signIn.this, "Verification email is sent", Toast.LENGTH_SHORT).show();;
                        }
                        else
                        {
                            Toast.makeText(signIn.this, "Try after a while", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
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
