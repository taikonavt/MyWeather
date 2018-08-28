package com.example.maxim.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maxim on 05.08.18.
 */

public class AppPreferences implements PrefsHelper{
    public static final String LAST_CURRENT_LOCATION_KEY = "last_current_location_key";
    public static final String UNITS_KEY = "units_key";
    public static final String UNITS_IMP = "imperial";
    public static final String UNITS_METRIC = "metric";
    public static final String API_KEY = "api_key";
    public static final String MY_API = "54c14a8a9eb21c3a704db6e4a59636a0";
    public static final String LANGUAGE_KEY = "language_key";
    public static final String UPDATE_PERIOD_KEY = "update_period_key";

    private SharedPreferences sharedPreferences;

    public AppPreferences(AppCompatActivity activity){
        this.sharedPreferences = activity.getPreferences(MODE_PRIVATE);
    }

    @Override
    public String getPreference(String keyPref, String ifNull) {
        return sharedPreferences.getString(keyPref, ifNull);
    }

    @Override
    public int getPreference(String keyPref, int ifNull) {
        return sharedPreferences.getInt(keyPref, ifNull);
    }


    @Override
    public void savePreference(String keyPref, String value) {
        sharedPreferences.edit().putString(keyPref, value).apply();
    }

    @Override
    public void savePreference(String keyPref, int value) {
        sharedPreferences.edit().putInt(keyPref, value).apply();
    }

    @Override
    public void deletePreference(String keyPref) {
        sharedPreferences.edit().remove(keyPref).apply();
    }

}
