package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        //Get the information from the previous page:
        Intent fromPreviousPage = getIntent();
        TextView showTitle = findViewById(R.id.recTxtViewTitle);
        showTitle.setText(fromPreviousPage.getStringExtra("title"));
        //long id = fromPreviousPage.getLongExtra("Id", -1);

    }
}
