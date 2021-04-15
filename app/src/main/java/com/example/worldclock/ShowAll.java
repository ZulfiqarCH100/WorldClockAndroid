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
    ArrayList<City> temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCities = new ArrayList<>();
        setContentView(R.layout.show_all);

        Intent intent = getIntent();
        allCities.clear();
        allCities = (ArrayList<City>) intent.getSerializableExtra("SelectMore");
        if(allCities.size() == 0)
            populateCities(allCities);
        temp = allCities;
        buildRecyclerView();

        EditText searchText = findViewById(R.id.search);;
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
        //Input all cities.
        t.add(new City("Islamabad", "Africa/Timbuktu"));
        t.add(new City("Karachi", "Atlantic/Canary"));
        t.add(new City("Tokyo", "Europe/Tiraspol"));
        t.add(new City("Beijing", "Australia/Lord_Howe"));
        t.add(new City("Lahore", "Indian/Mahe"));
        t.add(new City("Paris", "Asia/Baghdad"));
        t.add(new City("Mumbai", "Africa/Johannesburg"));
        t.add(new City("London", "Asia/Karachi"));
        t.add(new City("Blep Bloop", "Asia/Thimbu"));
        t.add(new City("Multan", "Asia/Krasnoyarsk"));
        t.add(new City("Out of Cities", "Asia/Kashgar"));
        t.add(new City("Argentina", "Singapore"));
        t.add(new City("Sirf Tina", "Australia/Adelaide"));
        t.add(new City("Non Urgent Tina", "Australia/NSW"));
        t.add(new City("Spain", "Pacific/Truk"));
        t.add(new City("Madrid", "Pacific/Tarawa"));
        t.add(new City("Barcelona", "Pacific/Kiritimati"));
        t.add(new City("Rainbow Road", "Asia/Seoul"));
        t.add(new City("Mera Ghar", "Asia/Seoul"));
        t.add(new City("Should Really use a file for this", "Asia/Seoul"));
        t.add(new City("Dun", "Asia/Seoul"));
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
        mRecyclerview.setHasFixedSize(true); //Increases performance as recycler view never changes in size.
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