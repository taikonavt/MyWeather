package com.example.maxim.myweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.maxim.myweather.database.Contract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocationEntry.COLUMN_LOCATION_ID + " INTEGER UNIQUE NOT NULL, " +
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_TODAY_LAST_UPDATE + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_FORECAST_LAST_UPDATE + " INTEGER NOT NULL" +
                ");"
        );
        db.execSQL("CREATE TABLE " + TodayWeatherEntry.TABLE_NAME + " (" +
                TodayWeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodayWeatherEntry.COLUMN_LOCATION_ID + " INTEGER  NOT NULL, " +
                TodayWeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                TodayWeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                TodayWeatherEntry.COLUMN_TEMPERATURE + " REAL NOT NULL, " +
                TodayWeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                TodayWeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                TodayWeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                TodayWeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +
                " FOREIGN KEY (" + TodayWeatherEntry.COLUMN_LOCATION_ID + ") " +
                " REFERENCES " + LocationEntry.TABLE_NAME +
                " (" + LocationEntry.COLUMN_LOCATION_ID + ") " +
                ");"
        );
        db.execSQL("CREATE TABLE " + ForecastWeatherEntry.TABLE_NAME + " (" +
                ForecastWeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ForecastWeatherEntry.COLUMN_LOCATION_ID + " INTEGER UNIQUE, " +
                ForecastWeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                ForecastWeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                ForecastWeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                ForecastWeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                ForecastWeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                ForecastWeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                ForecastWeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                ForecastWeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                ForecastWeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +
                " FOREIGN KEY (" + ForecastWeatherEntry.COLUMN_LOCATION_ID + ") " +
                " REFERENCES " + LocationEntry.TABLE_NAME +
                " (" + LocationEntry.COLUMN_LOCATION_ID + ") " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodayWeatherEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ForecastWeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}