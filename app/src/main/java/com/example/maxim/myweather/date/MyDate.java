package com.example.maxim.myweather.date;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by maxim on 15.09.18.
 */

public class MyDate{
    private long millis;
    private static final int MILLIS_IN_SEC = 1000;
    private Calendar calendar;

    public MyDate(long sec){
        millis = sec * MILLIS_IN_SEC;
        // TODO: 15.09.18 make language
        Locale locale = new Locale("ru");
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), locale);
        calendar.setTimeInMillis(millis);
    }

    public String getMonth(){
        return new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];
    }

    public String getDayOfMonth(){
        return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getDayOfWeek(){
        return new DateFormatSymbols().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];
    }
}