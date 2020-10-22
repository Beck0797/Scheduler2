package com.example.scheduler2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class webpage extends AppCompatActivity {
    private EditText editTextWebPageLink;
    private String webPageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);

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
            // register the url
        }

    }
}