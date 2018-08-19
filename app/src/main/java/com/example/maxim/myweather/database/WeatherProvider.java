package com.example.maxim.myweather.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.maxim.myweather.utils.DateUtils;

public class WeatherProvider extends ContentProvider {
    private DatabaseHelper dbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static final int CODE_LOCATION = 100;
    public static final int CODE_TODAY_WEATHER = 200;
    public static final int CODE_TODAY_WEATHER_FOR_LOCATION = 201;
    public static final int CODE_FORECAST_WEATHER = 300;
    public static final int CODE_FORECAST_WEATHER_FOR_LOCATION = 301;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority, Contract.PATH_LOCATION + "/*", CODE_LOCATION);
        matcher.addURI(authority, Contract.PATH_TODAY, CODE_TODAY_WEATHER);
        matcher.addURI(authority, Contract.PATH_FORECAST, CODE_FORECAST_WEATHER);
//        matcher.addURI(authority, Contract.PATH_TODAY + "/#", CODE_TODAY_WEATHER_FOR_LOCATION);
//        matcher.addURI(authority, Contract.PATH_FORECAST + "/#", CODE_FORECAST_WEATHER_FOR_LOCATION);
        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case CODE_LOCATION:{
                cursor = dbHelper.getReadableDatabase().query(
                        Contract.LocationEntry.TABLE_NAME,
                        columns,
                        selection + " = ?",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CODE_TODAY_WEATHER:{
                String location = uri.getLastPathSegment();
                String[] args = new String[] {location};
                cursor = dbHelper.getReadableDatabase().query(
                        Contract.TodayWeatherEntry.TABLE_NAME,
                        columns,
                        selection + " = ?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CODE_FORECAST_WEATHER:{
                String location = uri.getLastPathSegment();
                String[] args = new String[] {location};
                cursor = dbHelper.getReadableDatabase().query(
                        Contract.ForecastWeatherEntry.TABLE_NAME,
                        columns,
                        selection + " = ?",
                        args,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case CODE_FORECAST_WEATHER: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long weatherDate =
                                value.getAsLong(Contract.ForecastWeatherEntry.COLUMN_DATE);
                        if (!DateUtils.isDateNormalized(weatherDate)) {
                            throw new IllegalArgumentException("Date must be normalized to insert");
                        }
                        long id = db.insert(Contract.ForecastWeatherEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long numRowsInserted = 0;

        switch (uriMatcher.match(uri)){
            case CODE_LOCATION:{
                numRowsInserted = db.insert(
                        Contract.LocationEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                break;
            }
            case CODE_TODAY_WEATHER:{
                numRowsInserted = db.insert(
                        Contract.TodayWeatherEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                break;
            }
        }
        if (numRowsInserted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numRowsDeleted = 0;

        switch (uriMatcher.match(uri)){
            case CODE_LOCATION:{
                numRowsDeleted = db.delete(
                        Contract.LocationEntry.TABLE_NAME,
                        selection + " = ?",
                        selectionArgs
                );
                break;
            }
            case CODE_TODAY_WEATHER:{
                numRowsDeleted = db.delete(
                        Contract.TodayWeatherEntry.TABLE_NAME,
                        selection + " = ?",
                        selectionArgs
                );
                break;
            }
            case CODE_FORECAST_WEATHER:{
                numRowsDeleted = db.delete(
                        Contract.ForecastWeatherEntry.TABLE_NAME,
                        selection + " = ?",
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numRowsUpdated = 0;

        switch (uriMatcher.match(uri)){
            case CODE_TODAY_WEATHER:{
                numRowsUpdated = db.update(
                        Contract.TodayWeatherEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            }
            case CODE_FORECAST_WEATHER:{
                numRowsUpdated = db.update(
                        Contract.ForecastWeatherEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (numRowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsUpdated;
    }
}
