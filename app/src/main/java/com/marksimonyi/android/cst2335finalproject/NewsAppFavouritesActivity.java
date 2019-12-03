package com.marksimonyi.android.cst2335finalproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * InternetStuff.java
 * Section 020
 * Daniel Cummings
 * 2019-12-02
 */
public class NewsAppFavouritesActivity extends AppCompatActivity {
    ListView listFavs;
    ProgressBar progressBar;
    ListNewsAdapter listFavsAdapter = null;
    ArrayList<HashMap<String, String>> favList =null; //an Arraylist that holds

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_favourites_list);
        favList=new ArrayList<>();
        listFavs = findViewById(R.id.listFav);
        progressBar = findViewById(R.id.newsapp_progressbar);
        listFavs.setEmptyView(progressBar);
        listFavs.setAdapter(listFavsAdapter = new ListNewsAdapter(this, favList, true));


        NewsAppDatabaseHelper dbOpener = new NewsAppDatabaseHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        String[] columns = {NewsAppDatabaseHelper.COL_URL, NewsAppDatabaseHelper.COL_TITLE, NewsAppDatabaseHelper.COL_DESCRIPTION, NewsAppDatabaseHelper.COL_IMAGE_URL};
        Cursor dbresult = db.query(false, NewsAppDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int urlColIndex = dbresult.getColumnIndex(NewsAppDatabaseHelper.COL_URL);
        int titleColIndex = dbresult.getColumnIndex(NewsAppDatabaseHelper.COL_TITLE);
        int descriptionColIndex = dbresult.getColumnIndex(NewsAppDatabaseHelper.COL_DESCRIPTION);
        int imageColIndex = dbresult.getColumnIndex(NewsAppDatabaseHelper.COL_IMAGE_URL);

        while(dbresult.moveToNext())
        {
            String url = dbresult.getString(urlColIndex);
            String title = dbresult.getString(titleColIndex);
            String description = dbresult.getString(descriptionColIndex);
            String imageUrl = dbresult.getString(imageColIndex);
            HashMap<String, String> data = new HashMap<>();

            //adds the new Article to the array list:
            data.put(NewsMain.KEY_URL, url);
            data.put(NewsMain.KEY_TITLE, title);
            data.put(NewsMain.KEY_DESCRIPTION, description);
            data.put(NewsMain.KEY_URLTOIMAGE, imageUrl);
            favList.add(data);
        }
        listFavsAdapter.notifyDataSetChanged();


    }
}
