package com.example.maxim.myweather.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import com.example.maxim.myweather.common.Place;
import com.example.maxim.myweather.PreferenceActivity;
import com.example.maxim.myweather.common.TodayWeather;
import com.example.maxim.myweather.model.MyModel;
import com.example.maxim.myweather.view.MainActivity;
import com.example.maxim.myweather.view.MyActivity;

import java.util.ArrayList;


public class MainPresenter implements
        MyPresenter {
    private static final int CURRENT_PLACE_LOADER_ID = 1111;


    private MainActivity activity;
    private MyModel model;

    private int displayingPlaceIndex;
    private Place currentPlace;
    private Place[] favouritePlaces;

    // Above checked constants
    private static final String LAST_LOCATION_KEY = "last_location_key";
    private static final int MOSCOW_ID = 524901;
    private ArrayList<Place> placeList;


    public MainPresenter() {
    }

    @Override
    public void attachView(MyActivity activity) {
        this.activity = (MainActivity) activity;
        this.model = new MyModel(this);
        model.updateCurrentPlace();
        model.initLoader();
        displayingPlaceIndex = 0;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Context getAppContext() {
        return activity;
    }

    public AppCompatActivity getActivity(){
        return activity;
    }

    @Override
    public void requestLocationPermissions() {
        activity.requestLocationPermissions();
    }

    @Override
    public void showToast(String message) {

    }

    public void updateCurrentPlace(Place place){
        currentPlace = place;
        activity.updateNavigatorCurrentPlace(place.getCityName());
    }

    public void updateFavouritePlaces(Place[] places){
        favouritePlaces = places;
        String[] cityNames = new String [places.length];
        for (int i = 0; i < places.length; i++) {
            cityNames[i] = places[i].getCityName();
        }
        activity.updateNavigatorFavouritePlaces(cityNames);
    }

    public void updateTodayWeather(TodayWeather todayWeather){
        activity.updateTodayWeather(
                Float.toString(todayWeather.getTemp()),
                todayWeather.getShortDescr(),
                Float.toString(todayWeather.getWindSpeed()),
                Float.toString(todayWeather.getDegrees()));
    }

    public void updateForecastWeather(Cursor cursor){
        activity.getForecastListAdapter().swapCursor(cursor);
    }

    public void onSettingsSelected() {
        Intent intent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(intent);
    }

    public void onNavigationCurrentPlaceSelected() {
        displayingPlaceIndex = 0;
        activity.setNavigationCurrentPlaceChecked();
        model.download(currentPlace.getPlaceId());
    }

    public void onNavigationItemSelected(int id) {
        displayingPlaceIndex = id + 1;
        activity.setNavigationPlaceChecked(id);
        model.download(favouritePlaces[id].getPlaceId());
        //        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        final int addNewLocationBtnId = placeList.size();
//
//        if (id == R.id.current_place){
//            displayingLocationIndex = 0;
//        } else if (id == addNewLocationBtnId){
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivityForResult(intent, NEW_LOCATION_REQUEST_CODE);
//            return true;
//        } else{
//            displayingLocationIndex = id; // здесь id от 1 и дальше
//        }
//        updateInfo();
    }

    public void deleteFavouritePlace(int id) {
        long placeId = favouritePlaces[id].getPlaceId();
        model.deleteFavouritePlace(placeId);
    }

    public void accessToLocationGranted() {
        model.updateCurrentPlace();
    }
}


//    private void addCurrentLocationFromPref(ArrayList<Place> placeList) {
//        Place place = new Place();
//        AppPreferences preferences = new AppPreferences(this);
//        long cityId = preferences.getPreference(AppPreferences.LAST_CURRENT_LOCATION_KEY, MOSCOW_ID);
//        place.setLocationId(cityId);
//        place.setCityName(getString(R.string.current_location));
//        setLocationById(place);
//        placeList.add(place);
//    }


// above checked methods

//        private void requestLocationPermissions() {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CALL_PHONE)) {
//                // Запросим эти две пермиссии у пользователя
//                ActivityCompat.requestPermissions(this,
//                        new String[]{
//                                Manifest.permission.ACCESS_COARSE_LOCATION,
//                                Manifest.permission.ACCESS_FINE_LOCATION
//                        },
//                        PERMISSION_REQUEST_CODE);
//            }
//        }
//
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//            if (requestCode == PERMISSION_REQUEST_CODE) {   // Это та самая пермиссия, что мы запрашивали?
//                if (grantResults.length == 2 &&
//                        (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//                    // Все препоны пройдены и пермиссия дана
//                    requestLocation(placeList.get(0));
//                }
//            }
//        }

//    private void setTodayWeather() {
//        getSupportActionBar().setTitle(placeList.get(displayingPlaceIndex).getCityName());
//
//        Cursor cursor;
//
//        Uri uri = Contract.TodayWeatherEntry.CONTENT_URI;
//        String selection = Contract.TodayWeatherEntry.COLUMN_PLACE_ID;
//        String[] selectionArgs = new String[]{Long.toString(
//                placeList.get(displayingPlaceIndex).getPlaceId()
//        )};
//
//        cursor = getContentResolver().query(
//                uri,
//                null,
//                selection,
//                selectionArgs,
//                null
//        );
//
//        if (cursor.moveToFirst()){
//            int locationIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PLACE_ID);
//            int weatherIdIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WEATHER_ID);
//            int descriptionIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_SHORT_DESC);
//            int temperatureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_TEMPERATURE);
//            int humidityIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_HUMIDITY);
//            int pressureIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_PRESSURE);
//            int windSpeedIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_WIND_SPEED);
//            int degreesIndex = cursor.getColumnIndex(Contract.TodayWeatherEntry.COLUMN_DEGREES);
//
//            tvTodayTemp.setText(Float.toString(cursor.getFloat(temperatureIndex)) +
//                    getString(R.string.degrees_celsius));
//            tvTodayWeatherType.setText(cursor.getString(descriptionIndex));
//            tvTodayHumidity.setText(Integer.toString(cursor.getInt(humidityIndex)) + "%");
//            tvTodayWind.setText(Float.toString(cursor.getFloat(windSpeedIndex)) +
//                    getString(R.string.metrov_v_sec) + ", " +
//                    Float.toString(cursor.getFloat(degreesIndex)) + getString(R.string.degrees));
//        }
//        cursor.close();
//    }
//
////    private void updateInfo() {
////        long locationId = placeList.get(displayingLocationIndex).getPlaceId();
////        Network.getInstance().requestTodayWeather(locationId);
////        Network.getInstance().requestForecastWeather(locationId);
////        getSupportLoaderManager().restartLoader(ID_LOADER, null, this);
////
////        navigationView.post(onNavChange);
////        getSupportActionBar().setTitle(placeList.get(displayingLocationIndex).getCityName());
////    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        if (resultCode == RESULT_OK) {
////            switch (requestCode) {
////                case NEW_LOCATION_REQUEST_CODE: {
////                    Place place = (Place) data.getParcelableExtra(SearchActivity.PLACE_KEY);
////                    addFavoriteLocation(place);
////                    getFavoriteLocations(placeList);
////                    displayingLocationIndex = placeList.size() - 1;
////                    updateDrawerItems();
////                    updateInfo();
////                }
////            }
////        }
////    }
//
//    private void addFavoriteLocation(Place place){
//        Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI;
//
//        ContentValues cv = new ContentValues();
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_CITY_NAME, place.getCityName());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_COUNTRY_NAME, place.getCountryName());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_PLACE_ID, place.getPlaceId());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_COORD_LAT, place.getCoordLat());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_COORD_LONG, place.getCoordLon());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_TODAY_LAST_UPDATE, place.getTodayLastUpdate());
//        cv.put(Contract.FavouritePlaceEntry.COLUMN_FORECAST_LAST_UPDATE, place.getForecastLastUpdate());
//
//        getContentResolver().insert(uri, cv);
//    }
//
//    private void deleteFavoriteLocation(Place place){
//        Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI;
//        String[] args = new String[]{String.valueOf(place.getPlaceId())};
//        getContentResolver().delete(uri, Contract.FavouritePlaceEntry.COLUMN_PLACE_ID, args);
//    }
//
//
//
//
//
//    private void setLocationById(Place place){
//        String locationId = Long.toString(place.getPlaceId());
//        Uri uri = Contract.FavouritePlaceEntry.CONTENT_URI.buildUpon()
//                .appendPath(locationId)
//                .build();
//
//        Cursor cursor = getContentResolver().query(
//                uri,
//                null,
//                null,
//                null,
//                null
//        );
//
//        int locationIdIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_PLACE_ID);
//        int cityNameIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_CITY_NAME);
//        int countryNameIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COUNTRY_NAME);
//        int coordLatIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COORD_LAT);
//        int coordLonIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_COORD_LONG);
//        int todayLastUpdateIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_TODAY_LAST_UPDATE);
//        int forecastLastUpdateIndex = cursor.getColumnIndex(Contract.FavouritePlaceEntry.COLUMN_FORECAST_LAST_UPDATE);
//
//        if (cursor.moveToFirst()){
//            place.setCityName(cursor.getString(cityNameIndex));
//            place.setCountryName(cursor.getString(countryNameIndex));
//            place.setCoordLat(cursor.getFloat(coordLatIndex));
//            place.setCoordLong(cursor.getFloat(coordLonIndex));
//            place.setTodayLastUpdate(cursor.getLong(todayLastUpdateIndex));
//            place.setForecastLastUpdate(cursor.getLong(forecastLastUpdateIndex));
//        }
//    }
//
//    @Override
//    public void updateCurrentLocation(TodayWeatherRequest todayWeatherRequest) {
//        long locationId = todayWeatherRequest.getId();
//        String cityName = todayWeatherRequest.getName();
//        String countryName = todayWeatherRequest.getSys().getCountry();
//        float coordLon = todayWeatherRequest.getCoord().getLon();
//        float coordLat = todayWeatherRequest.getCoord().getLat();
//        long todayLastUdpate = System.currentTimeMillis();
//        long forecastLastUpdate = 0;
//
//        Place place = placeList.get(0);
//        place.setLocationId(locationId);
//        place.setCityName(cityName);
//        place.setCountryName(countryName);
//        place.setCoordLong(coordLon);
//        place.setCoordLat(coordLat);
//        place.setTodayLastUpdate(todayLastUdpate);
//        place.setForecastLastUpdate(forecastLastUpdate);
//
//        updateDrawerItems();
//    }
//
//
//
//
//
//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
//        switch (i){
//            case ID_LOADER:
//                Uri uri = Contract.ForecastWeatherEntry.CONTENT_URI
//                        .buildUpon()
//                        .appendPath(Long.toString(placeList.get(displayingPlaceIndex).getPlaceId()))
//                        .build();
//                String sortOrder= Contract.ForecastWeatherEntry.COLUMN_DATE + " ASC";
//
//                return new CursorLoader(this,
//                        uri,
//                        null,
//                        null,
//                        null,
//                        sortOrder);
//            default:
//                throw new RuntimeException("Loader Not Implemented: " + i);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
//        adapter.swapCursor(cursor);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        adapter.swapCursor(null);
//    }
//
//    public static void attachController(MyPresenter contr) {
//        controller = contr;
//    }
//    }