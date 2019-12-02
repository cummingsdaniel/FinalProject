package com.marksimonyi.android.cst2335finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeFavourites extends AppCompatActivity {

    /**
     * the list that holds the search results
     * backs the listview that displays the results
     */
    ArrayList<Recipe> resultsList = new ArrayList<>();
    /**
     * the instance of the custom adapter that handles display of results
     */
    MSRecipeAdapter msAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favourites);

        Toolbar tBar = findViewById(R.id.recTbMain);
        setSupportActionBar(tBar);
        ListView list = findViewById(R.id.recLstResults);
        msAdapter = new MSRecipeAdapter();
        list.setAdapter(msAdapter);
        boolean isTablet = findViewById(R.id.recFrame) != null; //check if the FrameLayout is loaded

        RecipeDBHelper dbHelper = new RecipeDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery(String.format("SELECT * FROM %s", RecipeDBHelper.REC_FAV_TABLE_NAME), new String[]{});

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

        list.setOnItemClickListener(( parent,  view,  position,  id) -> {
            Log.e("you clicked on :" , "item "+ position);

            //When you click on a row, open selected recipe on a new page (ViewRecipe)
            Recipe selected = resultsList.get(position);
            Bundle data = new Bundle();
            data.putString(RecipeActivity.TITLE, selected.getTitle());
            data.putString(RecipeActivity.IMAGE, selected.getImage());
            data.putString(RecipeActivity.URL, selected.getUrl());
            data.putLong(RecipeActivity.ID, selected.getId());
            data.putBoolean(RecipeActivity.FAV, selected.isFav());

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
                Intent nextPage = new Intent(RecipeFavourites.this, ViewRecipe.class);
                nextPage.putExtras(data); //send data to next activity
                startActivityForResult(nextPage, RecipeActivity.RQ_EMPTY_PAGE); //make the transition
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RecipeActivity.RQ_EMPTY_PAGE)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(RecipeActivity.ID, -1);
                if (data.getBooleanExtra("add", false) ) {
                    Recipe r = resultsList.get(resultsList.indexOf(new Recipe((int)id)));
                    r.setFav(true);
                    //addFavRecipe(r);
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

        Recipe r = resultsList.get(resultsList.indexOf(new Recipe(id)));
        resultsList.remove(r);
        msAdapter.notifyDataSetChanged();
    }

    /**
     * adapter for displaying recipe results
     */
    protected class MSRecipeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return resultsList.size();
        }

        public Recipe getItem(int position) {
            return resultsList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.recipe_row, parent, false);

            Recipe thisRow = getItem(position);
            TextView rowTitle = newView.findViewById(R.id.recLblRowTitle);
            //TextView rowId = (TextView)newView.findViewById(R.id.row_id);

            rowTitle.setText(thisRow.getTitle());
            //rowId.setText("id:" + thisRow.getId());
            //return the row:
            return newView;
        }

        //public long getItemId(int position) { return 0; }
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}
