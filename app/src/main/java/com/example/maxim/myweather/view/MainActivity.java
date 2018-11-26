package com.example.maxim.myweather.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.myweather.ForecastListAdapter;
import com.example.maxim.myweather.R;
import com.example.maxim.myweather.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        ExplainPermissionDialogFragment.OnButtonPermissionDialogListener,
        MyActivity{

    public static final String TAG = "myTag";
    public static final String CLASS = MainActivity.class.getSimpleName() + " ";

    private static final int PERMISSION_REQUEST_CODE = 10;

    private MainPresenter presenter;

    private TextView tvTodayTemp;
    private TextView tvTodayWeatherType;
    private TextView tvTodayWind;
    private TextView tvTodayHumidity;
    private NavigationView navigationView;


    private ForecastListAdapter forecastListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        presenter = new MainPresenter();
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvTodayTemp = (TextView) findViewById(R.id.tv_main_info_field_temperature);
        tvTodayHumidity = (TextView) findViewById(R.id.tv_main_info_field_humidity);
        tvTodayWeatherType = (TextView) findViewById(R.id.tv_main_info_field_weather_type);
        tvTodayWind = (TextView) findViewById(R.id.tv_main_info_field_wind);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_ten_days_forecast);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        forecastListAdapter = new ForecastListAdapter();
        recyclerView.setAdapter(forecastListAdapter);
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
            presenter.onSettingsSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        if (id == R.id.current_place){
            presenter.onNavigationCurrentPlaceSelected();
        } else {
            presenter.onNavigationItemSelected(id);
        }
        return true;
    }

    public void setTitle(String string){
        getSupportActionBar().setTitle(string);
    }

    public void setNavigationCurrentPlaceChecked(){
        navigationView.post(new Runnable() {
            @Override
            public void run() {
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(0)
                        .getSubMenu()
                        .getItem(0);
                menuItem.setChecked(true);
            }
        });
    }

    public void setNavigationPlaceChecked(final int position){
        navigationView.post(new Runnable() {
            @Override
            public void run() {
                clearCheckedPosition();
                MenuItem menuItem = navigationView.getMenu()
                        .getItem(1)
                        .getSubMenu()
                        .getItem(position -1);
                menuItem.setChecked(true);
            }
        });
    }

    public void updateNavigatorCurrentPlace(String cityName){
        final int CURRENT_PLACE_MENU_ID = 0;
        final int CURRENT_PLACE_ITEM_ID = 0;

        final Menu menu = navigationView.getMenu();
        MenuItem location = menu.getItem(CURRENT_PLACE_MENU_ID);
        Menu menuLocation = location.getSubMenu();
        menuLocation.getItem(CURRENT_PLACE_ITEM_ID).setTitle(cityName);
    }

    public void updateNavigatorFavouritePlaces(String[] cityNames){
        final int FAVOURITES_PLACES_ID = 1;
        final int GROUP_ID = 1;
        final int ITEM_ORDER = 100;

        final Menu menu = navigationView.getMenu();
        MenuItem favourites = menu.getItem(FAVOURITES_PLACES_ID);
        Menu menuFavourites = favourites.getSubMenu();
        menuFavourites.removeGroup(GROUP_ID);

        for (int i = 0; i < cityNames.length; i++) {
            final int ITEM_ID = i;
            final MenuItem menuItem = menuFavourites
                    .add(GROUP_ID, ITEM_ID, ITEM_ORDER + i, cityNames[i])
                    .setIcon(R.drawable.ic_place_black_24dp)
                    .setActionView(R.layout.action_view_delete);

            View actionView = menuItem.getActionView();
            ImageView imageView = (ImageView) actionView.findViewById(R.id.iv_delete);
            OnDeleteFavouritePlaceItemListener listener = new OnDeleteFavouritePlaceItemListener(ITEM_ID);
            imageView.setOnClickListener(listener);
        }

        int addNewLocationBtnId = cityNames.length;
        menuFavourites
                .add(GROUP_ID, addNewLocationBtnId,
                        ITEM_ORDER + addNewLocationBtnId,
                        R.string.add_new_location)
                .setIcon(R.drawable.ic_add_black_24dp);
    }

    private void clearCheckedPosition(){
        navigationView.getMenu()
                .getItem(0)
                .getSubMenu()
                .getItem(0)
                .setChecked(false);

        SubMenu subMenu = navigationView.getMenu().getItem(1).getSubMenu();
        for (int i = 0; i < subMenu.size(); i++) {
            subMenu.getItem(i)
                   .setChecked(false);
        }
    }

    // последний гарантированно вызываемый метод перед закрытием
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.destroy();
    }

    public void updateTodayWeather(String temp, String type, String wind, String humidity) {
        tvTodayTemp.setText(temp);
        tvTodayWeatherType.setText(type);
        tvTodayWind.setText(wind);
        tvTodayHumidity.setText(humidity);
    }

    public ForecastListAdapter getForecastListAdapter() {
        return forecastListAdapter;
    }

    public void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            DialogFragment dialogFragment = new ExplainPermissionDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "DialogFragment");
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getResources().getString(R.string.access_granted),
                            Toast.LENGTH_LONG).show();
                    presenter.accessToLocationGranted();
                } else
                    Toast.makeText(this, getResources().getString(R.string.access_denied),
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onOkButtonPermissionDialogFragmentClick() {
        requestPermission();
    }

    private class OnDeleteFavouritePlaceItemListener implements View.OnClickListener {
        private int id;

        OnDeleteFavouritePlaceItemListener(int id){
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            presenter.deleteFavouritePlace(id);
        }
    }
}
