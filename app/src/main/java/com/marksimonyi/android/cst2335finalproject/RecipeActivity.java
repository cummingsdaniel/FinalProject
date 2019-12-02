package com.marksimonyi.android.cst2335finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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

        Toolbar tBar = findViewById(R.id.recTbMain);
        setSupportActionBar(tBar);
        ListView list = findViewById(R.id.recLstResults);


        //resultsList.add(new Recipe("example1", "", ""));
        //resultsList.add(new Recipe("example2", "", ""));
        //resultsList.add(new Recipe("example3", "", ""));

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
            //AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //AlertDialog dialog = builder.setTitle("Alert!")
            //        .setMessage("This button is not ready yet!")
            //        .setPositiveButton("OK",(d,w) -> {  /* nothing */})
            //        .setNegativeButton("Also, OK", (d,w) -> {  /* nothing */})
            //        .create();
            //dialog.show();

            EditText searchBox = findViewById(R.id.recTxtSearch);
            RecipeQuery rq = new RecipeQuery(searchBox.getText().toString());
            Log.i("ms", searchBox.getText().toString());
            rq.execute();
        });

        // CP-1
        //1.	Each person’s project must have a ListView somewhere to present items. Selecting an item from the ListView must show detailed information about the item selected.
        //2.	Each activity must have at least 1 progress bar and at least 1 button.
        //3.	Each activity must have at least 1 edit text with appropriate text input method and at least 1 Toast, Snackbar, and custom dialog notification.

        // CP-2
        //4.	The software must have 1 different activity written by each person in your group. The activity must be accessible by selecting a graphical icon from a Toolbar.
        //6.	Each activity must have a help menu item that displays a dialog with the author’s name, Activity version number, and instructions for how to use the interface.
        //7.	There must be at least 1 other language supported by your Activity.
        //    If you are not bilingual, then you must support both British and American English (words like colour, color, neighbour, neighbor, etc).
        //    If you know a language other than English, then you can support that language in your application and don’t need to support American English.
        //9.	Each activity must use an AsyncTask to retrieve data from an http server.




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
     * query class that handles api access to the Food2Fork recipe search api
     */
    protected class RecipeQuery extends AsyncTask<String, Integer, String> {

        /**
         * the search string used to return results from the api
         */
        private String searchString;
        private ArrayList<Recipe> results;

        /**
         * constructs a recipeQuery with the search string provided by the user
         * @param searchString
         */
        RecipeQuery(String searchString) {
            this.searchString = searchString;
            results = new ArrayList<>();
        }

        /**
         * the main portion of the asyncTask; accesses the api and parses results
         * called automatically when execute() is called.
         * @param args
         * @return
         */
        @Override
        protected String doInBackground(String... args) {
            Log.i("ms", "in background");
            String ret = null;
            try {
                URL url;
                if (searchString.equals("Lasagna")) {
                    url = new URL("http://torunski.ca/FinalProjectLasagna.json");
                } else {
                    url = new URL("http://torunski.ca/FinalProjectChickenBreast.json");
                }

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){sb.append(line + "\n");}String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                int count = jObject.getInt("count");
                publishProgress(100*(1/(count+1)));
                JSONArray jArray = jObject.getJSONArray("recipes");
                for (int i = 0; i<jArray.length(); ++i) {
                    try {
                        JSONObject aRecipe = jArray.getJSONObject(i);
                        Recipe r = new Recipe(aRecipe.getString("title"), aRecipe.getString("image_url"), aRecipe.getString("source_url"));
                        results.add(r);
                    } catch(org.json.JSONException je) {
                        //error loading recipe
                    }
                    publishProgress(100*((i+1)/(count+1)));
                }

            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?"; }
            catch(org.json.JSONException je){ ret = "JSON exception"; }

            return ret;
        }

        /**
         * automatically called when publishProgress is called.
         * Updates the ui/progressbar (asynchronously) to reflect progress
         * @param value
         */
        @Override
        protected void onProgressUpdate(Integer... value) {
            Log.i("msV", "in publish progress");
            ProgressBar progress = findViewById(R.id.recProgressBar);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(value[0]);
        }

        /**
         * automatically called when doInBackground is finished.
         * displays results in UI.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i("msV", "in post Execute");
            ProgressBar progress = findViewById(R.id.recProgressBar);
            progress.setVisibility(View.INVISIBLE);
            progress.setProgress(0);

            if (result!=null) {
                Log.e("msResult", result);
            } else {

                resultsList = results;
                msAdapter.notifyDataSetChanged();

            }
        }

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
