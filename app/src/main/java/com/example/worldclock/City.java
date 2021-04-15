package com.example.worldclock;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;

public class City implements Serializable {
    String name;
    TimeZone time;
    boolean subscribed;

    public City(String n, String t) {name = n; time = TimeZone.getTimeZone(t); subscribed = false;}

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
}
