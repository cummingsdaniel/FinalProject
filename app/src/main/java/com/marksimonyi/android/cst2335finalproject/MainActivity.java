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
        Button mainBtnCurrency =  findViewById(R.id.mainBtnCurrency);

        mainBtnCurrency.setOnClickListener(clk -> {
            Intent nextPage = new Intent(MainActivity.this, ForeignExchangeAPI.class);
            startActivity(nextPage);
        });
    }
}
