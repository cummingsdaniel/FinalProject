package com.marksimonyi.android.cst2335finalproject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ListNewsAdapter.java
 * Section 020
 * Daniel Cummings
 * 2019-12-02
 */

/*the adapter class that implements the interface for NewsMain or NewsAppFavouritesActivity*/
class ListNewsAdapter extends BaseAdapter {
    private Activity activity;  // the activity
    private ArrayList<HashMap<String, String>> hashMapArrayList;//an Arraylist that will map(<key, value>) as data
    private boolean isFav; // a boolean that determains weather its Favourites list or not.

    /*Class Constructer with parameters*/
    public ListNewsAdapter(Activity activity, ArrayList<HashMap<String, String>> hashMapArrayList ,boolean isFav) {
        this.activity = activity;
        this.hashMapArrayList = hashMapArrayList;
        this.isFav = isFav;
    }
    public void bread(String s){
        Toast.makeText(activity, s, Toast.LENGTH_SHORT);
    }
    /*returns the hash map arraylists size*/
    public int getCount() {
        return hashMapArrayList.size();
    }
    /*returns what to show at row position of Hash Map*/
    public HashMap<String, String>  getItem(int position) {
        return hashMapArrayList.get(position);
    }
    /*returns the database id of the item at position of ArrayList*/
    public long getItemId(int position) {
        return position;
    }
    /*Specifies how each Row in the News list will look*/
    public View getView(int position, View freshView, ViewGroup parent) {
        ListNewsRow listNewsRow = null;
            listNewsRow = new ListNewsRow();

        // The Layout Inflater is initialized to the LayoutInflater in context to the activity
            LayoutInflater inflater = LayoutInflater.from(activity);
        //freshView is now the root object for finding the widgets
            freshView = inflater.inflate(R.layout.newsapp_list_row, parent, false);
        //the objects of the news list row are initialized to the widgets of the xml file.
            listNewsRow.galleryImage = (ImageView) freshView.findViewById(R.id.newsapp_listrow_galleryImage);
            listNewsRow.title = (TextView) freshView.findViewById(R.id.newsapp_listrow_title);
            listNewsRow.description = (TextView) freshView.findViewById(R.id.newsapp_listrow_description);
            listNewsRow.saveToFavs = (Button) freshView.findViewById(R.id.news_save_to_fav);
            listNewsRow.goToPage = (Button) freshView.findViewById(R.id.news_goto_article);
            listNewsRow.removeButton = (Button) freshView.findViewById(R.id.news_remove_from_favs);
        //Sets a tag associated with the view and a key.
            freshView.setTag(listNewsRow);
        //fresh view is finalized to a view for later use.
        final View view = freshView;
        //ID are set to the ImageView and textView objects
        listNewsRow.galleryImage.setId(position);
        listNewsRow.title.setId(position);
        listNewsRow.description.setId(position);

        //NewsAppDatabaseHelper is declaired, initialized and instantiated to the activity
        NewsAppDatabaseHelper dbhelper = new NewsAppDatabaseHelper(activity);

        //The add to favs button behavior
        listNewsRow.saveToFavs.setOnClickListener(fbtn -> {
            Log.d("This thing...", "Saved to fav");
        //Hasp Map object is declared as map, initialized and instantiated to the position of the ArrayList
            HashMap<String, String> map = getItem(position);
            if(dbhelper.insertData(map.get(NewsMain.KEY_TITLE),
                        map.get(NewsMain.KEY_URL),
                        map.get(NewsMain.KEY_DESCRIPTION),
                    map.get(NewsMain.KEY_URLTOIMAGE))){
                Log.d("saved to favourites", "Saved");
                Toast.makeText(activity, "Twas Saved", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "This was saved", Snackbar.LENGTH_SHORT).show();
            }else{
                bread("Nope");
            }
        });

        HashMap<String, String> mapThread = hashMapArrayList.get(position);
        String url = mapThread.get(NewsMain.KEY_URL);

        listNewsRow.removeButton.setOnClickListener(rbtn ->{
            if(dbhelper.removeData(url)) {
                Log.d("it is deleted", "deleted");
                new AlertDialog.Builder(activity)
                        .setMessage("This article was deleted from favourites")
                        .setCancelable(false)
                        .setPositiveButton("ok", (dialog, which) ->
                                Snackbar.make(view, "You are fake news", Snackbar.LENGTH_SHORT).show()).show();
            }
            activity.finish();
        });

        listNewsRow.goToPage.setOnClickListener(pbtn ->{
            Intent i = new Intent(activity, Article.class);
            i.putExtra("url",url );
            activity.startActivity(i);
        });

        try{
            listNewsRow.title.setText(mapThread.get(NewsMain.KEY_TITLE));
            listNewsRow.description.setText(mapThread.get(NewsMain.KEY_DESCRIPTION));

            if(mapThread.get(NewsMain.KEY_URLTOIMAGE).toString().length() < 5)
            {
                listNewsRow.galleryImage.setVisibility(View.GONE);
            }else{
                Picasso.get()
                        .load(mapThread.get(NewsMain.KEY_URLTOIMAGE))
                        .resize(300, 200)
                        .centerCrop()
                        .into(listNewsRow.galleryImage);
            }
        }catch(Exception e) {}

        if(isFav) {
            listNewsRow.removeButton.setVisibility(View.VISIBLE);
            listNewsRow.saveToFavs.setVisibility(View.INVISIBLE);
        }else{
            listNewsRow.removeButton.setVisibility(View.INVISIBLE);
            listNewsRow.saveToFavs.setVisibility(View.VISIBLE);
        }
        return freshView;
    }

    }

/*Functon based class that holds declaired variables of for each row of the ListView*/
class ListNewsRow {
    ImageView galleryImage;
    TextView title;
    TextView description;
    Button saveToFavs; 
    Button goToPage;
    Button removeButton;
}
