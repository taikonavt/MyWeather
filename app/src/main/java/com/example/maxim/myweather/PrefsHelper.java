package com.example.maxim.myweather;

/**
 * Created by maxim on 05.08.18.
 */

interface PrefsHelper {
    String getPreference(String keyPref, String ifNull);

    int getPreference(String keyPref, int ifNull);

    void savePreference(String keyPref, String value);

    void savePreference(String keyPref, int value);

    void deletePreference(String keyPref);
}
