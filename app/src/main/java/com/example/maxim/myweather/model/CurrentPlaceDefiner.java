package com.example.maxim.myweather.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.maxim.myweather.common.Place;
import com.example.maxim.myweather.presenter.MyPresenter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static android.content.Context.LOCATION_SERVICE;

class CurrentPlaceDefiner {
    private Context context;
    private MyModel model;
    private MyPresenter presenter;
    private Place place;

    CurrentPlaceDefiner(MyModel model){
        this.model = model;
        this.presenter = model.getPresenter();
        this.context = presenter.getAppContext();
        place = new Place();
    }

    public void updateCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        presenter.requestLocationPermissions();
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            Location location = locationManager.getLastKnownLocation(provider);
            setLocation(location);

            locationManager.requestSingleUpdate(provider, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setLocation(location);
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

    public static final int SCALE = 2;
    private void setLocation(Location location){
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float latF = (new BigDecimal(lat).setScale(SCALE, RoundingMode.HALF_EVEN)).floatValue();
        float lonF = (new BigDecimal(lon).setScale(SCALE, RoundingMode.HALF_EVEN)).floatValue();
        place.setCoordLat(latF);
        place.setCoordLong(lonF);
        model.setCurrentPlace(place);
    }


    interface OnLocationChangedListener{
        void setCurrentPlace(Place place);
    }
}
