package com.example.worldclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class SQLite implements Database {
    private Context context;
    public SQLite(Context ctx){ context = ctx; }

    //Implementations for the interface functions.
    @Override
    public void saveCity(String name) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = "UPDATE Cities SET Subscribed = 1 WHERE Name = '" + name + "'";
        db.execSQL(query);
        Log.d("Boop", name + " Saved");
    }

    @Override
    public void deleteCity(String name) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = "UPDATE Cities SET Subscribed = 0 WHERE Name = '" + name + "'";
        db.execSQL(query);
        Log.d("Boop", name + " Deleted");
    }


    @Override
    public ArrayList<City> load(boolean selectedOnly) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c;
        if (selectedOnly)
            c = db.rawQuery("SELECT * FROM Cities WHERE Subscribed = 1", null); //Favourite cities.
        else
            c = db.rawQuery("SELECT * FROM Cities", null); //All cities.

        ArrayList<City> temp = new ArrayList<>();
        while(c.moveToNext()){
            City read = new City(c.getString(c.getColumnIndex("Name")), c.getString(c.getColumnIndex("TimeZone")));
            int subscribed = c.getInt(c.getColumnIndex("Subscribed"));
            if (subscribed == 1)
                read.setSubscribed(true);
            else
                read.setSubscribed(false);
            temp.add(read);
        }
        return temp;
    }

    @Override
    public boolean isDbEmpty() {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Cities", null);
        if (c.getCount() == 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

    @Override
    public void fillDb(ArrayList<City> cities) {
        Log.d("Wow", "Database Call Made");
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        for(int x = 0; x < cities.size(); x++) {
            ContentValues values = new ContentValues();
            values.put("Name", cities.get(x).name);
            values.put("TimeZone", cities.get(x).timezone);
            values.put("Subscribed", cities.get(x).returnSubscribed());
            db.insertWithOnConflict("Cities",null,values,SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
}
