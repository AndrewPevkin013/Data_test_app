package com.example.datatestapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Items {

    private String mName;

    public Items() { }

    public void setName(String name) {
        mName = name;
    }


    public String getDateTime() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }


    public String getName() {
        if (mName != null)
            return mName;
        else
            return "PumpkinEater69";
    }
}
