package com.example.maxim.myweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by maxim on 05.08.18.
 */

public class PreferenceActivity extends AppCompatActivity {
    private static final String TAG = PreferenceActivity.class.getSimpleName();

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
        TextView tv = (TextView) view;
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
