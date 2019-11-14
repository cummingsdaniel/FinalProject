package com.marksimonyi.android.cst2335finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Mark Simonyi
 *
 * Recipe app main page code.
 */
public class RecipeActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "RECIPE_ACTIVITY";
    ArrayList<Recipe> resultsList = new ArrayList<>();
    MSRecipeAdapter msAdapter;

    /**
     * onCreate method that sets up the initial state for the main page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In function: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        ListView list = findViewById(R.id.recLstResults);

        resultsList.add(new Recipe("example1", "", ""));
        resultsList.add(new Recipe("example2", "", ""));
        resultsList.add(new Recipe("example3", "", ""));

        Toast.makeText(this, "Updated " + 3 + " rows", Toast.LENGTH_LONG).show();

        msAdapter = new MSRecipeAdapter();
        list.setAdapter(msAdapter);

        //This listens for items being clicked in the list view
        list.setOnItemClickListener(( parent,  view,  position,  id) -> {
            Log.e("you clicked on :" , "item "+ position);

            //When you click on a row, open selected recipe on a new page (ViewRecipe)
            Recipe selected = resultsList.get(position);
            Intent nextPage = new Intent(RecipeActivity.this, ViewRecipe.class);
            nextPage.putExtra("title", selected.getTitle());
            nextPage.putExtra("image", selected.getImage());
            nextPage.putExtra("url", selected.getUrl());
            nextPage.putExtra("Id", id);
            //startActivityForResult(nextPage, R.layout.activity_view_recipe);
            startActivity(nextPage);
        });

        Button searchButton = findViewById(R.id.recBtnSearch);
        searchButton.setOnClickListener(b -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //This is the builder pattern, just call many functions on the same object:
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("This button is not ready yet!")
                    .setPositiveButton("OK",(d,w) -> {  /* nothing */})
                    //If you click the "Cancel" button:
                    .setNegativeButton("Also, OK", (d,w) -> {  /* nothing */})
                    .create();

            //then show the dialog
            dialog.show();
        });


        //1.	Each person’s project must have a ListView somewhere to present items. Selecting an item from the ListView must show detailed information about the item selected.
        //2.	Each activity must have at least 1 progress bar and at least 1 button.
        //3.	Each activity must have at least 1 edit text with appropriate text input method and at least 1 Toast, Snackbar, and custom dialog notification.



        //•	Create an interface that allows the user to enter a search term about recipes.
        //    There should be a "search" button that will search for all recipes that include that term. For example, this is a search for "chicken breast":
        //•	https://www.food2fork.com/api/search?key=YOUR_API_KEY&q=chicken%20breast
        //
        //•	Your application will call the web server to retrieve a list of recipes that match the term.
        //    Your application should create a list of titles that are retrieved in the results, and display those results in a list.
        //    Clicking on a title should display the title, image, and the URL of the recipe. Clicking on the URL should go to a web page that shows the recipe.
        //•	The user should be able to save a recipe into a list of favourites, saved in a database.
        //    This list should be accessible from a menu item. The user should be able to remove items from the list and database.
        //•	Your application should save the last topic that was searched to display the next time the application is launched.

    }

    /**
     * adapter for displaying recipe results
     */
    protected class MSRecipeAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return resultsList.size();
        }

        public Recipe getItem(int position){
            return resultsList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.recipe_row, parent, false );

            Recipe thisRow = getItem(position);
            TextView rowTitle = newView.findViewById(R.id.recLblRowTitle);
            //TextView rowId = (TextView)newView.findViewById(R.id.row_id);

            rowTitle.setText(thisRow.getTitle());
            //rowId.setText("id:" + thisRow.getId());
            //return the row:
            return newView;
        }

        public long getItemId(int position)
        {
            return 0;
        }
        //public long getItemId(int position) { return getItem(position).getId(); }
    }
}
