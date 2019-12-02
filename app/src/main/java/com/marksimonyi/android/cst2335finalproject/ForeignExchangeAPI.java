package com.marksimonyi.android.cst2335finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ForeignExchangeAPI extends AppCompatActivity {
    BaseAdapter myAdapter;
    ArrayList<ForeignExchangeList> myList = new ArrayList<>();
    String x,y,z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreignexchangeapi);
        ListView list = findViewById(R.id.List);
        //get a database:
        DatabaseHelper dbOpener = new DatabaseHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {DatabaseHelper.COL_CURRENCY,DatabaseHelper.COL_CONVO1,DatabaseHelper.COL_CONVO2};
        Cursor results = db.query(false, DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        ForeignQuery foreign = new ForeignQuery();
        int currencyColIndex = results.getColumnIndex(DatabaseHelper.COL_CURRENCY);
        int convo1ColIndex = results.getColumnIndex(DatabaseHelper.COL_CONVO1);
        int convo2ColIndex = results.getColumnIndex(DatabaseHelper.COL_CONVO2);
        while(results.moveToNext())
        {
            String currency = results.getString(currencyColIndex);
            String Converted = results.getString(convo1ColIndex);
            String converted2 = results.getString(convo2ColIndex);

            //add the new Contact to the array list:
            myList.add(new ForeignExchangeList(currency,Converted,converted2));
            myAdapter.notifyDataSetChanged();
        }
        list.setOnItemClickListener( ( parent,  view,  position,  id) ->{
            Toast.makeText(this, "You clicked", Toast.LENGTH_LONG).show();
        });


        Button insertButton = findViewById(R.id.button);

        insertButton.setOnClickListener(click -> {
            Snackbar.make(insertButton, "Converting", Snackbar.LENGTH_LONG).show();
            EditText base1 = findViewById(R.id.base);
            EditText conversion1 = findViewById(R.id.conversion1);
            EditText conversion2 = findViewById(R.id.conversion2);

             z = base1.getText().toString();
             x = conversion1.getText().toString();
             y = ","+conversion2.getText().toString();

            foreign.execute();

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
    class ForeignQuery extends AsyncTask<String, Integer, String> {
        String base, convo1, convo2;



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            myList.add(new ForeignExchangeList(base,convo1,convo2));
            myAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar bar = findViewById(R.id.progressbar);
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) { //Type 1
            if (x == "") x = "BLANK";
            if (y == "") y = "BLANK";

            String ret = null;
            String queryURLUV = "http://api.openweathermap.org/data/2.5/https://api.exchangeratesapi.io/latest?base="+z+"&symbols="+x+y;
            try {
                publishProgress(25);
                // Connect to the server:
                URL url2 = new URL(queryURLUV);
                HttpURLConnection url2Connection = (HttpURLConnection) url2.openConnection();
                InputStream inStream2 = url2Connection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream2, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");

                }
                String result = sb.toString();
                JSONObject jObject = new JSONObject(result);

                base = jObject.getString("base");
                publishProgress(50);
                convo1 = jObject.getString(x);
                publishProgress(75);
                convo2 = jObject.getString(y);
                publishProgress(100);


            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";} catch (JSONException e) {
                e.printStackTrace();
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }
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

