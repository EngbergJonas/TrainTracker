package com.example.jonasengberg.traintracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateFormatter {

    private static final long HOUR = 3600*1000;

    public String formatDate(String dateString)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedString = "";
        try {
            Date date = format.parse(dateString);
            Date newDate = new Date(date.getTime() + 3 * HOUR);
            formattedString = dateFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedString;
    }
}
