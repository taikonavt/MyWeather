package com.example.maxim.myweather;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
                    Network.DbCallable, LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "myTag";
    public static final String CLASS = MainActivity.class.getSimpleName() + " ";


    private static final String LAST_LOCATION_KEY = "last_location_key";
    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int MOSCOW_ID = 524901;
    private static final int ID_LOADER = 1111;
    private ArrayList<Place> placeList;
    private int displayingLocationIndex;

    private TextView tvTodayTemp;
    private TextView tvTodayWeatherType;
    private TextView tvTodayWind;
    private TextView tvTodayHumidity;
    private NavigationView navigationView;
    private ForecastListAdapter adapter;
    private LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Network.initNetwork(this);

        displayingLocationIndex = 0;
        placeList = new ArrayList<>();
        addCurrentLocationFromPref(placeList);
        getFavoriteLocations(placeList);

        initGui();
        setTodayWeather();

        updateCurrentLocationFromGps();

        startService(new Intent(MainActivity.this, SyncIntentService.class));

        getSupportLoaderManager().initLoader(ID_LOADER, null, this);
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
        updateDrawerItems();

        tvTodayTemp = (TextView) findViewById(R.id.tv_main_info_field_temperature);
        tvTodayHumidity = (TextView) findViewById(R.id.tv_main_info_field_humidity);
        tvTodayWeatherType = (TextView) findViewById(R.id.tv_main_info_field_weather_type);
        tvTodayWind = (TextView) findViewById(R.id.tv_main_info_field_wind);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_ten_days_forecast);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ForecastListAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setTodayWeather() {
        getSupportActionBar().setTitle(placeList.get(displayingLocationIndex).getCityName());

        Cursor cursor;

        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
        String selection = Contract.TodayWeatherEntry.COLUMN_LOCATION_ID;
        String[] selectionArgs = new String[]{Long.toString(
                placeList.get(displayingLocationIndex).getLocationId()
        )};

        cursor = getContentResolver().query(
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

    final int NEW_LOCATION_REQUEST_CODE = 121;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final int addNewLocationBtnId = placeList.size();

        if (id == R.id.current_place){
            displayingLocationIndex = 0;
        } else if (id == addNewLocationBtnId){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, NEW_LOCATION_REQUEST_CODE);
            return true;
        } else{
            displayingLocationIndex = id; // здесь id от 1 и дальше
        }
        updateInfo();
        return true;
    }

    private void updateInfo() {
        long locationId = placeList.get(displayingLocationIndex).getLocationId();
        Network.getInstance().requestTodayWeather(locationId);
        Network.getInstance().requestForecastWeather(locationId);
        getSupportLoaderManager().restartLoader(ID_LOADER, null, this);

        navigationView.post(onNavChange);
        getSupportActionBar().setTitle(placeList.get(displayingLocationIndex).getCityName());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_LOCATION_REQUEST_CODE: {
                    Place place = (Place) data.getParcelableExtra(SearchActivity.PLACE_KEY);
                    Log.d(TAG, MainActivity.class.getSimpleName() + " onActivityResult(); "
                    + place.getCountryName());
                    placeList.add(place);
                    displayingLocationIndex = placeList.indexOf(place);
                    updateDrawerItems();
                    updateInfo();
                    addFavoriteLocation(place);
                }
            }
        }
    }

    private void updateDrawerItems() {
        int LOCATION_ID = 0;
        int FAVOURITES_ID = 1;
        int GROUP_ID = 1;
        int ITEM_ORDER = 100;

        Menu menu = navigationView.getMenu();
        MenuItem location = menu.getItem(LOCATION_ID);
        Menu menuLocation = location.getSubMenu();
        menuLocation.getItem(0).setTitle(placeList.get(0).getCityName());

        MenuItem favourites = menu.getItem(FAVOURITES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);
        for (int i = 1; i < placeList.size(); i++) {
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, i, ITEM_ORDER + i, placeList.get(i).getCityName())
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

        int addNewLocationBtnId = placeList.size();
        menuFavourites
                .add(GROUP_ID, addNewLocationBtnId,
                        ITEM_ORDER + addNewLocationBtnId,
                        R.string.add_new_location)
                .setIcon(R.drawable.ic_add_black_24dp);
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
            else if (displayingLocationIndex <= placeList.size()){
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
        for (int i = 0; i < placeList.size() - 1; i++) {
            navigationView.getMenu()
                    .getItem(1)
                    .getSubMenu()
                    .getItem(i)
                    .setChecked(false);
        }
    }

    private void getFavoriteLocations(ArrayList<Place> placeList){

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
                Place place = new Place();
                place.setLocationId(cursor.getInt(idIndex));
                place.setCityName(cursor.getString(cityNameIndex));
                place.setCountryName(cursor.getString(countryIndex));
                place.setCoordLat(cursor.getFloat(latitudeIndex));
                place.setCoordLat(cursor.getFloat(longitudeIndex));
                place.setTodayLastUpdate(cursor.getLong(todayIndex));
                place.setForecastLastUpdate(cursor.getLong(forecastIndex));
                placeList.add(i, place);
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void addFavoriteLocation(Place place){
        Uri uri = Contract.LocationEntry.CONTENT_URI;

        ContentValues cv = new ContentValues();
        cv.put(Contract.LocationEntry.COLUMN_CITY_NAME, place.getCityName());
        cv.put(Contract.LocationEntry.COLUMN_COUNTRY_NAME, place.getCountryName());
        cv.put(Contract.LocationEntry.COLUMN_LOCATION_ID, place.getLocationId());
        cv.put(Contract.LocationEntry.COLUMN_COORD_LAT, place.getCoordLat());
        cv.put(Contract.LocationEntry.COLUMN_COORD_LONG, place.getCoordLon());
        cv.put(Contract.LocationEntry.COLUMN_TODAY_LAST_UPDATE, place.getTodayLastUpdate());
        cv.put(Contract.LocationEntry.COLUMN_FORECAST_LAST_UPDATE, place.getForecastLastUpdate());

        getContentResolver().insert(uri, cv);
    }

    private void updateCurrentLocationFromGps(){
        Place place = new Place();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation(place);
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation(final Place place) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 10 секунд или каждые 10 метров
            int MIN_TIME = 1000 * 60 * 10;
            int MIN_DISTANCE = 1000;
            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Log.d(TAG, CLASS + "onLocationChanged(); " + lat + " " + lon);
                    place.setCoordLat((float) location.getLatitude());
                    place.setCoordLong((float) location.getLongitude());
                    Network.getInstance().requestTodayWeather(place.getCoordLat(), place.getCoordLon());
                    Network.getInstance().requestForecastWeather(place.getCoordLat(), place.getCoordLon());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            }, null);
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            // Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {   // Это та самая пермиссия, что мы запрашивали?
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                requestLocation(placeList.get(0));
            }
        }
    }

    private void addCurrentLocationFromPref(ArrayList<Place> placeList){
        Place place = new Place();
        AppPreferences preferences = new AppPreferences(this);
        long cityId = preferences.getPreference(AppPreferences.LAST_CURRENT_LOCATION_KEY, MOSCOW_ID);
        place.setLocationId(cityId);
        place.setCityName(getString(R.string.current_location));
        setLocationById(place);
        placeList.add(place);
    }

    private void setLocationById(Place place){
        String locationId = Long.toString(place.getLocationId());
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
            place.setCityName(cursor.getString(cityNameIndex));
            place.setCountryName(cursor.getString(countryNameIndex));
            place.setCoordLat(cursor.getFloat(coordLatIndex));
            place.setCoordLong(cursor.getFloat(coordLonIndex));
            place.setTodayLastUpdate(cursor.getLong(todayLastUpdateIndex));
            place.setForecastLastUpdate(cursor.getLong(forecastLastUpdateIndex));
        }
    }

    // последний гарантированно вызываемый метод перед закрытием
    @Override
    protected void onPause() {
        super.onPause();

        AppPreferences preferences = new AppPreferences(this);
        preferences.savePreference(LAST_LOCATION_KEY,
                placeList.get(displayingLocationIndex).getCityName());
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

        Place place = placeList.get(0);
        place.setLocationId(locationId);
        place.setCityName(cityName);
        place.setCountryName(countryName);
        place.setCoordLong(coordLon);
        place.setCoordLat(coordLat);
        place.setTodayLastUpdate(todayLastUdpate);
        place.setForecastLastUpdate(forecastLastUpdate);

        updateDrawerItems();
    }

    @Override
    public void sendToDbTodayWeather(TodayWeatherRequest todayWeatherRequest) {
        long locationId = todayWeatherRequest.getId();
        int weatherId = todayWeatherRequest.getWeather()[0].getId();
        String shortDescription = todayWeatherRequest.getWeather()[0].getDescription();
        float temperature = todayWeatherRequest.getMain().getTemp();
        float humidity = todayWeatherRequest.getMain().getHumidity();
        float pressure = todayWeatherRequest.getMain().getPressure();
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
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        switch (i){
            case ID_LOADER:
                Uri uri = Contract.ForecastWeatherEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(placeList.get(displayingLocationIndex).getLocationId()))
                        .build();
                String sortOrder= Contract.ForecastWeatherEntry.COLUMN_DATE + " ASC";

                return new CursorLoader(this,
                        uri,
                        null,
                        null,
                        null,
                        sortOrder);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + i);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
