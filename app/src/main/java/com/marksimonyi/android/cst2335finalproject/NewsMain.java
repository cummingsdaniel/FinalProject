package com.marksimonyi.android.cst2335finalproject;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * NewsMain.java
 * Section 020
 * Daniel Cummings
 * 2019-12-02
 */

/*This Class launches the News List interface.*/
public class NewsMain extends AppCompatActivity {

    ListView listNews;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>(); //an Arraylist that will map(<key, value>)

    static final String API_KEY = "f665129def0f4fc1bab8809ee6fc13da"; //API key
    String INPUT_SEARCH = "trump"; //input search key. For now it is set to the value of trump as a default to ensure it works
    static final String KEY_TITLE = "title"; //title Key
    static final String KEY_DESCRIPTION = "description"; //description key
    static final String KEY_URL = "url"; //url key
    static final String KEY_URLTOIMAGE = "urlToImage"; //image url key

    /*It's Debugging tool...without the crust *slowclap* */
    public void bread(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsapp_activity_main); //initializes Layout of Activity
        listNews = findViewById(R.id.listNews); //initializes ListView objests
        progressBar = findViewById(R.id.newsapp_progressbar); //initializes progress bar
        listNews.setEmptyView(progressBar); //instantiates the progress bar to show if adaptor is empty
        /*makes a toast if internet isn't working*/
        if(InternetStuff.isNetworkAvailable((getApplicationContext()))) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            bread("no Internets.");
        }
    }

    /*This inflates the Search bar on click*/
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newsapp_main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.newsapp_search_item)
                .getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.newsapp_search_item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Click to Search....");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /*Performs the search*/
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 2) {
                    dataList.clear();
                    INPUT_SEARCH = query; //assigns the input query as the input search value
                    DownloadNews search = new DownloadNews();
                    search.execute();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }

    @Override /*options on the toolbar*/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.newsapp_go_to_favs:
                Intent jumpToFavourites = new Intent(NewsMain.this, NewsAppFavouritesActivity.class);
                startActivity(jumpToFavourites);
                break;
            default:
                break;
        }
        return true;
    }
    /*A private AsyncTask inherited class for the datalist.*/
    private class DownloadNews extends AsyncTask<String, Void, String> {
        String responseType;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        /*executes the newslist in the background*/
        @Override
        protected String doInBackground(String... args) {
            String xml = InternetStuff.excuteGet("https://newsapi.org/v2/everything?apiKey=" + API_KEY + "&q=" +INPUT_SEARCH
            );
            return xml;
        }


        @Override
        protected void onPostExecute(String sentFromDoInBackground) {
            if (sentFromDoInBackground.length() > 10) { //Just checking if https address is not empty
                try {
                    JSONObject jsonResponse = new JSONObject(sentFromDoInBackground);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");//
                    /*the keys and values of the data structure. Shortening strings with long texts to a fixed length value.
                     that value represents the original string and indexes the key and values in a Node link list*/
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE));
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION));
                        map.put(KEY_URL, jsonObject.optString(KEY_URL));
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE));
                        dataList.add(map);
                    }
                } catch (JSONException e) { //catches
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                ListNewsAdapter adapter = new ListNewsAdapter(NewsMain.this, dataList, false);
                listNews.setAdapter(adapter);


                listNews.setOnItemClickListener((parent, view, position, id) -> {

                    Intent i = new Intent(NewsMain.this, Article.class);
                    i.putExtra("url", dataList.get(+position).get(KEY_URL));
                    startActivity(i);
                });

            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}