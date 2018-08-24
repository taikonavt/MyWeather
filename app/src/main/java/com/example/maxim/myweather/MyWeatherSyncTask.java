package com.example.maxim.myweather;

import android.content.Context;

import java.net.URL;

public class MyWeatherSyncTask {

    synchronized public static void syncWeather(Context context){

        try {
//            URL weatherRequestUrl = NetworkUtils.getUrl(context);
//            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            // TODO: 30.07.18 get data from json
            // TODO: 30.07.18 delete old data from DB
            // TODO: 30.07.18 insert new data to DB
            // TODO: 30.07.18 set time of data update


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
