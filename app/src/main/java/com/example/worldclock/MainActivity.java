package com.example.worldclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<City> temp;
    private RecyclerView mRecyclerview;
    private MainAdapter mAdapter; //Only sends as many objects to the RecyclerView as it can view in one screen.
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addMore;
    private Database db;
    private Handler mainHandler = new Handler(); //Since the handler is initalized in the main thread it puts work in the message queue of the main thread.
    private volatile boolean stopThread = false; //Used to stop a running thread. Volatile means a thread only reads the most up to date version of this variable.

    final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMore = findViewById(R.id.addMore); //Our button
        temp = new ArrayList<>();

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllCities();
            }
        });
        buildRecyclerView();

        //Setting up database
        db = new SQLite(getApplicationContext());
        if (db.isDbEmpty())
            setUpDatabase();
        showSavedCities();

        startThread();
    }

    private void startThread(){
        stopThread = false;
        Runnable timeUpdater = new TimeUpdater(1000);
        new Thread(timeUpdater).start();
    }

    private void showSavedCities() {
        temp.clear();
        temp = db.load(true);
        mAdapter.changeList(temp);
        mAdapter.notifyDataSetChanged();
    }

    //Will only run when the application is run for the very first time.
    public void setUpDatabase() {
        //Connecting to API
        Toast.makeText(getApplicationContext(), "Contacting Api and Building Database", Toast.LENGTH_LONG).show();
        Runnable json = new ApiThread();
        new Thread(json).start();
        Toast.makeText(getApplicationContext(), "All Done :)", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showSavedCities();
        startThread();
    }

    void showAllCities() {
        Intent intent = new Intent(this, ShowAll.class);
        startActivityForResult(intent, REQUEST_CODE);
        stopThread = true;
    }


    public void buildRecyclerView() {
        mRecyclerview = findViewById(R.id.recyclerView);
        //mRecyclerview.setHasFixedSize(true); //Increases performance as recycler view never changes in size.
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainAdapter(temp);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }


    //Implementing my thread class.
    class TimeUpdater implements Runnable {
        int sleepTime;

        TimeUpdater(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            while (stopThread == false){
                if (temp.size() != 0){
                    for(int x = 0; x < temp.size(); x++)
                        temp.get(x).updateTime(dateFormat);
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ApiThread implements Runnable{
        ArrayList<City> cities;

        ApiThread(){cities = new ArrayList<>();}

        @Override
        public void run() {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.timezonedb.com/v2.1/list-time-zone?key=AAAKZ8GJTJH5&format=json&fields=zoneName";
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray zones = jsonObj.getJSONArray("zones");

                    // looping through All Contacts.
                    for (int i = 0; i < zones.length(); i++) {
                        JSONObject c = zones.getJSONObject(i);
                        String zoneName = c.getString("zoneName");
                        String cityName = zoneName.substring(zoneName.indexOf('/') + 1);
                        if (cityName.indexOf('/') == -1) {
                            if (cityName.indexOf('_') != -1)
                                cityName = cityName.replace('_', ' ');
                            cities.add(new City(cityName, zoneName));
                        }
                    }
                    db.fillDb(cities);
                } catch (final JSONException e) {
                    Log.e("dont care", "Json parsing error: " + e.getMessage());
                }
            }
        }
    }
}