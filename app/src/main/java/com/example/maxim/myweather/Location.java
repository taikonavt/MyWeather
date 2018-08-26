package com.example.maxim.myweather;

import com.example.maxim.myweather.database.WeatherProvider;

public class Location {
    private long id;
    private String cityName;
    private String countryName;
    private String coordLat;
    private String coordLong;
    private long todayLastUpdate;
    private long forecastLastUpdate;

    public Location(){
        id = 0;
        cityName = "";
        countryName = "";
        coordLat = "";
        coordLong = "";
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

    public void setLocationId(int id) {
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

    public String getCoordLat() {
        return coordLat;
    }

    public void setCoordLat(String coordLat) {
        this.coordLat = coordLat;
    }

    public String getCoordLon() {
        return coordLong;
    }

    public void setCoordLong(String coordLong) {
        this.coordLong = coordLong;
    }

    public long getTodayLastUpdate() {
        return todayLastUpdate;
    }

    public void setTodayLastUpdate(long todayLastUpdate) {
        this.todayLastUpdate = todayLastUpdate;
    }
}
