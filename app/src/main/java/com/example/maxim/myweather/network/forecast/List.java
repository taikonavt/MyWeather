package com.example.maxim.myweather.network.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {
    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("pressure")
    @Expose
    private float pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("weather")
    @Expose
    private Weather[] weather;
    @SerializedName("speed")
    @Expose
    private float speed;
    @SerializedName("deg")
    @Expose
    private int deg;
    @SerializedName("clouds")
    @Expose
    private int clouds;
    @SerializedName("rain")
    @Expose
    private float rain;


    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }
}
