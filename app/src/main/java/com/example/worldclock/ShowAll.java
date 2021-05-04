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
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allCities = new ArrayList<>();
        temp = new ArrayList<>();
        db = new SQLite(getApplicationContext());
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
        //allCities = (ArrayList<City>) intent.getSerializableExtra("SelectMore");
        allCities = db.load(false);
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

    @Override
    public void onBackPressed() {
        prepareResult();
        super.onBackPressed();
    }

    public void prepareResult(){
        Intent intent = new Intent();
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
                    //Save the change in db
                    if(isChecked)
                        db.saveCity(t.name);
                    else
                        db.deleteCity(t.name);
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
    }
}