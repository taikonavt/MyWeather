package com.example.maxim.myweather.presenter;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maxim.myweather.view.MainActivity;
import com.example.maxim.myweather.view.MyActivity;
import com.example.maxim.myweather.view.StartActivity;

import java.util.concurrent.TimeUnit;

public class StartPresenter implements MyPresenter{
    private StartActivity activity;

    @Override
    public void attachView(MyActivity activity) {
        this.activity = (StartActivity) activity;
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

    public void startApp(){
        MyTask myTask = new MyTask();
        myTask.execute();
    }

    private void startMainActivity(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        MainActivity.attachController(this);
        activity.finish();
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(3);
                startMainActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
