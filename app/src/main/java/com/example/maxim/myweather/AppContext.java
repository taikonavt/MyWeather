package com.example.maxim.myweather;

import android.app.Application;

public class AppContext extends Application{

    private static AppContext instance;

    private AppContext(){
        instance = this;
    }

    public static AppContext getInstance(){
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }
}
