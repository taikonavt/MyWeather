package com.example.maxim.myweather;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
                Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
                String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
                String[] selectionArgs = new String[] {"524901"};
                Cursor cursor = getContentResolver().query(
                        uri,
                        null,
                        selection,
                        selectionArgs,
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
}
