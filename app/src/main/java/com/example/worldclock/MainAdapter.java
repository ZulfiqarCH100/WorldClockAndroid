package com.example.worldclock;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ExampleViewHolder> {
    private ArrayList<City> list;
    private DateFormat dateFormat;
    private onItemClickListener mListener;

    //This interface would be implemented in main activity to get position of the item clicked.
    //The main activity is subscribed to the listeners via this interface.
    public interface onItemClickListener{
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public MainAdapter(ArrayList<City> l) {
        this.list = l;
        dateFormat = new SimpleDateFormat("HH:mm");
    }

    //We cant use a non static class inside a static class so we pass the ExampleViewHolder a onclick listener object.
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView cityName;
        public TextView time;
        public ExampleViewHolder(View itemView, onItemClickListener listener) {
            super(itemView);
           cityName = itemView.findViewById(R.id.subCityName);
           time = itemView.findViewById(R.id.subTime);
        }
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_objects,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    //Sets the values of one Example_object held by holder.
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        City current = list.get(position);
        holder.cityName.setText(current.getName());
        Calendar cal = Calendar.getInstance(current.getTime());
        dateFormat.setTimeZone(cal.getTimeZone());
        holder.time.setText(dateFormat.format(cal.getTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void changeList(ArrayList<City> l){
        list = l;
    }
}
