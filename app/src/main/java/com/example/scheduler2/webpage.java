package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class webpage extends AppCompatActivity {
    private EditText editTextWebPageLink;
    private String webPageUrl;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);

        //firebase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("web_link");
        editTextWebPageLink = findViewById(R.id.editTextWebPageLink);
        webPageUrl = "";

    }

    public void backMenu(View view) {
        Intent i = new Intent(getApplicationContext(), menu.class);
        startActivity(i);
        finish();
    }

    public void onClickLinkRegister(View view) {
        webPageUrl = editTextWebPageLink.getText().toString();
        if(webPageUrl.isEmpty()){
            Toast.makeText(this, "WebPage Url can not be empty", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.setValue(webPageUrl);
        }

    }
}