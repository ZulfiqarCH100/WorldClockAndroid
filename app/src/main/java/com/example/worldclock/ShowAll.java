package com.example.worldclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

public class ShowAll extends AppCompatActivity {
    private ArrayList<City> allCities;
    private RecyclerView mRecyclerview;
    private ShowAllAdapter mAdapter; //Only sends as many objects to the RecyclerView as it can view in one screen.
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<City> temp; //Modified for filtering.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCities = new ArrayList<>();
        temp = new ArrayList<>();
        setContentView(R.layout.show_all);
        handleIntent();
        buildRecyclerView();
        handleSearch();
    }

    public void handleSearch(){
        EditText searchText = findViewById(R.id.search);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing to do here.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Nothing to do here.
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void handleIntent(){
        Intent intent = getIntent();
        allCities.clear();
        allCities = (ArrayList<City>) intent.getSerializableExtra("SelectMore");
        if(allCities.size() == 0)
            populateCities(allCities);
        temp = allCities;
    }

    public void filter(String s){
        temp = new ArrayList<>();
        for(City c : allCities){
            if (c.getName().toLowerCase().contains(s.toLowerCase()))
                temp.add(c);
        }
        mAdapter.changeList(temp);
        mAdapter.notifyDataSetChanged();
    }

    public void populateCities(ArrayList<City> t){
        //Input all cities and their respective TimeZones.
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
    }

    @Override
    public void onBackPressed() {
        prepareResult();
        super.onBackPressed();
    }

    public void prepareResult(){
        Intent intent = new Intent();
        intent.putExtra("Subscribed", allCities);
        setResult(RESULT_OK, intent);
    }

    public void buildRecyclerView() {
        mRecyclerview = findViewById(R.id.recyclerView2);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShowAllAdapter(temp);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        //Subscribing to the click listeners by implementing the interface.
        mAdapter.setOnItemClickListener(new ShowAllAdapter.onItemClickListener() {
            @Override
            public void subscribeChange(int position, boolean isChecked) {
                City t = temp.get(position);
                if (t.isSubscribed() != isChecked) { //This if is needed to filter unnecessary calls to the function when we scroll.
                    t.setSubscribed(isChecked);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
    }
}