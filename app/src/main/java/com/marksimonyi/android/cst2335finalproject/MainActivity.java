package com.marksimonyi.android.cst2335finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainBtnRecipe = findViewById(R.id.mainBtnRecipe);
        mainBtnRecipe.setOnClickListener(bt -> {
            Intent nextPage = new Intent( MainActivity.this, RecipeActivity.class);
            startActivity(nextPage);
        });
    }
}
