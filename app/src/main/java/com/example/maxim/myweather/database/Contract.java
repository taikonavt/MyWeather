package com.example.maxim.myweather.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.maxim.myweather";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOCATION = "location";
    public static final String PATH_COUNTRY = "country";
    public static final String PATH_FORECAST = "forecast";
    public static final String PATH_TODAY = "today";

    public static final class LocationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_LOCATION)
                .build();
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_CITY_CODE = "city_code";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_COUNTRY_ID = "country_id";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
        public static final String COLUMN_TODAY_LAST_UPDATE = "today_last_update";
        public static final String COLUMN_FORECAST_LAST_UPDATE = "forecast_last_update";
    }

    public static final class CountryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COUNTRY)
                .build();
        public static final String TABLE_NAME = "country";
        public static final String COLUMN_COUNTRY_NAME = "country_name";
    }

    public static final class TodayWeather implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TODAY)
                .build();
        public static final String TABLE_NAME = "today_weather";
        public static final String COLUMN_LOCATION_ID = "location_id";
    }

    public static final class ForecastWeatherEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FORECAST)
                .build();
        public static final String TABLE_NAME = "forecast_weather";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_description";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";
    }
}
