package com.example.maxim.myweather.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maxim on 05.08.18.
 */

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(
            @Query("q") String cityCountry,
            @Query("units") String units,
            @Query("appid") String keyApi);
}
