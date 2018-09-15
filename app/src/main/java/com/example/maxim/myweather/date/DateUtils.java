package com.example.maxim.myweather.date;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by maxim on 19.08.18.
 */

public class DateUtils {
    public static boolean isDateNormalized(long millisSinceEpoch){
        // TODO: 19.08.18 isDateNormalized
        return true;
    }

    public static String getDate (long millis){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(millis);
        String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfWeek = new DateFormatSymbols().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];

        return day + " " + month + " " + dayOfWeek;
    }
}
