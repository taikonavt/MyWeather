package com.example.maxim.myweather;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private long id;
    private String cityName;
    private String countryName;
    private float coordLat;
    private float coordLong;
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

    protected Place(Parcel in) {
        id = in.readLong();
        cityName = in.readString();
        countryName = in.readString();
        coordLat = in.readFloat();
        coordLong = in.readFloat();
        todayLastUpdate = in.readLong();
        forecastLastUpdate = in.readLong();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public long getForecastLastUpdate() {
        return forecastLastUpdate;
    }

    public void setForecastLastUpdate(long forecastLastUpdate) {
        this.forecastLastUpdate = forecastLastUpdate;
    }

    public long getPlaceId() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(cityName);
        parcel.writeString(countryName);
        parcel.writeFloat(coordLat);
        parcel.writeFloat(coordLong);
        parcel.writeLong(todayLastUpdate);
        parcel.writeLong(forecastLastUpdate);
    }
}
