package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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
//        Intent fromPreviousPage = getIntent();
//        TextView showTitle = findViewById(R.id.recTxtViewTitle);
//        showTitle.setText(fromPreviousPage.getStringExtra(RecipeActivity.TITLE));
        //long id = fromPreviousPage.getLongExtra("Id", -1);

        Toolbar tBar = findViewById(R.id.recTbMain);
        setSupportActionBar(tBar);

        Bundle data = getIntent().getExtras();

        RecipeDetailFragment rdFragment = new RecipeDetailFragment(); //add a DetailFragment
        rdFragment.setArguments( data ); //pass it a bundle for information
        rdFragment.setTablet(false);  //tell the fragment if it's running on a tablet or not
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recFrame, rdFragment) //Add the fragment in FrameLayout
                //.addToBackStack(null) //make the back button undo the transaction
                .commit(); //actually load the fragment.

        //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
        Snackbar.make(tBar, "snackbarTest", Snackbar.LENGTH_LONG).show();

    }

    /**
     * method to inflate the menu from xml
     * called automatically when calling setSupportActionBar(Toolbar)
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rec_main_menu, menu);
        return true;
    }

    /**
     * toolbar menu method that is called automatically when an item from the menu is selected.
     * Determines which menu item was clicked and performs the appropriate actions.
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.recMmiCharge:
                Toast.makeText(this, "Car Charging Station finder not implemented", Toast.LENGTH_LONG).show();
                //Intent nextPage = new Intent( this, ChargeActivity.class);
                //startActivity(nextPage);
                break;
            case R.id.recMmiCurrency:
                Toast.makeText(this, "Currency Exchange not implemented", Toast.LENGTH_LONG).show();
                //Intent nextPage = new Intent( this, CurrencyActivity.class);
                //startActivity(nextPage);
                break;
            case R.id.recMmiNews:
                Toast.makeText(this, "News not implemented", Toast.LENGTH_LONG).show();
                //Intent nextPage = new Intent( this, NewsActivity.class);
                //startActivity(nextPage);
                break;
            case R.id.recMmiHelp:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.setTitle("Recipe Search Help")
                        .setMessage(R.string.recHelpFullText)
                        .setPositiveButton("OK",(d,w) -> {  /* nothing */})
                        .create();
                dialog.show();

                break;
        }
        return true;
    }
}
