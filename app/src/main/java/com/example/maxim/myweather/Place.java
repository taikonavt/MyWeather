package com.example.maxim.myweather;

import com.example.maxim.myweather.database.WeatherProvider;

public class Place {
    private long id;
    private String cityName;
    private String countryName;
    private Float coordLat;
    private Float coordLong;
    private long todayLastUpdate;
    private long forecastLastUpdate;

    public Place(){
        id = 0;
        cityName = "";
        countryName = "";
        coordLat = 0.0f;
        coordLong = 0.0f;
        todayLastUpdate = 0;
        forecastLastUpdate = 0;
        }

    public long getForecastLastUpdate() {
        return forecastLastUpdate;
    }

    public void setForecastLastUpdate(long forecastLastUpdate) {
        this.forecastLastUpdate = forecastLastUpdate;
    }

    public long getLocationId() {
        return id;
    }

    public void setLocationId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public float getCoordLat() {
        return coordLat;
    }

    public void setCoordLat(float coordLat) {
        this.coordLat = coordLat;
    }

    public float getCoordLon() {
        return coordLong;
    }

    public void setCoordLong(float coordLong) {
        this.coordLong = coordLong;
    }

    public long getTodayLastUpdate() {
        return todayLastUpdate;
    }

    public void setTodayLastUpdate(long todayLastUpdate) {
        this.todayLastUpdate = todayLastUpdate;
    }
}
