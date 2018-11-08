package com.example.maxim.myweather.model;

/**
 * Created by maxim on 05.08.18.
 */

abstract class PrefsHelper {
    public static final String UNITS_KEY = "units_key";
    public static final String UNITS_IMP = "imperial";
    public static final String UNITS_METRIC = "metric";
    public static final String API_KEY = "api_key";
    public static final String MY_API = "54c14a8a9eb21c3a704db6e4a59636a0";
    public static final String LANGUAGE_KEY = "language_key";
    public static final String RUS = "russian";
    public static final String ENG = "english";

    public enum Units{
        Imp, Metric
    }

    public enum Language{
        Ru, Eng
    }

    abstract String getUnits();

    abstract String getLanguage();

    abstract String getMyApi();

    abstract void setUnits(Units units);

    abstract void setLanguage(Language language);
}
