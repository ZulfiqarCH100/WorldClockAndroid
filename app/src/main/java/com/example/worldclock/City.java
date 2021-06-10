package com.example.worldclock;

import android.util.Log;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class City implements Serializable {
    String name;
    TimeZone time;
    String timezone, currentTime;
    boolean subscribed;

    public City(String n, String t) {name = n; time = TimeZone.getTimeZone(t); subscribed = false; timezone = t; currentTime = new String(); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeZone getTime() {
        return time;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public int returnSubscribed(){
        if (subscribed == true)
            return 1;
        return 0;
    }

    public void updateTime(DateFormat dateFormat){
        Calendar cal = Calendar.getInstance(this.time);
        dateFormat.setTimeZone(cal.getTimeZone());
        currentTime = (dateFormat.format(cal.getTime()));
        Log.d("Boop", "Thread Running");
    }

    public void test(){
        Log.d("Wow", "I am a city with name" + name);
    }

    public String getCurrentTime(){
        return currentTime;
    }
}
