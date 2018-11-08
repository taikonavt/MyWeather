package com.example.maxim.myweather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.maxim.myweather.R;
import com.example.maxim.myweather.model.MyModel;
import com.example.maxim.myweather.presenter.MainPresenter;
import com.example.maxim.myweather.presenter.MyPresenter;
import com.example.maxim.myweather.presenter.StartPresenter;

public class StartActivity extends AppCompatActivity
                            implements MyActivity{
    StartPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        presenter = new StartPresenter();
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
