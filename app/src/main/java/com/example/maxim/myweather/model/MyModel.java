package com.example.maxim.myweather.model;

import com.example.maxim.myweather.presenter.MyPresenter;
import com.example.maxim.myweather.view.MyActivity;

public class MyModel {
    private Network network;
    private AppPreferences preferences;
    private MyActivity activity;
    private MyPresenter presenter;


    public MyModel(MyPresenter presenter){
        this.presenter = presenter;
        network = new Network(presenter.getAppContext());
    }



//    private ArrayList<Place> getFavoriteLocations(){
//        while (placeList.size() > 1){
//            placeList.remove(1);
//        }
//
//        Uri uri = Contract.LocationEntry.CONTENT_URI;
//
//        Cursor cursor = getContentResolver().query(
//                uri,
//                null,
//                null,
//                null,
//                null);
//
//        int i = 1;
//        if (cursor.moveToFirst()){
//            int idIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_LOCATION_ID);
//            int cityNameIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_CITY_NAME);
//            int countryIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COUNTRY_NAME);
//            int latitudeIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LAT);
//            int longitudeIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LONG);
//            int todayIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE);
//            int forecastIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE);
//
//            do {
//                Place place = new Place();
//                place.setLocationId(cursor.getInt(idIndex));
//                place.setCityName(cursor.getString(cityNameIndex));
//                place.setCountryName(cursor.getString(countryIndex));
//                place.setCoordLat(cursor.getFloat(latitudeIndex));
//                place.setCoordLat(cursor.getFloat(longitudeIndex));
//                place.setTodayLastUpdate(cursor.getLong(todayIndex));
//                place.setForecastLastUpdate(cursor.getLong(forecastIndex));
//                placeList.add(i, place);
//                i++;
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
}
