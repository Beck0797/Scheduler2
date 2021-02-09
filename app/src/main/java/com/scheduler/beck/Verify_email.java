package com.scheduler.beck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verify_email extends AppCompatActivity implements View.OnClickListener {

    private Button btnVerify, btnResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        sendVerificationEmail();

        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResend);
        
        btnResend.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnResend:
                sendVerificationEmail();
                break;
            case R.id.btnVerify:
                checkIfEmailVerified();
                break;
        }
        
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
                            Toast.makeText(Verify_email.this, "Please check your email for the verification code", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Verify_email.this, "Wrong email", Toast.LENGTH_SHORT).show();
                            //to do: make the user to register another email.

                        }
                    }
                });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, go to sign in page.
            Intent intent = new Intent(getApplicationContext(), signIn.class);
            startActivity(intent);
            finish();
            Toast.makeText(Verify_email.this, "Email is verified", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(Verify_email.this, "Email is not verified", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
}