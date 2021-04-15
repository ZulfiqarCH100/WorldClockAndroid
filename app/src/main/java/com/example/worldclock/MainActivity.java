package com.example.worldclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<City> list;
    private ArrayList<City> temp;
    private RecyclerView mRecyclerview;
    private MainAdapter mAdapter; //Only sends as many objects to the RecyclerView as it can view in one screen.
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addMore;

    final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMore = findViewById(R.id.addMore); //Our button
        list = new ArrayList<>();
        temp = new ArrayList<>();

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllCities();
            }
        });
        buildRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        list = (ArrayList<City>) data.getSerializableExtra("Subscribed");
        temp.clear();
        for (int x = 0; x < list.size(); x++){
            if (list.get(x).isSubscribed())
                temp.add(list.get(x));
        }
        mAdapter.notifyDataSetChanged();
    }

    void showAllCities(){
        Intent intent = new Intent(this, ShowAll.class);
        intent.putExtra("SelectMore", list);
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