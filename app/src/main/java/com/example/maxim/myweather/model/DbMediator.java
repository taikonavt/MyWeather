package com.example.maxim.myweather.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.maxim.myweather.Place;
import com.example.maxim.myweather.database.Contract;
import com.example.maxim.myweather.network.forecast.ForecastWeatherRequest;
import com.example.maxim.myweather.network.today.TodayWeatherRequest;

public class DbMediator {
    private MyModel model;
    private ContentResolver resolver;

    DbMediator(MyModel model){
        this.model = model;
        resolver = model.getPresenter()
                .getAppContext()
                .getContentResolver();
    }

    boolean checkCurrentPlaceIsFresh(Place place){
        Uri uri = Contract.CurrentPlaceEntry.CONTENT_URI;

        Cursor cursor = resolver.query(
                uri,
                null,
                null,
                null,
                null
        );
        int size = cursor.getCount();
        if (size > 1) {
            String message = Contract.CurrentPlaceEntry.TABLE_NAME + " store more then 1 row";
            throw new IndexOutOfBoundsException(message);
        }
        if (cursor.moveToFirst()){
            int locationIdIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_PLACE_ID);
            int cityNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_CITY_NAME);
            int countryNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COUNTRY_NAME);
            int coordLatIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LAT);
            int coordLonIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LONG);
            int todayLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_TODAY_LAST_UPDATE);
            int forecastLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_FORECAST_LAST_UPDATE);

            float lat = cursor.getFloat(coordLatIndex);
            float lon = cursor.getFloat(coordLonIndex);

            if (place.getCoordLat() == lat && place.getCoordLon() == lon){
                final long HALF_AN_HOUR = 30 * 60 * 1000;
                long currentTime = System.currentTimeMillis();
                long todayLastUpdate = cursor.getLong(todayLastUpdateIndex);
                long dt = currentTime - todayLastUpdate;
                if (dt < HALF_AN_HOUR){
                    return true;
                }
            }
        }
        return false;
    }

    void updateTodayWeather(TodayWeatherRequest todayWeatherRequest) {
        long locationId = todayWeatherRequest.getId();
        int weatherId = todayWeatherRequest.getWeather()[0].getId();
        String shortDescription = todayWeatherRequest.getWeather()[0].getDescription();
        float temperature = todayWeatherRequest.getMain().getTemp();
        float humidity = todayWeatherRequest.getMain().getHumidity();
        float pressure = todayWeatherRequest.getMain().getPressure();
        float windSpeed = todayWeatherRequest.getWind().getSpeed();
        float windDegrees = todayWeatherRequest.getWind().getDeg();

        ContentValues cv = new ContentValues();
        cv.put(Contract.TodayWeatherEntry.COLUMN_PLACE_ID, locationId);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID, weatherId);
        cv.put(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC, shortDescription);
        cv.put(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE, temperature);
        cv.put(Contract.TodayWeatherEntry.COLUMN_HUMIDITY, humidity);
        cv.put(Contract.TodayWeatherEntry.COLUMN_PRESSURE, pressure);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED, windSpeed);
        cv.put(Contract.TodayWeatherEntry.COLUMN_DEGREES, windDegrees);

        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_PLACE_ID;
        String[] selectionArgs = new String[]{Long.toString(locationId)};

        Cursor query = resolver.query(uri, null, selection, selectionArgs, null);
        if (query.moveToFirst()){
            resolver.delete(uri, selection, selectionArgs);
            resolver.insert(uri, cv);
        }
        else {
            Uri u = resolver.insert(uri, cv);
        }
        query.close();
    }

    void updateCurrentPlace(TodayWeatherRequest todayWeatherRequest) {
        Uri uriDelete = Contract.CurrentPlaceEntry.CONTENT_URI;
        resolver.delete(uriDelete, null, null);

        long locationId = todayWeatherRequest.getId();
        String cityName = todayWeatherRequest.getName();
        String countryName = todayWeatherRequest.getSys().getCountry();
        float coordLat = todayWeatherRequest.getCoord().getLat();
        float coordLon = todayWeatherRequest.getCoord().getLon();
        long todayLastUdpate = System.currentTimeMillis();
        long forecastLastUpdate = 0;

        ContentValues cv = new ContentValues();
        cv.put(Contract.CurrentPlaceEntry.COLUMN_PLACE_ID, locationId);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_CITY_NAME, cityName);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_COUNTRY_NAME, countryName);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_COORD_LAT, coordLat);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_COORD_LONG, coordLon);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_TODAY_LAST_UPDATE, todayLastUdpate);
        cv.put(Contract.CurrentPlaceEntry.COLUMN_FORECAST_LAST_UPDATE, forecastLastUpdate);

        resolver.insert(uriDelete, cv);
    }

    void updateForecastWeather (ForecastWeatherRequest forecastWeatherRequest) {
        int cnt = forecastWeatherRequest.getCnt();
        long locationId = forecastWeatherRequest.getCity().getId();
        ContentValues[] values = new ContentValues[cnt];

        long date;
        int weatherId;
        String description;
        float minTemp;
        float maxTemp;
        int humidity;
        float pressure;
        float speed;
        float degrees;

        for (int i = 0; i < cnt; i++) {
            date = forecastWeatherRequest.getList()[i].getDt();
            weatherId = forecastWeatherRequest.getList()[i].getWeather()[0].getId();
            description = forecastWeatherRequest.getList()[i].getWeather()[0].getMain();
            minTemp = forecastWeatherRequest.getList()[i].getTemp().getMin();
            maxTemp = forecastWeatherRequest.getList()[i].getTemp().getMax();
            humidity = forecastWeatherRequest.getList()[i].getHumidity();
            pressure = forecastWeatherRequest.getList()[i].getPressure();
            speed = forecastWeatherRequest.getList()[i].getSpeed();
            degrees = forecastWeatherRequest.getList()[i].getDeg();

            ContentValues cv = new ContentValues();
            cv.put(Contract.ForecastWeatherEntry.COLUMN_PLACE_ID, locationId);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_DATE, date);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_WEATHER_ID, weatherId);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_SHORT_DESC, description);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_MIN_TEMP, minTemp);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_MAX_TEMP, maxTemp);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_HUMIDITY, humidity);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_PRESSURE, pressure);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_WIND_SPEED, speed);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_DEGREES, degrees);

            values[i] = cv;
        }
        String[] stringArgs = new String[] {Long.toString(locationId)};
        resolver.delete(
                Contract.ForecastWeatherEntry.CONTENT_URI,
                Contract.ForecastWeatherEntry.COLUMN_PLACE_ID,
                stringArgs);

        int n = resolver.bulkInsert(Contract.ForecastWeatherEntry.CONTENT_URI, values);
    }
}
