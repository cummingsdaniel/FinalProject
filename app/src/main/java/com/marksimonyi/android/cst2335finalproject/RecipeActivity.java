package com.marksimonyi.android.cst2335finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.marksimonyi.android.cst2335finalproject.NewsApp.NewsMain;


/**
 * @author Mark Simonyi
 *
 * Recipe app main page code.
 */
public class RecipeActivity extends AppCompatActivity {

    //public static final String ACTIVITY_NAME = "RECIPE_ACTIVITY";
    /**
     * request code identifier for requests to empty page activity
     * used when viewing details on smaller screens
     */
    public static final int RQ_EMPTY_PAGE = 1;
    /**
     * the label on data containing the ID of the element within the database.
     */
    public static final String ID = "ID";
    /**
     * the label on data containing the original URL of the source recipe.
     */
    public static final String URL = "URL";
    /**
     * the label on data containing the Title of the element.
     */
    public static final String TITLE = "Title";
    /**
     * the label on data containing the URL of the element image.
     */
    public static final String IMAGE = "Image";
    /**
     * the label on data indicating whether the recipe is a favourite.
     */
    public static final String FAV = "Favourite";
    /**
     * the list that holds the search results
     * backs the listview that displays the results
     */
    ArrayList<Recipe> resultsList = new ArrayList<>();
    /**
     * the instance of the custom adapter that handles display of results
     */
    MSRecipeAdapter msAdapter;

    /**
     * onCreate method that sets up the initial state for the main page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.e(ACTIVITY_NAME, "In function: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        Toolbar tBar = findViewById(R.id.recTbMain);
        setSupportActionBar(tBar);
        ListView list = findViewById(R.id.recLstResults);
        msAdapter = new MSRecipeAdapter();
        list.setAdapter(msAdapter);
        boolean isTablet = findViewById(R.id.recFrame) != null; //check if the FrameLayout is loaded

        SharedPreferences prefs = getSharedPreferences("RecipePrefs", Context.MODE_PRIVATE);
        EditText searchBox = findViewById(R.id.recTxtSearch);
        searchBox.setText(prefs.getString("search",""));

        RecipeDBHelper dbHelper = new RecipeDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery(String.format("SELECT * FROM %s", RecipeDBHelper.REC_SAV_TABLE_NAME), new String[]{});

        int titleIndex = c.getColumnIndex( RecipeDBHelper.COL_TITLE );
        int imgIndex = c.getColumnIndex( RecipeDBHelper.COL_IMG );
        int urlIndex = c.getColumnIndex( RecipeDBHelper.COL_URL );
        int favIndex = c.getColumnIndex( RecipeDBHelper.COL_FAV );
        int dbID = c.getColumnIndex(RecipeDBHelper.COL_ID);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String title = c.getString(titleIndex);
            String img = c.getString(imgIndex);
            String url = c.getString(urlIndex);
            int fav = c.getInt(favIndex);
            Recipe r = new Recipe(title, img, url);
            r.setFav(fav==1);
            r.setId(c.getLong(dbID));
            resultsList.add(r);
            c.moveToNext();
        }
        c.close();
        msAdapter.notifyDataSetChanged();

        db.delete(RecipeDBHelper.REC_SAV_TABLE_NAME, RecipeDBHelper.COL_ID + " > ?", new String[]{"0"}); // clear table of existing values

        Toast.makeText(this, "Found and loaded " + resultsList.size() + " saved Recipes!", Toast.LENGTH_LONG).show();

        //This listens for items being clicked in the list view
        list.setOnItemClickListener(( parent,  view,  position,  id) -> {
            Log.e("you clicked on :" , "item "+ position);

            //When you click on a row, open selected recipe on a new page (ViewRecipe)
            Recipe selected = resultsList.get(position);
            Bundle data = new Bundle();
            data.putString(TITLE, selected.getTitle());
            data.putString(IMAGE, selected.getImage());
            data.putString(URL, selected.getUrl());
            data.putLong(ID, selected.getId());
            data.putBoolean(FAV, selected.isFav());

            //startActivity(nextPage);
            if(isTablet)
            {
                RecipeDetailFragment rdFragment = new RecipeDetailFragment(); //add a DetailFragment
                rdFragment.setArguments( data ); //pass it a bundle for information
                rdFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recFrame, rdFragment) //Add the fragment in FrameLayout
                        .addToBackStack(null) //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextPage = new Intent(RecipeActivity.this, ViewRecipe.class);
                nextPage.putExtras(data); //send data to next activity
                startActivityForResult(nextPage, RQ_EMPTY_PAGE); //make the transition
            }

        });

        Button searchButton = findViewById(R.id.recBtnSearch);
        searchButton.setOnClickListener(b -> {
            //EditText searchBox = findViewById(R.id.recTxtSearch);
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

        // CP-3 Complete everything.
        //5.	Each Activity must use a fragment somewhere in its graphical interface.
        //8.	The items listed in the ListView must be stored by the application so that appear the next time the application is launched.
        //    The user must be able to add and delete items, which would then also be stored in a database.
        //10.	Each activity must use SharedPreferences to save something about the application for use the next time the application is launched.
        //11.	All activities must be integrated into a single working application, on a single device or emulator.
        //12.	The interfaces must look professional, with GUI elements properly laid out and aligned.
        //13.	The functions and variables you write must be properly documented using JavaDoc comments.
        //



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQ_EMPTY_PAGE)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ID, -1);
                if (data.getBooleanExtra("add", false) ) {
                    Recipe r = resultsList.get(resultsList.indexOf(new Recipe((int)id)));
                    r.setFav(true);
                    addFavRecipe(r);
                } else {
                    deleteRecipeId((int)id);
                }

            }
        }
    }

    /**
     * helper method to delete a message from the favourites database by id
     * @param id
     */
    public void deleteRecipeId(int id)
    {
        Log.i("Delete this recipe:" , " id="+id);
        RecipeDBHelper dbHelper = new RecipeDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(dbHelper.REC_FAV_TABLE_NAME, dbHelper.COL_ID + " = ?", new String[]{""+id});

        //resultsList.get(resultsList.indexOf(new Recipe(id))).setFav(false);
    }

    public void addFavRecipe(Recipe r) {
        Log.i("Add this Recipe:" , " id="+r.getId());
        RecipeDBHelper dbHelper = new RecipeDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RecipeDBHelper.COL_TITLE, r.getTitle());
        cv.put(RecipeDBHelper.COL_IMG, r.getImage());
        cv.put(RecipeDBHelper.COL_URL, r.getUrl());
        int fav = r.isFav() ? 1 : 0;
        cv.put(RecipeDBHelper.COL_FAV, fav);
        db.insert(RecipeDBHelper.REC_FAV_TABLE_NAME, null, cv); // long id =
    }

    /**
     * query class that handles api access to the Food2Fork recipe search api
     */
    protected class RecipeQuery extends AsyncTask<String, Integer, String> {

        /**
         * the search string used to return results from the api
         */

        private String searchString;
        /**
         * the arraylist that holds the search results
         */
        private ArrayList<Recipe> results;

        /**
         * constructs a recipeQuery with the search string provided by the user
         * @param searchString the string to search with
         */
        RecipeQuery(String searchString) {
            this.searchString = searchString;
            results = new ArrayList<>();
        }

        /**
         * helper method for determining if a file has already been downloaded.
         * @param fname
         * @return true if the file exists, false if not.
         */
        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
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

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8), 8);
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
                        String imageUrl = aRecipe.getString("image_url");
                        Recipe r = new Recipe(aRecipe.getString("title"), imageUrl, aRecipe.getString("source_url"));
                        results.add(r);

                        String imgUrl = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
                        Log.i("msLoadImage","Looking for " + imgUrl);
                        if (fileExistance(imgUrl)) {
                            Log.i("msLoadImage", "Found " + imgUrl + " locally.");
//                            FileInputStream fis = null;
//                            try {    fis = openFileInput(imgUrl);   }
//                            catch (FileNotFoundException e) {    e.printStackTrace();  }
//                            Bitmap bm = BitmapFactory.decodeStream(fis);
//                            weatherImg = bm;

                        } else {
                            Log.i("msLoadImage", "Downloading " + imgUrl + " from internet.");

                            //String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";
                            Bitmap image = null;
                            //URL neurl = new URL(imageUrl);
                            URL neurl = new URL("https://bigoven-res.cloudinary.com/image/upload/d_recipe-no-image.jpg,t_recipe-480/lasagna-49.jpg");
                            HttpURLConnection connection = (HttpURLConnection) neurl.openConnection();
                            //urlConnection.setFollowRedirects(true); // responseCode = 301 always
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            //if (responseCode == 200 || responseCode == 301) {
                            if (responseCode == 200) {
                                image = BitmapFactory.decodeStream(connection.getInputStream());

                                if (image != null) {
                                    FileOutputStream outputStream = openFileOutput(imgUrl, Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            }

                            publishProgress(100);
                        }





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
         * @param value the values passed from publishProgress
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
         * @param result the result returned from doInBackground
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

        //public long getItemId(int position) { return 0; }
        public long getItemId(int position) { return getItem(position).getId(); }
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
        Intent nextPage;
        switch(item.getItemId())
        {
            case R.id.recMmiCharge:
                //Toast.makeText(this, "Car Charging Station finder not implemented", Toast.LENGTH_LONG).show();
                nextPage = new Intent( this, ElectricCar.class);
                startActivity(nextPage);
                break;
            case R.id.recMmiCurrency:
                //Toast.makeText(this, "Currency Exchange not implemented", Toast.LENGTH_LONG).show();
                nextPage = new Intent( this, ForeignExchangeAPI.class);
                startActivity(nextPage);
                break;
            case R.id.recMmiNews:
                //Toast.makeText(this, "News not implemented", Toast.LENGTH_LONG).show();
                nextPage = new Intent( this, NewsMain.class);
                startActivity(nextPage);
                break;
            case R.id.recMmiFav:
                nextPage = new Intent(RecipeActivity.this, RecipeFavourites.class);
                startActivity(nextPage); //make the transition
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

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("RecipePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        EditText searchBox = findViewById(R.id.recTxtSearch);
        edit.putString("search", searchBox.getText().toString());
        edit.commit();

        RecipeDBHelper dbHelper = new RecipeDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Recipe r : resultsList) {
            ContentValues cv = new ContentValues();
            cv.put(RecipeDBHelper.COL_TITLE, r.getTitle());
            cv.put(RecipeDBHelper.COL_IMG, r.getImage());
            cv.put(RecipeDBHelper.COL_URL, r.getUrl());
            db.insert(RecipeDBHelper.REC_SAV_TABLE_NAME, null, cv); // long id =
        }

    }
}
