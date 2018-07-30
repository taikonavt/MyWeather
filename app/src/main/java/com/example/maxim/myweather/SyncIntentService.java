package com.example.maxim.myweather;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SyncIntentService extends IntentService{

    public SyncIntentService() {
        super("MyWeatherSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MyWeatherSyncTask.syncWeather(this);
    }
}
