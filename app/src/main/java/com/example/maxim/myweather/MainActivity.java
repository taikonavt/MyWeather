package com.example.maxim.myweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ExplainPermissionDialogFragment.OnButtonPermissionDialogListener {

    public static final String TAG = "myTag";
    public static final int REQUEST_CODE = 100;
    private ArrayList<String> locationList;
    private int displayingLocation;

    private NavigationView navigationView;
    private TextView tvTemperature;
    private TextView tvHumidity;

    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvTemperature = (TextView) findViewById(R.id.tv_temp);
        tvHumidity = (TextView) findViewById(R.id.tv_hum);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        sensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);

        if (sensorTemperature != null){
            Log.d(TAG, sensorTemperature.toString());
        } else
            Log.d(TAG, "temperature sensor = null");
        if (sensorHumidity != null){
            Log.d(TAG, sensorHumidity.toString());
        } else
            Log.d(TAG, "humidity sensor = null");

        locationList = getFakeArray();
        updateDrawersItem();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestForAccessLocationPermission();
        } else {
            doSomeCode();
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
                displayingLocation = 0;
                break;
            default:
                displayingLocation = id;
        }

        navigationView.post(onNavChange);

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateDrawersItem() {
        int FAVOURITES_ID = 1;
        int GROUP_ID = 1;
        int ITEM_ORDER = 100;

        Menu menu = navigationView.getMenu();
        MenuItem favourites = menu.getItem(FAVOURITES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);
        for (int i = 0; i < locationList.size(); i++) {
            final int ITEM_ID = 1 + i;
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, ITEM_ID, ITEM_ORDER + i, locationList.get(i))
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
    }

    private Runnable onNavChange = new Runnable() {
        @Override
        public void run() {
            if (displayingLocation == 0){
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(0)
                        .getSubMenu()
                        .getItem(0);
                menuItem.setChecked(true);
            }
            else if (displayingLocation <= locationList.size()){
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(1)
                        .getSubMenu()
                        .getItem(displayingLocation-1);
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
        for (int i = 0; i < locationList.size(); i++) {
            navigationView.getMenu()
                    .getItem(1)
                    .getSubMenu()
                    .getItem(i)
                    .setChecked(false);
        }
    }

    private ArrayList<String> getFakeArray(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(0, "Moscow"); // index = 0 currentLocation
        arrayList.add(1, "St.Petersburg"); // index > 0 favourite
        arrayList.add(2, "N.Novgorod");
        return arrayList;
    }

    SensorEventListener listenerTemperature = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            showTempSensor(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void showTempSensor(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Temperature sensor value = ").append(event.values[0])
                .append("\n").append("=================================").append("\n");
        Log.d(TAG, stringBuilder.toString());
        tvTemperature.setText(String.format("%03.1f", event.values[0]));
    }

    SensorEventListener listenerHumidity = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            showHumSensor(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void showHumSensor(SensorEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Humidity sensor value = ").append(event.values[0])
                .append("\n").append("=================================").append("\n");
        Log.d(TAG, stringBuilder.toString());
        tvHumidity.setText(String.format("%03.1f", event.values[0]));
    }

    private void requestForAccessLocationPermission(){
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            DialogFragment dialogFragment = new ExplainPermissionDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "DialogFragment");
        } else {
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    doSomeCode();
                } else
                    Toast.makeText(this, "Access denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doSomeCode(){
        Toast.makeText(this, "Access Coarse Location was granted", Toast.LENGTH_LONG).show();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE);
    }

    @Override
    public void onOkButtonPermissionDialogFragmentClick() {
        requestPermission();
    }
}
