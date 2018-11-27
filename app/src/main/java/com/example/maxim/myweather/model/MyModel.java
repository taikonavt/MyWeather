package com.example.maxim.myweather.model;

import android.net.Uri;

import com.example.maxim.myweather.common.Place;
import com.example.maxim.myweather.database.Contract;
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
    }

    public void updateCurrentPlace(){
        CurrentPlaceDefiner definer = new CurrentPlaceDefiner(this);
        definer.updateCurrentLocation();
    }

    public void initLoader() {
        loaderManager = new MyLoaderManager(this);
    }

    MyPresenter getPresenter(){
        return presenter;
    }

    @Override
    public void setCurrentPlace(Place place) {
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

    public void updateWeatherForDisplayingPlace(Place place) {
        network.requestTodayWeather(place.getPlaceId());
        network.requestForecastWeather(place.getPlaceId());
        loaderManager.reloadWeather();
    }

    public void deleteFavouritePlace(long placeId){
        Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI;
        String selection = Contract.FavouritePlaceEntry.COLUMN_PLACE_ID;
        String[] args = new String [] {Long.toString(placeId)};
        presenter.getAppContext().getContentResolver().delete(
                uri,
                selection,
                args);
    }
}
