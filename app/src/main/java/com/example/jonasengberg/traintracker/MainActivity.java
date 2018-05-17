package com.example.jonasengberg.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
//TODO: Calendar widget, alarm when app is off
public class MainActivity extends AppCompatActivity {

    private EditText searchOrigin;
    private EditText searchDest;
    private ListView listView;
    GetTrainlist getTimeTables;
    private boolean isEmpty = true;
    ArrayAdapter<String> adapter;
    public static final String EXTRA_MESSAGE = "com.example.jonasengberg.traintracker.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchOrigin = findViewById(R.id.editText1);
        searchDest = findViewById(R.id.editText2);
        listView = (ListView) findViewById(R.id.routeList);
        init();
    }

    //Search for two points and get the timetables for todays departing trains
    private void init() {
        searchDest.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try{
                    if(searchOrigin != null)
                    {
                        updateList();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Enter in format = HKI, TKU, EPO, JNS, etc.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    //Clicking on the list will take you to MapsActivity and -if- the train is running mark the train on the map
    final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ArrayList<String> list = new ArrayList<>();
            list.add(parent.getItemAtPosition(position).toString());
            String allContent = list.get(0);
            list.clear();

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, allContent);
            startActivity(intent);
        }
    };

    //ListView with train info from the API
    private void updateList()
    {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        System.out.println("DATE. " + date);
        if (searchOrigin.getText().toString().length() == 0 || searchDest.getText().toString().length() == 0 ) {
            Toast.makeText(this, "The fields are empty!", Toast.LENGTH_SHORT).show();
        }
        else{
            String url = "https://rata.digitraffic.fi/api/v1/live-trains/station/" + searchOrigin.getText().toString() + "/" + searchDest.getText().toString() + "?departure_date=" + date;
            try
            {
                getTimeTables = new ParseTrainlist().execute(url).get();

                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getTimeTables.getNoteRows());
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(listener);

                searchDest.getText().clear();
                searchOrigin.getText().clear();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

}
