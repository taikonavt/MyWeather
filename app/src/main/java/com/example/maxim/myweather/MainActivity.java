package com.example.maxim.myweather;

import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String LAST_LOCATION_KEY = "last_location_key";
    private static final String CITY_FOR_FIRST_START = "Moscow";
    private static final String UNKNOWN_CURRENT_LOCATION = "unknown";
    private ArrayList<String> locationList;
    private int displayingLocation;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationList = getLocations();
        displayingLocation = getLastDisplayingLocation();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(locationList.get(displayingLocation));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateDrawersItem();

        startService(new Intent(MainActivity.this, SyncIntentService.class));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_ten_days_forecast);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ForecastListAdapter adapter = new ForecastListAdapter(getFakeForecast());
        recyclerView.setAdapter(adapter);
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
                displayingLocation = 0;
                break;
            default:
                displayingLocation = id; // здесь id от 1 и дальше
        }

        navigationView.post(onNavChange);
        getSupportActionBar().setTitle(locationList.get(displayingLocation));

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
        menuLocation.getItem(0).setTitle(locationList.get(0));

        MenuItem favourites = menu.getItem(FAVOURITES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);
        for (int i = 1; i < locationList.size(); i++) {
//            final int ITEM_ID = i;
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, i, ITEM_ORDER + i, locationList.get(i))
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
        for (int i = 0; i < locationList.size() - 1; i++) {
            navigationView.getMenu()
                    .getItem(1)
                    .getSubMenu()
                    .getItem(i)
                    .setChecked(false);
        }
    }

    private ArrayList<String> getLocations(){
        ArrayList<String> locationList = new ArrayList<>();
        locationList.add(0, getCurrentLocation());
        getLocationsFromDB(locationList);

        if (locationList.size() == 1 && locationList.get(0).equals(UNKNOWN_CURRENT_LOCATION))
            locationList.add(1, CITY_FOR_FIRST_START);
        return locationList;
    }

    private String getCurrentLocation() {
        return "Current Location";
    }

    private ArrayList<String> getLocationsFromDB(ArrayList<String> arrayList) {
        arrayList.add(1, "St.Petersburg"); // index > 0 favourite
        arrayList.add(2, "N.Novgorod");
        arrayList.add(3, "Moscow");
        return arrayList;
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
                locationList.get(displayingLocation));
    }

    public int getLastDisplayingLocation() {
        AppPreferences preferences = new AppPreferences(this);
        String string = preferences.getPreference(LAST_LOCATION_KEY, CITY_FOR_FIRST_START);
        return locationList.indexOf(string);
    }
}
