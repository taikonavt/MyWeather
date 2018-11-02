package com.example.maxim.myweather.presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.maxim.myweather.view.MainActivity;
import com.example.maxim.myweather.view.MyActivity;


public class MainPresenter implements MyPresenter {
    private static boolean instantiated = false;
    private AppCompatActivity activity;

    public MainPresenter() throws UnsupportedOperationException{
        if (instantiated){
            throw new UnsupportedOperationException("MainPresenter instance may be only one");
        }
        instantiated = true;
    }

    @Override
    public void attachView(MyActivity activity) {
        this.activity = (MainActivity) activity;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void destroy() {

    }
}
