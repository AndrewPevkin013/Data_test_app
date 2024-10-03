package com.example.datatestapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Items {

    private String mName;
    public Items() {}


    public void setName(String name) {
        mName = name;
    }


    public String getDateTime() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String dateTime = df.format(today);
        return dateTime;
    }


    public String getName() {
            return "PumpkinEater69";
    }
}
