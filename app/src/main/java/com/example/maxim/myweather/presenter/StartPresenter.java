package com.example.maxim.myweather.presenter;

import android.content.Context;
import android.content.Intent;

import com.example.maxim.myweather.model.MyModel;
import com.example.maxim.myweather.view.MainActivity;
import com.example.maxim.myweather.view.MyActivity;
import com.example.maxim.myweather.view.StartActivity;

public class StartPresenter implements MyPresenter{
    private StartActivity activity;
    private MyModel model;

    public StartPresenter(){

    }

    @Override
    public void attachView(MyActivity activity) {
        this.activity = (StartActivity) activity;
        this.model = new MyModel(this);
    }

    @Override
    public void viewIsReady() {
        model.updateCurrentPlace();
        startMainActivity();
        activity.finish();
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
    }

    public Context getAppContext(){
        return activity.getApplicationContext();
    }

    @Override
    public void requestLocationPermissions() {
        return;
    }

    @Override
    public void showToast(String message) {

    }


}
