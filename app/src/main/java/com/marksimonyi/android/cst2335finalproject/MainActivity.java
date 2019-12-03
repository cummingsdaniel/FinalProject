package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.marksimonyi.android.cst2335finalproject.NewsApp.NewsMain;

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
        Button mainBtnCurrency =  findViewById(R.id.mainBtnCurrency);

        mainBtnCurrency.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, ForeignExchangeAPI.class);
            startActivity(nextPage);
        });

        Button mainBtnElectric =  findViewById(R.id.mainBtnCar);

        mainBtnElectric.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, ElectricCar.class);
            startActivity(nextPage);
        });
    }
}
