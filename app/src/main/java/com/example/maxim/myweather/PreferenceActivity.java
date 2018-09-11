package com.example.maxim.myweather;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.AccessNetworkConstants;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.myweather.database.Contract;
import com.example.maxim.myweather.database.DatabaseHelper;

/**
 * Created by maxim on 05.08.18.
 */

public class PreferenceActivity extends AppCompatActivity {
    private static final String TAG = "myTag";
    private static final String CLASS = PreferenceActivity.class.getSimpleName() + " ";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getSupportActionBar().setTitle(R.string.settings_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayout logIn = (LinearLayout) findViewById(R.id.ll_activity_preferences_log_in);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogIn();
            }
        });

        LinearLayout unit = (LinearLayout) findViewById(R.id.ll_activity_preferences_units);
        unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUnits(view);
            }
        });

        TextView aboutDev = (TextView) findViewById(R.id.tv_activity_preferences_developer);
        aboutDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutDev();
            }
        });

        Button button = (Button) findViewById(R.id.button_query);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, CLASS + "onClick();");

                addLocation();
//                showLocation();
//                showTodayWeather();
//                askDB();
//                showForecast();
                }
        });

        Spinner language = (Spinner) findViewById(R.id.sp_activity_preferences_language);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.settings_languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplication().getBaseContext(), "Sorry language choosing doesn't work yet", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showAboutDev() {
        Toast.makeText(this, "Sorry developer info doesn't work yet", Toast.LENGTH_LONG).show();
    }

    private void changeUnits(View view) {
        AppPreferences preferences = new AppPreferences(this);
        TextView tv = (TextView) findViewById(R.id.tv_activity_preferences_units);
        String stringFromView = (String) tv.getText();
        String metric = getResources().getString(R.string.settings_measurement_units_metric);
        String imperial = getResources().getString(R.string.settings_measurement_units_imperial);

        if (stringFromView.equals(metric)) {
            tv.setText(imperial);
            preferences.savePreference(AppPreferences.UNITS_KEY, AppPreferences.UNITS_IMP);
        }
        else {
            tv.setText(metric);
            preferences.savePreference(AppPreferences.UNITS_KEY, AppPreferences.UNITS_METRIC);
        }
    }

    private void startLogIn() {
        Toast.makeText(this, "Sorry log in doesn't work yet", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTodayWeather(){
        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[] {"524901"};
        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()){
            int locationIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_LOCATION_ID);
            int weatherIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID);
            int descriptionIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC);
            int temperatureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE);
            int humidityIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_HUMIDITY);
            int pressureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PRESSURE);
            int windSpeedIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED);
            int degreesIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_DEGREES);

            do {
                Log.d(TAG, CLASS +
                        Long.toString(cursor.getLong(locationIdIndex)) + " " +
                        Integer.toString(cursor.getInt(weatherIdIndex)) +  " " +
                        cursor.getString(descriptionIndex) + " " +
                        Float.toString(cursor.getFloat(temperatureIndex)) + " " +
                        Integer.toString(cursor.getInt(humidityIndex)) + " " +
                        Integer.toString(cursor.getInt(pressureIndex)) + " " +
                        Float.toString(cursor.getFloat(windSpeedIndex)) + " " +
                        Float.toString(cursor.getFloat(degreesIndex))
                );
            } while (cursor.moveToNext());
        }
    }

    private void showLocation(){
        Uri uri = Contract.LocationEntry.CONTENT_URI;
        String selection = Contract.LocationEntry.COLUMN_CITY_NAME;
        String[] selectionArgs = new String[] {"*"};

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                selection,
                null,
                null
        );

        Log.d(TAG, CLASS + "showLocation(); " + cursor.getCount());

        if (cursor.moveToFirst()){
            int locatinIdIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_LOCATION_ID);
            int cityIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_CITY_NAME);
            int countryIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COUNTRY_NAME);
            int coordLatIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LAT);
            int coordLonIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LONG);
            int todayIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE);
            int forecastIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE);

            do {
                Log.d(TAG, CLASS +
                Long.toString(cursor.getLong(locatinIdIndex)) + " " +
                        cursor.getString(cityIndex) +  " " +
                        cursor.getString(countryIndex) + " " +
                        Float.toString(cursor.getFloat(coordLatIndex)) + " " +
                        Float.toString(cursor.getFloat(coordLonIndex)) + " " +
                        Long.toString(cursor.getLong(todayIndex)) + " " +
                        Long.toString(cursor.getLong(forecastIndex))
                );
            } while (cursor.moveToNext());
        }
    }

    private void addLocation(){
        Uri uri = Contract.LocationEntry.CONTENT_URI;
        ContentValues cv = new ContentValues();

        cv.put(Contract.LocationEntry.COLUMN_LOCATION_ID, 1271881);
        cv.put(Contract.LocationEntry.COLUMN_CITY_NAME, "Firozpur Jhirka");
        cv.put(Contract.LocationEntry.COLUMN_COUNTRY_NAME, "IN");
        cv.put(Contract.LocationEntry.COLUMN_COORD_LAT, 76.949997);
        cv.put(Contract.LocationEntry.COLUMN_COORD_LONG, 76.949997);
        cv.put(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE, System.currentTimeMillis());
        cv.put(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE, System.currentTimeMillis());

        Uri u = getContentResolver().insert(uri, cv);
        Log.d(TAG, CLASS + u.toString());
    }

    private void askDB(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                Contract.LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Log.d(TAG, CLASS + "showLocation(); " + cursor.getCount());

        if (cursor.moveToFirst()){
            int locatinIdIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_LOCATION_ID);
            int cityIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_CITY_NAME);
            int countryIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COUNTRY_NAME);
            int coordLatIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LAT);
            int coordLonIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LONG);
            int todayIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE);
            int forecastIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE);

            do {
                Log.d(TAG, CLASS +
                        Long.toString(cursor.getLong(locatinIdIndex)) + " " +
                        cursor.getString(cityIndex) +  " " +
                        cursor.getString(countryIndex) + " " +
                        Float.toString(cursor.getFloat(coordLatIndex)) + " " +
                        Float.toString(cursor.getFloat(coordLonIndex)) + " " +
                        Long.toString(cursor.getLong(todayIndex)) + " " +
                        Long.toString(cursor.getLong(forecastIndex))
                );
            } while (cursor.moveToNext());
        }
    }

    private void showForecast(){
        Uri uri = Contract.ForecastWeatherEntry.CONTENT_URI
                .buildUpon()
                .appendPath("524901")
                .build();

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        int locationIdIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_LOCATION_ID);
        int dateIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_DATE);
        int weatherIdIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_WEATHER_ID);
        int descriptionIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_SHORT_DESC);
        int minIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_MIN_TEMP);
        int maxIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_MAX_TEMP);
        int humidityIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_HUMIDITY);
        int pressureIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_PRESSURE);
        int speedIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_WIND_SPEED);
        int degreesIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_DEGREES);

        if (cursor.moveToFirst()){
            do {
                long locationId = cursor.getLong(locationIdIndex);
                long date = cursor.getLong(dateIndex);
                int weatherId = cursor.getInt(weatherIdIndex);
                String description = cursor.getString(descriptionIndex);
                float min = cursor.getFloat(minIndex);
                float max = cursor.getFloat(maxIndex);
                int humidity = cursor.getInt(humidityIndex);
                float pressure = cursor.getFloat(pressureIndex);
                float speed = cursor.getFloat(speedIndex);
                float degrees = cursor.getFloat(degreesIndex);

                Log.d(TAG, CLASS + "showForecast(); " +
                locationId + " " +
                        date + " " +
                        weatherId +  " " +
                        description +  " " +
                        min + " " +
                        max +  " " +
                        humidity + " " +
                        pressure +  " " +
                        speed +  " " +
                        degrees
                );
            } while (cursor.moveToNext());
        }
    }
}
