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
import com.example.maxim.myweather.database.WeatherProvider;
import com.example.maxim.myweather.network.Network;
import com.example.maxim.myweather.network.WeatherRequest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    Network.DbCallable{

    public static final String TAG = "myTag";
    public static final String CLASS = MainActivity.class.getSimpleName() + " ";


    private static final String LAST_LOCATION_KEY = "last_location_key";
    private static final String UNKNOWN_CURRENT_LOCATION = "unknown";
    private ArrayList<Location> favoriteLocationList;
    private int displayingLocationIndex;
    private WeatherProvider weatherProvider;

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
        weatherProvider = new WeatherProvider();

        favoriteLocationList = getFavoriteLocations();
        displayingLocationIndex = getLastDisplayingLocationIndex();
        updateLocation(displayingLocationIndex);

        startService(new Intent(MainActivity.this, SyncIntentService.class));

        initGui();

        setTodayWeather();
    }

    private void initGui() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(
                favoriteLocationList.get(displayingLocationIndex).getCityName());

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
        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[]{Long.toString(
                favoriteLocationList.get(displayingLocationIndex).getLocationId()
        )};

        Cursor cursor = weatherProvider.query(
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

            tvTodayTemp.setText(Float.toString(cursor.getFloat(temperatureIndex)));
            tvTodayWeatherType.setText(cursor.getString(descriptionIndex));
            tvTodayHumidity.setText(cursor.getInt(humidityIndex));
            tvTodayWind.setText(Float.toString(cursor.getFloat(windSpeedIndex))
                    + " ," + Float.toString(cursor.getFloat(degreesIndex)));
        }
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
            }
        }
        updateLocation(displayingLocationIndex);
        setTodayWeather();

        navigationView.post(onNavChange);
        getSupportActionBar().setTitle(favoriteLocationList.get(displayingLocationIndex).getCityName());

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
        menuLocation.getItem(0).setTitle(favoriteLocationList.get(0).getCityName());

        MenuItem favourites = menu.getItem(FAVOURITES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);
        for (int i = 1; i < favoriteLocationList.size(); i++) {
//            final int ITEM_ID = i;
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, i, ITEM_ORDER + i, favoriteLocationList.get(i).getCityName())
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
            else if (displayingLocationIndex <= favoriteLocationList.size()){
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
        for (int i = 0; i < favoriteLocationList.size() - 1; i++) {
            navigationView.getMenu()
                    .getItem(1)
                    .getSubMenu()
                    .getItem(i)
                    .setChecked(false);
        }
    }

    private ArrayList<Location> getFavoriteLocations(){
        ArrayList<Location> locationList = new ArrayList<>();
        addFavoriteLocationsFromDB(locationList);
        return locationList;
    }

    private Location getCurrentLocationFromGps(){
        // TODO: 25.08.18 get coordinates from gps
        Location location = new Location();
        location.setCoordLat("55.75222");
        location.setCoordLong("37.615555");
        return location;
    }

    private void updateLocation(int pos) {
        if (pos <= 0){
            Location currentLocation = getCurrentLocationFromGps();
            favoriteLocationList.add(0, currentLocation);
            String coordLat = currentLocation.getCoordLat();
            String coordLon = currentLocation.getCoordLon();
            Network.getInstance().requestTodayWeather(coordLat, coordLon);
        } else {
            Location location = favoriteLocationList.get(pos);
            long locationId = location.getLocationId();
            Network.getInstance().requestTodayWeather(locationId);
        }
    }

    private void addFavoriteLocationsFromDB(ArrayList<Location> arrayList) {
//        arrayList.add(1, "Saint Petersburg"); // index > 0 favourite
//        arrayList.add(2, "Nizhny Novgorod");
//        arrayList.add(3, "Moscow");

        Uri uri = Contract.LocationEntry.CONTENT_URI;

        Cursor cursor = weatherProvider.query(
                uri,
                null,
                null,
                null,
                null);

        int i = 0;
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
                location.setCoordLat(cursor.getString(latitudeIndex));
                location.setCoordLat(cursor.getString(longitudeIndex));
                location.setTodayLastUpdate(cursor.getLong(todayIndex));
                location.setForecastLastUpdate(cursor.getLong(forecastIndex));
                arrayList.add(i, location);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
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
                favoriteLocationList.get(displayingLocationIndex).getCityName());
    }

    public int getLastDisplayingLocationIndex() {
        AppPreferences preferences = new AppPreferences(this);
        String string = preferences.getPreference(LAST_LOCATION_KEY, "");
        return favoriteLocationList.indexOf(string);
    }

    @Override
    public void sendToDbTodayWeather(WeatherRequest weatherRequest) {
        long id = weatherRequest.getId();
        int weatherId = weatherRequest.getWeather()[0].getId();
        String shortDescription = weatherRequest.getWeather()[0].getDescription();
        float temperature = weatherRequest.getMain().getTemp();
        int humidity = weatherRequest.getMain().getHumidity();
        int pressure = weatherRequest.getMain().getPressure();
        float windSpeed = weatherRequest.getWind().getSpeed();
        float windDegrees = weatherRequest.getWind().getDeg();

        ContentValues cv = new ContentValues();
        cv.put(Contract.TodayWeatherEntry.COLUMN_LOCATION_ID, id);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID, weatherId);
        cv.put(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC, shortDescription);
        cv.put(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE, temperature);
        cv.put(Contract.TodayWeatherEntry.COLUMN_HUMIDITY, humidity);
        cv.put(Contract.TodayWeatherEntry.COLUMN_PRESSURE, pressure);
        cv.put(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED, windSpeed);
        cv.put(Contract.TodayWeatherEntry.COLUMN_DEGREES, windDegrees);

        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[]{Long.toString(id)};

        Cursor query = weatherProvider.query(uri, null, selection, selectionArgs, null);
        if (query.moveToFirst()){
            weatherProvider.update(uri, cv, selection, selectionArgs);
        }
        else {
            weatherProvider.insert(uri, cv);
        }
        query.close();
    }

    @Override
    public void sendToDbForecastWeather(WeatherRequest weatherRequest) {

    }
}
