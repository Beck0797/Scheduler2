package com.example.scheduler2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.w3c.dom.Text;

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
      String email = login_edit.getText().toString();
      String password = password_edit.getText().toString();
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
                              Toast.makeText(signIn.this, "Authentication success.",
                                      Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(getApplicationContext(), menu.class);
                              startActivity(intent);
                              //updateUI(user);
                          } else {
                              // If sign in fails, display a message to the user.
                              Log.w(TAG, "signInWithEmail:failure", task.getException());
                              Toast.makeText(signIn.this, "Authentication failed.",
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
}