package com.wyksofts.saveone.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CurrentDay {

    //current day and time
    String currentTime;
    String date;

    public String getCurrentTime(){
        //get current time
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        currentTime = dateFormat.format(time);

        return currentTime;
    }

    public String getCurrentDate(){

        //get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        date = df.format(c);

        return date;

    }

}
