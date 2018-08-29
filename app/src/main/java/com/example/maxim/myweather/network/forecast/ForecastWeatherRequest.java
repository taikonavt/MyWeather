package com.example.maxim.myweather.network.forecast;

import com.example.maxim.myweather.network.today.Clouds;
import com.example.maxim.myweather.network.today.Coord;
import com.example.maxim.myweather.network.today.Main;
import com.example.maxim.myweather.network.today.Sys;
import com.example.maxim.myweather.network.today.Weather;
import com.example.maxim.myweather.network.today.Wind;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maxim on 05.08.18.
 */

public class ForecastWeatherRequest {
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private float message;
    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private List[] list;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public float getMessage() {
        return message;
    }

    public void setMessage(float message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List[] getList() {
        return list;
    }

    public void setList(List[] list) {
        this.list = list;
    }
}
