package com.example.maxim.myweather.model;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.maxim.myweather.common.Place;
import com.example.maxim.myweather.common.TodayWeather;
import com.example.maxim.myweather.database.Contract;
import com.example.maxim.myweather.presenter.MainPresenter;

import static com.example.maxim.myweather.view.MainActivity.CLASS;
import static com.example.maxim.myweather.view.MainActivity.TAG;

public class MyLoaderManager implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CURRENT_PLACE_LOADER_ID = 1111;
    private static final int FAVOURITE_PLACES_LOADER_ID = 2222;
    private static final int TODAY_WEATHER_LOADER_ID = 3333;
    private static final int FORECAST_WEATHER_LOADER_ID = 4444;

    private boolean isFirstLoading = true;
    private MainPresenter presenter;

    MyLoaderManager(MyModel model){
        this.presenter = (MainPresenter) model.getPresenter();
        AppCompatActivity activity = presenter.getActivity();
        activity.getSupportLoaderManager().initLoader(CURRENT_PLACE_LOADER_ID, null, this);
    }

    private void initAnotherLoaders(){
        AppCompatActivity activity = presenter.getActivity();
        activity.getSupportLoaderManager().initLoader(FAVOURITE_PLACES_LOADER_ID, null, this);
        activity.getSupportLoaderManager().initLoader(TODAY_WEATHER_LOADER_ID, null, this);
        activity.getSupportLoaderManager().initLoader(FORECAST_WEATHER_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        switch (i){
            case CURRENT_PLACE_LOADER_ID:{
                Uri uri = Contract.CurrentPlaceEntry.CONTENT_URI;
                return new CursorLoader(
                        presenter.getActivity(),
                        uri,
                        null,
                        null,
                        null,
                        null
                );
            }
            case FAVOURITE_PLACES_LOADER_ID:{
                Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI;
                return new CursorLoader(
                        presenter.getActivity(),
                        uri,
                        null,
                        null,
                        null,
                        null
                );
            }
            case TODAY_WEATHER_LOADER_ID:{
                Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
                String selection = Contract.TodayWeatherEntry.COLUMN_PLACE_ID;
                String displayingPlaceId = Long.toString(presenter.getDisplayingPlace().getPlaceId());
                String[] args = new String[] {displayingPlaceId};
                return new CursorLoader(
                        presenter.getActivity(),
                        uri,
                        null,
                        selection,
                        args,
                        null
                );
            }
            case FORECAST_WEATHER_LOADER_ID:{
                Uri uri = Contract.ForecastWeatherEntry.CONTENT_URI;
                String selection = Contract.ForecastWeatherEntry.COLUMN_PLACE_ID;
                String displayingPlaceId = Long.toString(presenter.getDisplayingPlace().getPlaceId());
                String[] args = new String[] {displayingPlaceId};
                return new CursorLoader(
                        presenter.getActivity(),
                        uri,
                        null,
                        selection,
                        args,
                        null
                );
            }
            default:
                throw new RuntimeException("Loader not implemented: " + i);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id) {
            case CURRENT_PLACE_LOADER_ID:{
                int placeIdIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_PLACE_ID);
                int cityNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_CITY_NAME);
                int countryNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COUNTRY_NAME);
                int coordLatIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LAT);
                int coordLonIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LONG);
                int todayLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_TODAY_LAST_UPDATE);
                int forecastLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_FORECAST_LAST_UPDATE);

                Place place = new Place();
                if (cursor.moveToFirst()) {
                    place.setLocationId(cursor.getInt(placeIdIndex));
                    place.setCityName(cursor.getString(cityNameIndex));
                    place.setCountryName(cursor.getString(countryNameIndex));
                    place.setCoordLat(cursor.getFloat(coordLatIndex));
                    place.setCoordLong(cursor.getFloat(coordLonIndex));
                    place.setTodayLastUpdate(cursor.getLong(todayLastUpdateIndex));
                    place.setForecastLastUpdate(cursor.getLong(forecastLastUpdateIndex));
                }
                if (isFirstLoading) {
                    presenter.setDisplayingPlace(place);
                    isFirstLoading = false;
                }
                presenter.updateCurrentPlace(place);
                initAnotherLoaders();
                break;
            }
            case FAVOURITE_PLACES_LOADER_ID:{
                int placeIdIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_PLACE_ID);
                int cityNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_CITY_NAME);
                int countryNameIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COUNTRY_NAME);
                int coordLatIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LAT);
                int coordLonIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_COORD_LONG);
                int todayLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_TODAY_LAST_UPDATE);
                int forecastLastUpdateIndex = cursor.getColumnIndex(Contract.CurrentPlaceEntry.COLUMN_FORECAST_LAST_UPDATE);

                Place[] places = new Place[cursor.getCount()];
                if (cursor.moveToFirst()) {
                    int i = 0;
                    do {
                        places[i].setLocationId(cursor.getInt(placeIdIndex));
                        places[i].setCityName(cursor.getString(cityNameIndex));
                        places[i].setCountryName(cursor.getString(countryNameIndex));
                        places[i].setCoordLat(cursor.getFloat(coordLatIndex));
                        places[i].setCoordLong(cursor.getFloat(coordLonIndex));
                        places[i].setTodayLastUpdate(cursor.getLong(todayLastUpdateIndex));
                        places[i].setForecastLastUpdate(cursor.getLong(forecastLastUpdateIndex));
                        i++;
                    } while (cursor.moveToNext());
                }
                presenter.updateFavouritePlaces(places);
                break;
            }
            case TODAY_WEATHER_LOADER_ID:{
                int placeIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PLACE_ID);
                int weatherIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID);
                int shortDescrIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC);
                int tempIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE);
                int humidityIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_HUMIDITY);
                int pressureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PRESSURE);
                int windIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED);
                int degreesIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_DEGREES);

                TodayWeather todayWeather = new TodayWeather();
                if (cursor.moveToFirst()) {
                    todayWeather.setPlaceId(cursor.getLong(placeIdIndex));
                    todayWeather.setWeatherId(cursor.getInt(weatherIdIndex));
                    todayWeather.setShortDescr(cursor.getString(shortDescrIndex));
                    todayWeather.setTemp(cursor.getFloat(tempIndex));
                    todayWeather.setHumidity(cursor.getFloat(humidityIndex));
                    todayWeather.setPressure(cursor.getFloat(pressureIndex));
                    todayWeather.setWindSpeed(cursor.getFloat(windIndex));
                    todayWeather.setDegrees(cursor.getFloat(degreesIndex));
                }
                presenter.updateTodayWeather(todayWeather);
                break;
            }
            case FORECAST_WEATHER_LOADER_ID:{
                presenter.updateForecastWeather(cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();
        switch (id){
            case CURRENT_PLACE_LOADER_ID:{
                presenter.updateCurrentPlace(new Place());
                break;
            }
            case FAVOURITE_PLACES_LOADER_ID:{
                presenter.updateFavouritePlaces(new Place[0]);
            }
        }
    }

    void reloadWeather() {
        AppCompatActivity activity = presenter.getActivity();
        activity.getSupportLoaderManager().restartLoader(TODAY_WEATHER_LOADER_ID, null, this);
        activity.getSupportLoaderManager().restartLoader(FORECAST_WEATHER_LOADER_ID, null, this);
    }
}
