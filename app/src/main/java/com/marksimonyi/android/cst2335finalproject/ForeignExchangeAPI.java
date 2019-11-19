package com.marksimonyi.android.cst2335finalproject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ForeignExchangeAPI extends AppCompatActivity {
    BaseAdapter myAdapter;
    ArrayList<String> myList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreignexchangeapi);
        ListView list = findViewById(R.id.List);
        list.setAdapter( myAdapter = new ListAdapter(myList));

        myList.add("x");
        myList.add("y");
        myList.add("z");
        myAdapter.notifyDataSetChanged();

        Button insertButton =  findViewById(R.id.button);

        insertButton.setOnClickListener(click -> {
            Toast.makeText(this, "Toasty", Toast.LENGTH_LONG).show();
            Snackbar.make(insertButton, "Snacky boi", Snackbar.LENGTH_LONG).show();


        });
    }
    private class ListAdapter extends BaseAdapter {
        private ListAdapter (ArrayList<String> othermessage){
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
        public View getView(int p, View recycled, ViewGroup parent)
        {
            View thisRow = recycled;

            if(recycled == null)
                thisRow = getLayoutInflater().inflate(R.layout.foreignexchangeapilist, null);

            TextView itemText = thisRow.findViewById(R.id.api  );
            itemText.setText( "Array at:" + p + " is " + getItem(p) );


            return thisRow;
        }
    }

}

