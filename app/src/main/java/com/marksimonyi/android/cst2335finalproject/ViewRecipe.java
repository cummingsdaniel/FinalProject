package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;


/**
 * code for the viewRecipe activity which shows detailed Recipe information.
 */
public class ViewRecipe extends AppCompatActivity {

    /**
     * onCreate method which sets up the initial state of the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        //Get the information from the previous page:
        Intent fromPreviousPage = getIntent();
        TextView showTitle = findViewById(R.id.recTxtViewTitle);
        showTitle.setText(fromPreviousPage.getStringExtra("title"));
        //long id = fromPreviousPage.getLongExtra("Id", -1);

        //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
        Snackbar.make(showTitle, "snackbarTest", Snackbar.LENGTH_LONG).show();

    }
}
