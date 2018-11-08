package com.example.maxim.myweather.model;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maxim on 05.08.18.
 */

public class AppPreferences extends PrefsHelper {

    private SharedPreferences preferences;

    public AppPreferences(Context context){
        preferences = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    String getUnits() {
        return preferences.getString(UNITS_KEY, UNITS_METRIC);
    }

    @Override
    String getLanguage() {
        return preferences.getString(LANGUAGE_KEY, RUS);
    }

    @Override
    String getMyApi() {
        return preferences.getString(API_KEY, MY_API);
    }

    @Override
    void setUnits(Units units) {
        switch (units){
            case Imp:
                preferences.edit()
                        .putString(UNITS_KEY, UNITS_IMP)
                        .apply();
                break;
            case Metric:
                preferences.edit()
                        .putString(UNITS_KEY, UNITS_METRIC)
                        .apply();
                break;
        }
    }

    @Override
    void setLanguage(Language language) {
        switch (language){
            case Ru:
                preferences.edit()
                        .putString(LANGUAGE_KEY, RUS)
                        .apply();
                break;
            case Eng:
                preferences.edit()
                        .putString(LANGUAGE_KEY, ENG)
                        .apply();
                break;
        }
    }
}
