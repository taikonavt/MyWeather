package com.example.maxim.myweather.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maxim on 05.08.18.
 */

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadTodayWeather(
            @Query("id") long locationId,
            @Query("units") String units,
            @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadTodayWeather(
            @Query("lat") String coordLat,
            @Query("lon") String coordLon,
            @Query("units") String units,
            @Query("appid") String keyApi);
}
