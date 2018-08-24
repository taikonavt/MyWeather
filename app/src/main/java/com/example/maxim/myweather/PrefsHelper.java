package com.example.maxim.myweather;

/**
 * Created by maxim on 05.08.18.
 */

interface PrefsHelper {
    String getPreference(String keyPref, String ifNull);

    void savePreference(String keyPref, String value);

    void deletePreference(String keyPref);
}
