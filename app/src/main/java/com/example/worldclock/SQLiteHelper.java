package com.example.worldclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String tableName = "Cities.db";
    public static final int version = 1;

    public SQLiteHelper(Context context) {
        super(context, tableName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "CREATE TABLE Cities (Name TEXT PRIMARY KEY," + "TimeZone TEXT," + "Subscribed INTEGER)";
        db.execSQL(command);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Cities");
        onCreate(db);
    }
}
