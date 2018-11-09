package com.example.maxim.myweather.presenter;

import android.content.Context;

import com.example.maxim.myweather.view.MyActivity;

public interface MyPresenter {

    void attachView(MyActivity activity);

    void viewIsReady();

    void detachView();

    void destroy();

    Context getAppContext();

    boolean requestLocationPermissions();
}
