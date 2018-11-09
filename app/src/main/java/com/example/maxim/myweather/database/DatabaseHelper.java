package com.example.maxim.myweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.maxim.myweather.database.Contract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;
    static final String TAG = "myTag";
    static final String CLASS = DatabaseHelper.class.getSimpleName() + " ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CURRENT_PLACE_TABLE =
                "CREATE TABLE " + FavouritePlaceEntry.TABLE_NAME + " (" +
                        CurrentPlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CurrentPlaceEntry.COLUMN_PLACE_ID + " INTEGER NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_COORD_LONG + " REAL NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_TODAY_LAST_UPDATE + " INTEGER NOT NULL, " +
                        CurrentPlaceEntry.COLUMN_FORECAST_LAST_UPDATE + " INTEGER NOT NULL" +
                        ");";
        db.execSQL(SQL_CREATE_CURRENT_PLACE_TABLE);

        final String SQL_CREATE_FAVOURITE_PLACE_TABLE =
                "CREATE TABLE " + FavouritePlaceEntry.TABLE_NAME + " (" +
                        FavouritePlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouritePlaceEntry.COLUMN_PLACE_ID + " INTEGER NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_COORD_LONG + " REAL NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_TODAY_LAST_UPDATE + " INTEGER NOT NULL, " +
                        FavouritePlaceEntry.COLUMN_FORECAST_LAST_UPDATE + " INTEGER NOT NULL, " +
                        "UNIQUE (" + FavouritePlaceEntry.COLUMN_PLACE_ID + ") ON CONFLICT REPLACE " +
                        ");";
        db.execSQL(SQL_CREATE_FAVOURITE_PLACE_TABLE);

        final String SQL_CREATE_TODAY_WEATHER =
                "CREATE TABLE " + TodayWeatherEntry.TABLE_NAME + " (" +
                        TodayWeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TodayWeatherEntry.COLUMN_LOCATION_ID + " INTEGER  NOT NULL, " +
                        TodayWeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                        TodayWeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                        TodayWeatherEntry.COLUMN_TEMPERATURE + " REAL NOT NULL, " +
                        TodayWeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                        TodayWeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                        TodayWeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                        TodayWeatherEntry.COLUMN_DEGREES + " REAL NOT NULL" +
                        ");";
        db.execSQL(SQL_CREATE_TODAY_WEATHER);

        final String SQL_CREATE_FORECAST_TABLE =
                "CREATE TABLE " + ForecastWeatherEntry.TABLE_NAME + " (" +
                        ForecastWeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ForecastWeatherEntry.COLUMN_LOCATION_ID + " INTEGER NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                        ForecastWeatherEntry.COLUMN_DEGREES + " REAL NOT NULL" +
                        ");";
        db.execSQL(SQL_CREATE_FORECAST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CurrentPlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavouritePlaceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodayWeatherEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ForecastWeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}