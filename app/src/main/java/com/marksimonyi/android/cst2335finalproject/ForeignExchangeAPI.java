package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ForeignExchangeAPI extends AppCompatActivity {
    BaseAdapter myAdapter;
    ArrayList<ForeignExchangeList> myList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreignexchangeapi);
        ListView list = findViewById(R.id.List);
        //get a database:
        DatabaseHelper dbOpener = new DatabaseHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {DatabaseHelper.COL_ID, DatabaseHelper.COL_CURRENCY};
        Cursor results = db.query(false, DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int currencyColIndex = results.getColumnIndex(DatabaseHelper.COL_CURRENCY);
        int idColIndex = results.getColumnIndex(DatabaseHelper.COL_ID);
        while(results.moveToNext())
        {
            String currency = results.getString(currencyColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            myList.add(new ForeignExchangeList(currency, id));
        }
        list.setOnItemClickListener( ( parent,  view,  position,  id) ->{
            Toast.makeText(this, "You clicked", Toast.LENGTH_LONG).show();
        });
        myAdapter.notifyDataSetChanged();

        Button insertButton = findViewById(R.id.button);

        insertButton.setOnClickListener(click -> {
            Snackbar.make(insertButton, "Snacky boi", Snackbar.LENGTH_LONG).show();


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foreignexchangemenu, menu);

        MenuItem searchItem = menu.findItem(R.id.choice2item);
        SearchView sView = (SearchView) searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //what to do when the menu item is selected:

            case R.id.choice4:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();

                Intent nextPage = new Intent(ForeignExchangeAPI.this, ForeignExchangeHelpMenu.class);
                startActivity(nextPage);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }


    private class ListAdapter extends BaseAdapter {
        private ListAdapter(ArrayList<ForeignExchangeList> othermessage) {
            myList = othermessage;
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int i) {
            return myList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int p, View recycled, ViewGroup parent) {
            View thisRow = recycled;

            if (recycled == null)
                thisRow = getLayoutInflater().inflate(R.layout.foreignexchangeapilist, null);

            TextView itemText = thisRow.findViewById(R.id.api);
            itemText.setText("Array at:" + p + " is " + getItem(p));


            return thisRow;
        }
    }

}

