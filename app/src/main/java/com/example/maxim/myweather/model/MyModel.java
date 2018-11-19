package com.example.maxim.myweather.model;

import com.example.maxim.myweather.Place;
import com.example.maxim.myweather.network.forecast.ForecastWeatherRequest;
import com.example.maxim.myweather.network.today.TodayWeatherRequest;
import com.example.maxim.myweather.presenter.MyPresenter;

public class MyModel implements
        CurrentPlaceDefiner.OnLocationChangedListener,
        Network.OnWeatherDataReceiveListener {
    private Network network;
    private MyPresenter presenter;
    private DbMediator dbMediator;
    private MyLoaderManager loaderManager;


    public MyModel(MyPresenter presenter){
        this.presenter = presenter;
        network = new Network(this);
        dbMediator = new DbMediator(this);
        loaderManager = new MyLoaderManager(this);
    }

    public void startApp(){
        CurrentPlaceDefiner definer = new CurrentPlaceDefiner(this);
        definer.updateCurrentLocation();
    }

    MyPresenter getPresenter(){
        return presenter;
    }


    @Override
    public void onLocationChanged(Place place) {
        if (!dbMediator.checkCurrentPlaceIsFresh(place)){
            network.requestTodayWeather(place.getCoordLat(), place.getCoordLon());
            network.requestForecastWeather(place.getCoordLat(), place.getCoordLon());
        }
    }

    @Override
    public void onTodayWeatherDataReceive(TodayWeatherRequest todayWeatherRequest) {
        dbMediator.updateTodayWeather(todayWeatherRequest);
    }

    @Override
    public void onCurrentPlaceDataReceive(TodayWeatherRequest todayWeatherRequest) {
        dbMediator.updateCurrentPlace(todayWeatherRequest);
    }

    @Override
    public void onForecastWeatherDataReceive(ForecastWeatherRequest forecastWeatherRequest) {
        dbMediator.updateForecastWeather(forecastWeatherRequest);
    }

    @Override
    public void onRequestFailure() {
        presenter.showToast("Server connection was failed");
    }

    public void download(long locationId) {
        loaderManager.reloadTodayWeather(locationId);
    }


//    private ArrayList<Place> getFavoriteLocations(){
//        while (placeList.size() > 1){
//            placeList.remove(1);
//        }
//
//        Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI;
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
//            int idIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_PLACE_ID);
//            int cityNameIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_CITY_NAME);
//            int countryIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COUNTRY_NAME);
//            int latitudeIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COORD_LAT);
//            int longitudeIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COORD_LONG);
//            int todayIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_TODAY_LAST_UPDATE);
//            int forecastIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_FORECAST_LAST_UPDATE);
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
