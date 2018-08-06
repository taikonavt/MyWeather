package com.example.maxim.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maxim on 05.08.18.
 */

public class AppPreferences implements PrefsHelper{
    public static final String LAST_LOCATION_KEY = "last_location_key";
    public static final String UNITS_KEY = "units_key";
    public static final String API_KEY = "api_key";
    public static final String LANGUAGE_KEY = "language_key";
    public static final String UPDATE_PERIOD_KEY = "update_period_key";

    private SharedPreferences sharedPreferences;

    public AppPreferences(MainActivity mainActivity){
        this.sharedPreferences = mainActivity.getPreferences(MODE_PRIVATE);
    }

    @Override
    public String getPreference(String keyPref, String ifNull) {
        return sharedPreferences.getString(keyPref, ifNull);
    }

    @Override
    public void savePreference(String keyPref, String value) {
        sharedPreferences.edit().putString(keyPref, value).apply();
    }

    @Override
    public void deletePreference(String keyPref) {
        sharedPreferences.edit().remove(keyPref).apply();
    }

}
