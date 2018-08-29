package com.example.maxim.myweather.network;

import com.example.maxim.myweather.network.forecast.ForecastWeatherRequest;
import com.example.maxim.myweather.network.today.TodayWeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maxim on 05.08.18.
 */

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<TodayWeatherRequest> loadTodayWeather(
            @Query("id") long locationId,
            @Query("units") String units,
            @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<TodayWeatherRequest> loadTodayWeather(
            @Query("lat") float coordLat,
            @Query("lon") float coordLon,
            @Query("units") String units,
            @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<TodayWeatherRequest> loadTodayWeather(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("appid") String keyApi);

    @GET("data/2.5/forecast/daily")
    Call<ForecastWeatherRequest> loadForecastWeather(
            @Query("id") long locationId,
            @Query("units") String units,
            @Query("cnt") int cnt,
            @Query("appid") String keyApi);

    @GET("data/2.5/forecast/daily")
    Call<ForecastWeatherRequest> loadForecastWeather(
            @Query("lat") float coordLat,
            @Query("lon") float coordLon,
            @Query("units") String units,
            @Query("cnt") int cnt,
            @Query("appid") String keyApi);
}
