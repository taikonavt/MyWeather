package com.example.maxim.myweather;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxim.myweather.database.Contract;
import com.example.maxim.myweather.network.Network;
import com.example.maxim.myweather.network.forecast.ForecastWeatherRequest;
import com.example.maxim.myweather.network.today.TodayWeatherRequest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    Network.DbCallable{

    public static final String TAG = "myTag";
    public static final String CLASS = MainActivity.class.getSimpleName() + " ";


    private static final String LAST_LOCATION_KEY = "last_location_key";
    private static final int MOSCOW_ID = 524901;
    private ArrayList<Location> locationList;
    private int displayingLocationIndex;

    private TextView tvTodayTemp;
    private TextView tvTodayWeatherType;
    private TextView tvTodayWind;
    private TextView tvTodayHumidity;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Network.initNetwork(this);

        displayingLocationIndex = 0;
        locationList = new ArrayList<>();
        addCurrentLocationFromPref(locationList);
        getFavoriteLocations(locationList);

        initGui();
        setTodayWeather();

        updateCurrentLocationFromGps();

        startService(new Intent(MainActivity.this, SyncIntentService.class));
    }

    private void initGui() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateDrawersItem();

        tvTodayTemp = (TextView) findViewById(R.id.tv_main_info_field_temperature);
        tvTodayHumidity = (TextView) findViewById(R.id.tv_main_info_field_humidity);
        tvTodayWeatherType = (TextView) findViewById(R.id.tv_main_info_field_weather_type);
        tvTodayWind = (TextView) findViewById(R.id.tv_main_info_field_wind);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_ten_days_forecast);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ForecastListAdapter adapter = new ForecastListAdapter(getFakeForecast());
        recyclerView.setAdapter(adapter);
    }

    private void setTodayWeather() {
        getSupportActionBar().setTitle(locationList.get(displayingLocationIndex).getCityName());

        Cursor cursor;

        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[]{Long.toString(
                locationList.get(displayingLocationIndex).getLocationId()
        )};

        cursor = getContentResolver().query(
                uri,
                null,
                selection,
                selectionArgs,
                null
        );

        Log.d(TAG, CLASS + "setTodayWeather(); " + cursor.getCount());

        if (cursor.moveToFirst()){
            int locationIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_LOCATION_ID);
            int weatherIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID);
            int descriptionIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC);
            int temperatureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE);
            int humidityIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_HUMIDITY);
            int pressureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PRESSURE);
            int windSpeedIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED);
            int degreesIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_DEGREES);

            tvTodayTemp.setText(Float.toString(cursor.getFloat(temperatureIndex)) +
                            getString(R.string.degrees_celsius));
            tvTodayWeatherType.setText(cursor.getString(descriptionIndex));
            tvTodayHumidity.setText(Integer.toString(cursor.getInt(humidityIndex)) + "%");
            tvTodayWind.setText(Float.toString(cursor.getFloat(windSpeedIndex)) +
                    getString(R.string.metrov_v_sec) + ", " +
                    Float.toString(cursor.getFloat(degreesIndex)) + getString(R.string.degrees));
        }
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(TAG, "onNavigationItemSelected(): " + id );

        switch (id){
            case R.id.current_place:
                displayingLocationIndex = 0;
                break;
            default: {
                displayingLocationIndex = id; // здесь id от 1 и дальше
                long locationId = locationList.get(displayingLocationIndex).getLocationId();
                Log.d(TAG, CLASS + "onNavigationItemSelected() " + locationId);
                Network.getInstance().requestTodayWeather(locationId);
            }
        }

        navigationView.post(onNavChange);
        getSupportActionBar().setTitle(locationList.get(displayingLocationIndex).getCityName());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateDrawersItem() {
        int LOCATION_ID = 0;
        int FAVOURITES_ID = 1;
        int GROUP_ID = 1;
        int ITEM_ORDER = 100;

        Menu menu = navigationView.getMenu();

        MenuItem location = menu.getItem(LOCATION_ID);
        Menu menuLocation = location.getSubMenu();
        menuLocation.getItem(0).setTitle(locationList.get(0).getCityName());

        MenuItem favourites = menu.getItem(FAVOURITES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);
        for (int i = 1; i < locationList.size(); i++) {
//            final int ITEM_ID = i;
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, i, ITEM_ORDER + i, locationList.get(i).getCityName())
                    .setIcon(R.drawable.ic_place_black_24dp)
                    .setActionView(R.layout.action_view_delete);

            View actionView = menuItem.getActionView();
            ImageView imageView = (ImageView) actionView.findViewById(R.id.iv_delete);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 20.07.18 Realize deleting
                    Log.d(TAG, "clicked: " + menuItem.getItemId());
                }
            });
        }
        navigationView.post(onNavChange);
    }

    private Runnable onNavChange = new Runnable() {
        @Override
        public void run() {
            if (displayingLocationIndex == 0){
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(0)
                        .getSubMenu()
                        .getItem(0);
                menuItem.setChecked(true);
            }
            else if (displayingLocationIndex <= locationList.size()){
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(1)
                        .getSubMenu()
                        .getItem(displayingLocationIndex -1);
                menuItem.setChecked(true);
            }
        }
    };

    private void clearCheckedPosition(){
        navigationView.getMenu()
                .getItem(0)
                .getSubMenu()
                .getItem(0)
                .setChecked(false);
        for (int i = 0; i < locationList.size() - 1; i++) {
            navigationView.getMenu()
                    .getItem(1)
                    .getSubMenu()
                    .getItem(i)
                    .setChecked(false);
        }
    }

    private void getFavoriteLocations(ArrayList<Location> locationList){

        Uri uri = Contract.LocationEntry.CONTENT_URI;

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        int i = 1;
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_LOCATION_ID);
            int cityNameIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_CITY_NAME);
            int countryIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COUNTRY_NAME);
            int latitudeIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LAT);
            int longitudeIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LONG);
            int todayIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE);
            int forecastIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE);

            do {
                Location location = new Location();
                location.setLocationId(cursor.getInt(idIndex));
                location.setCityName(cursor.getString(cityNameIndex));
                location.setCountryName(cursor.getString(countryIndex));
                location.setCoordLat(cursor.getFloat(latitudeIndex));
                location.setCoordLat(cursor.getFloat(longitudeIndex));
                location.setTodayLastUpdate(cursor.getLong(todayIndex));
                location.setForecastLastUpdate(cursor.getLong(forecastIndex));
                locationList.add(i, location);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void updateCurrentLocationFromGps(){
        // TODO: 25.08.18 get coordinates from gps
        Location location = new Location();
        location.setCoordLat(55.75222f);
        location.setCoordLong(37.615555f);

        Network.getInstance().requestTodayWeather(location.getCoordLat(), location.getCoordLon());
        Network.getInstance().requestForecastWeather(location.getCoordLat(), location.getCoordLon());
    }

    private void addCurrentLocationFromPref(ArrayList<Location> locationList){
        Location location = new Location();
        AppPreferences preferences = new AppPreferences(this);
        long cityId = preferences.getPreference(AppPreferences.LAST_CURRENT_LOCATION_KEY, MOSCOW_ID);
        location.setLocationId(cityId);
        location.setCityName(getString(R.string.current_location));
        setLocationById(location);
        locationList.add(location);
    }

    private void setLocationById(Location location){
        String locationId = Long.toString(location.getLocationId());
        Uri uri = Contract.LocationEntry.CONTENT_URI.buildUpon()
                .appendPath(locationId)
                .build();

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        int locationIdIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_LOCATION_ID);
        int cityNameIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_CITY_NAME);
        int countryNameIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COUNTRY_NAME);
        int coordLatIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LAT);
        int coordLonIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_COORD_LONG);
        int todayLastUpdateIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE);
        int forecastLastUpdateIndex = cursor.getColumnIndex(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE);

        if (cursor.moveToFirst()){
            location.setCityName(cursor.getString(cityNameIndex));
            location.setCountryName(cursor.getString(countryNameIndex));
            location.setCoordLat(cursor.getFloat(coordLatIndex));
            location.setCoordLong(cursor.getFloat(coordLonIndex));
            location.setTodayLastUpdate(cursor.getLong(todayLastUpdateIndex));
            location.setForecastLastUpdate(cursor.getLong(forecastLastUpdateIndex));
        }
    }

    private String[] getFakeForecast(){
        String[] strings = new String[10];
        for (int i = 0; i < 10; i++) {
            strings[i] = "123";
        }
        return strings;
    }

    // последний гарантированно вызываемый метод перед закрытием
    @Override
    protected void onPause() {
        super.onPause();

        AppPreferences preferences = new AppPreferences(this);
        preferences.savePreference(LAST_LOCATION_KEY,
                locationList.get(displayingLocationIndex).getCityName());
    }

    @Override
    public void updateCurrentLocation(TodayWeatherRequest todayWeatherRequest) {
        long locationId = todayWeatherRequest.getId();
        String cityName = todayWeatherRequest.getName();
        String countryName = todayWeatherRequest.getSys().getCountry();
        float coordLon = todayWeatherRequest.getCoord().getLon();
        float coordLat = todayWeatherRequest.getCoord().getLat();
        long todayLastUdpate = System.currentTimeMillis();
        long forecastLastUpdate = 0;

        Location location = locationList.get(0);
        location.setLocationId(locationId);
        location.setCityName(cityName);
        location.setCountryName(countryName);
        location.setCoordLong(coordLon);
        location.setCoordLat(coordLat);
        location.setTodayLastUpdate(todayLastUdpate);
        location.setForecastLastUpdate(forecastLastUpdate);

        Log.d(TAG, CLASS + "updateCurrentLocation(); " + cityName);

        updateDrawersItem();
    }

//    @Override
//    public void sendToDbNewLocation(ForecastWeatherRequest weatherRequest) {
//        long locationId = weatherRequest.getId();
//        String cityName = weatherRequest.getName();
//        String countryName = weatherRequest.getSys().getCountry();
//        float coordLon = weatherRequest.getCoord().getLon();
//        float coordLat = weatherRequest.getCoord().getLat();
//        long todayLastUdpate = System.currentTimeMillis();
//        long forecastLastUpdate = 0;
//
//        ContentValues cv = new ContentValues();
//        cv.put(Contract.LocationEntry.COLUMN_LOCATION_ID, locationId);
//        cv.put(Contract.LocationEntry.COLUMN_CITY_NAME, cityName);
//        cv.put(Contract.LocationEntry.COLUMN_COUNTRY_NAME, countryName);
//        cv.put(Contract.LocationEntry.COLUMN_COORD_LONG, coordLon);
//        cv.put(Contract.LocationEntry.COLUMN_COORD_LAT, coordLat);
//        cv.put(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE, todayLastUdpate);
//        cv.put(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE, forecastLastUpdate);
//
//        Uri uri = Contract.LocationEntry.CONTENT_URI;
//        getContentResolver().insert(uri, cv);
//
//        locationList = getFavoriteLocations();
//        updateDrawersItem();
//    }

    @Override
    public void sendToDbTodayWeather(TodayWeatherRequest todayWeatherRequest) {
        long locationId = todayWeatherRequest.getId();
        int weatherId = todayWeatherRequest.getWeather()[0].getId();
        String shortDescription = todayWeatherRequest.getWeather()[0].getDescription();
        float temperature = todayWeatherRequest.getMain().getTemp();
        int humidity = todayWeatherRequest.getMain().getHumidity();
        int pressure = todayWeatherRequest.getMain().getPressure();
        float windSpeed = todayWeatherRequest.getWind().getSpeed();
        float windDegrees = todayWeatherRequest.getWind().getDeg();

        ContentValues cv = new ContentValues();
        cv.put(Contract.TodayWeatherEntry.COLUMN_LOCATION_ID, locationId);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID, weatherId);
        cv.put(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC, shortDescription);
        cv.put(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE, temperature);
        cv.put(Contract.TodayWeatherEntry.COLUMN_HUMIDITY, humidity);
        cv.put(Contract.TodayWeatherEntry.COLUMN_PRESSURE, pressure);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED, windSpeed);
        cv.put(Contract.TodayWeatherEntry.COLUMN_DEGREES, windDegrees);

        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[]{Long.toString(locationId)};

        Cursor query = getContentResolver().query(uri, null, selection, selectionArgs, null);
        if (query.moveToFirst()){
            getContentResolver().delete(uri, selection, selectionArgs);
            getContentResolver().insert(uri, cv);
        }
        else {
            Uri u = getContentResolver().insert(uri, cv);
            Log.d(TAG, CLASS + u.toString());
        }
        query.close();
        setTodayWeather();
    }

    @Override
    public void sendToDbForecastWeather(ForecastWeatherRequest forecastWeatherRequest) {
        int cnt = forecastWeatherRequest.getCnt();
        long locationId = forecastWeatherRequest.getCity().getId();
        ContentValues[] values = new ContentValues[cnt];

        long date;
        int weatherId;
        String description;
        float minTemp;
        float maxTemp;
        int humidity;
        float pressure;
        float speed;
        float degrees;

        for (int i = 0; i < cnt; i++) {
            date = forecastWeatherRequest.getList()[i].getDt();
            weatherId = forecastWeatherRequest.getList()[i].getWeather()[0].getId();
            description = forecastWeatherRequest.getList()[i].getWeather()[0].getMain();
            minTemp = forecastWeatherRequest.getList()[i].getTemp().getMin();
            maxTemp = forecastWeatherRequest.getList()[i].getTemp().getMax();
            humidity = forecastWeatherRequest.getList()[i].getHumidity();
            pressure = forecastWeatherRequest.getList()[i].getPressure();
            speed = forecastWeatherRequest.getList()[i].getSpeed();
            degrees = forecastWeatherRequest.getList()[i].getDeg();

            ContentValues cv = new ContentValues();
            cv.put(Contract.ForecastWeatherEntry.COLUMN_LOCATION_ID, locationId);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_DATE, date);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_WEATHER_ID, weatherId);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_SHORT_DESC, description);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_MIN_TEMP, minTemp);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_MAX_TEMP, maxTemp);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_HUMIDITY, humidity);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_PRESSURE, pressure);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_WIND_SPEED, speed);
            cv.put(Contract.ForecastWeatherEntry.COLUMN_DEGREES, degrees);

            values[i] = cv;
        }

        String[] stringArgs = new String[] {Long.toString(locationId)};
        getContentResolver().delete(
                Contract.ForecastWeatherEntry.CONTENT_URI,
                Contract.ForecastWeatherEntry.COLUMN_LOCATION_ID,
                stringArgs);

        int n = getContentResolver().bulkInsert(Contract.ForecastWeatherEntry.CONTENT_URI, values);
        Log.d(TAG, CLASS + "sendToDbForecastWeather(); " + n);
    }

//    private void addNewFavoriteLocation(){
//        Location saintPetersburg = new Location();
//        saintPetersburg.setCityName("Saint Petersburg");
//        Network.getInstance().requestTodayWeather(saintPetersburg.getCityName());
//    }
}
