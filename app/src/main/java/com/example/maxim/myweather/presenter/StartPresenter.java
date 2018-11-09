package com.example.maxim.myweather.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxim.myweather.model.AppPreferences;
import com.example.maxim.myweather.model.MyModel;
import com.example.maxim.myweather.view.MainActivity;
import com.example.maxim.myweather.view.MyActivity;
import com.example.maxim.myweather.view.StartActivity;

import java.util.concurrent.TimeUnit;

public class StartPresenter implements MyPresenter{
    private StartActivity activity;
    private MyModel model;

    public StartPresenter(){
        this.model = new MyModel(this);
    }

    @Override
    public void attachView(MyActivity activity) {
        this.activity = (StartActivity) activity;
    }

    @Override
    public void viewIsReady() {
        model.startApp();
    }

    @Override
    public void detachView() {

    }

    @Override
    public void destroy() {

    }

    private void startMainActivity(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public Context getAppContext(){
        return activity.getApplicationContext();
    }

    @Override
    public boolean requestLocationPermissions() {
        return false;
    }


}
