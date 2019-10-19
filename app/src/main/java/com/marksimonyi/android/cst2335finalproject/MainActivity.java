package com.marksimonyi.android.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newsButton = findViewById(R.id.news_button);
        newsButton.setOnClickListener(btn -> {
            Intent goToNewsMain = new Intent(MainActivity.this, NewsMain.class);
            startActivity(goToNewsMain);
        });
    }
}
