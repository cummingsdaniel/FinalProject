package com.marksimonyi.android.cst2335finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ElectricCar extends AppCompatActivity {

    protected SharedPreferences sp;
    protected SharedPreferences.Editor edit;
    protected Snackbar sb;

    protected ImageButton buttonBack;
    protected Button buttonStart;
    protected EditText editLat, editLon;
    protected ProgressBar progress;

    protected Handler handler = new Handler();
    protected ListView listView;

    protected String search;
    protected String x,y;
    protected Double lat, lon;
    protected int prog;
    protected ArrayAdapter adapt;
    protected ArrayList<ListItem> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electric_car);

        sp = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editLat = findViewById(R.id.editLat);
        editLon = findViewById(R.id.editLon);

        if (sp.contains("lat") && sp.contains("lon")) {
            lat = Double.parseDouble(sp.getString("lat", "0"));
            lon = Double.parseDouble(sp.getString("lon", "0"));

            editLat.setText("" + lat);
            editLon.setText("" + lon);
        }

        buttonStart();
        buttonBack();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(!(lat == null && lon == null)) {
            save();
        }
    }

    protected void save(){
        sp = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        edit = sp.edit();

        edit.putString("lat", lat.toString());
        edit.putString("lon", lon.toString());

        edit.commit();
    }

    protected void startProgressBar(){
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        //start thread
        new Thread(new Runnable() {
            public void run() {
                while (prog < 100) {
                    // update progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            progress.setProgress(prog);
                            updateList();
                        }
                    });
                    try {
                        // sleep
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected void buttonStart(){
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //notify user that searching started
                Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_SHORT).show();

                //disables the button so user can't repeatedly tap until process has finished
                buttonStart.setVisibility(View.GONE);

                //start progress bar
                startProgressBar();

                //get lat/lon
                y = editLat.getText().toString();
                x = editLon.getText().toString();

                if(y.isEmpty())
                    lon = 0.0;
                if(x.isEmpty())
                    lat = 0.0;

                //saves the latitude and longitude to shared preferences
                save();

                //makes the list visible
                list.clear();
                listView = findViewById(R.id.list);
                listView.setVisibility(View.VISIBLE);

                //get information from website
                HttpGet http = new HttpGet();

                //populate list
                for(int i = 0; i < 10; i++)
                    addToList(http.locations[i], http.lons[i], http.lats[i], http.phones[i]);

                updateList();

                //code just to show off elements
                list.clear();
                addToList("title","x","y","phone");

                buttonStart.setVisibility(View.VISIBLE);
                prog = 0;
            }
        });
    }

    protected void updateList(){
        listView = findViewById(R.id.list);
        adapt = new ArrayAdapter(this,R.layout.list_row,list);
    }



    protected void addToList(String title, String lon, String lat, String phone){
        list.add(new ListItem(title, lon, lat, phone));
        prog+=10;
        updateList();
    }

    protected void buttonBack(){
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb = Snackbar.make(view, "Do you wish to go back?", Snackbar.LENGTH_SHORT)
                        .setAction("Exit", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"Exiting..",Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.activity_main); //go back to main activity
                            }
                        });
                sb.show();
            }
        });
    }

    private class HttpGet extends AsyncTask<Void, Void, Void>{
        public static final String REQUEST_METHOD = "GET";
        String[] locations = new String[10];
        String[] lats = new String[10];
        String[] lons = new String[10];
        String[] phones = new String[10];


        @Override
        protected Void doInBackground(Void... voids) {
            search = "https://api.openchargemap.io/v3/poi/?output=xml&countrycode=CA&latitude=" + lat +
                    "&longitude=" + lon + "&maxresults=10";
            String result = "";

            try{
                URL url = new URL(search);
                HttpURLConnection connect = (HttpURLConnection)url.openConnection();

                connect.setRequestMethod(REQUEST_METHOD);
                connect.setReadTimeout(15000);
                connect.setConnectTimeout(15000);

                InputStreamReader input = new InputStreamReader(connect.getInputStream());
                BufferedReader reader = new BufferedReader(input);
                StringBuilder sb = new StringBuilder();

                //supposed to read the information, get 10 lines
                for(int i = 0; i < 10; i++){
                    Toast.makeText(getApplicationContext(),reader.toString(),Toast.LENGTH_SHORT).show();
                }



                //closing the reader/input
                reader.close();
                input.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //supposed to parse the input and bring it back to the activity,
            //then create the list items, then populate the list
            for(int i = 0; i < 10; i++){
                locations[i] = "";
                lats[i] = "";
                lons[i] = "";
                phones[i] = "";
            }


            return null;
        }
    }
}
