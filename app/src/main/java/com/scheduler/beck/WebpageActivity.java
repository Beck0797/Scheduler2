package com.scheduler.beck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scheduler.beck.Utils.ThemeUtils;

public class WebpageActivity extends AppCompatActivity {
    private EditText editTextWebPageLink;
    private String webPageUrl;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_webpage);

        //firebase setup
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("web_link");
        editTextWebPageLink = findViewById(R.id.editTextWebPageLink);
        webPageUrl = "";

    }

    public void backMenu() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
        finish();
    }

    public void onClickLinkRegister(View view) {
        webPageUrl = editTextWebPageLink.getText().toString().trim();
        if(webPageUrl.isEmpty()){
            Toast.makeText(this, "WebPage Url can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isLink(webPageUrl)){
            Toast.makeText(this, "Link should start with \"http\"", Toast.LENGTH_SHORT).show();
            return;
        }

            databaseReference.setValue(webPageUrl);
            backMenu();


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        backMenu();
        return true;
    }
    private boolean isLink(String lnk) {
        if(lnk.length() > 4){
            return lnk.startsWith("http");
        }
        return false;
    }

}