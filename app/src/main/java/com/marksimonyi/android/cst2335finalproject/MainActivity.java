package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainBtnNews = findViewById(R.id.mainBtnNews);
        mainBtnNews.setOnClickListener(btn -> {
            Intent newsPage = new Intent(MainActivity.this, NewsMain.class);
            startActivity(newsPage);
        });

        Button mainBtnRecipe = findViewById(R.id.mainBtnRecipe);
        mainBtnRecipe.setOnClickListener(bt -> {
            Intent nextPage = new Intent( MainActivity.this, RecipeActivity.class);
            startActivity(nextPage);
        });
    }
}
