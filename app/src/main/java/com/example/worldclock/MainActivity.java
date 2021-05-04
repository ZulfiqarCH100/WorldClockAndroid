package com.example.worldclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<City> temp;
    private RecyclerView mRecyclerview;
    private MainAdapter mAdapter; //Only sends as many objects to the RecyclerView as it can view in one screen.
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addMore;
    private Database db;


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
    }

    private void showSavedCities(){
        temp.clear();
        temp = db.load(true);
        mAdapter.changeList(temp);
        mAdapter.notifyDataSetChanged();
    }

    //Will only run when the application is run for the very first time.
    public void setUpDatabase(){
        ArrayList<City> t = new ArrayList<>();
        t.add(new City("Dubai", "Asia/Dubai"));
        t.add(new City("Hong Kong", "Asia/Hong_Kong"));
        t.add(new City("Sydney", "Australia/Sydney"));
        t.add(new City("Beijing", "Australia/Lord_Howe"));
        t.add(new City("Tokyo", "Asia/Tokyo"));
        t.add(new City("Paris", "Europe/Paris"));
        t.add(new City("Calcutta", "Asia/Calcutta"));
        t.add(new City("London", "Europe/London"));
        t.add(new City("Madrid", "Europe/Madrid"));
        t.add(new City("Multan", "Asia/Karachi"));
        t.add(new City("Kiev", "Europe/Kiev"));
        t.add(new City("Rome", "Europe/Rome"));
        t.add(new City("Cairo", "Africa/Cairo"));
        t.add(new City("Chicago", "America/Chicago"));
        t.add(new City("Montreal", "America/Montreal"));
        t.add(new City("Dhaka", "Asia/Dhaka"));
        t.add(new City("Tehran", "Asia/Tehran"));
        t.add(new City("Athens", "Europe/Athens"));
        t.add(new City("Moscow", "Europe/Moscow"));
        t.add(new City("Dublin", "Europe/Dublin"));
        t.add(new City("Baghdad", "Asia/Baghdad"));

        db.fillDb(t);
        Log.d("Boop", "Database set up");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showSavedCities();
    }

    void showAllCities(){
        Intent intent = new Intent(this, ShowAll.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void buildRecyclerView() {
        mRecyclerview = findViewById(R.id.recyclerView);
        //mRecyclerview.setHasFixedSize(true); //Increases performance as recycler view never changes in size.
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MainAdapter(temp);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }
}