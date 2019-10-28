package com.marksimonyi.android.cst2335finalproject;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "RECIPE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In function: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

    }
}
