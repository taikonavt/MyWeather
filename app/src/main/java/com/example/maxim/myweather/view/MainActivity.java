package com.example.maxim.myweather.view;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.maxim.myweather.ForecastListAdapter;
import com.example.maxim.myweather.R;
import com.example.maxim.myweather.model.MyModel;
import com.example.maxim.myweather.presenter.MainPresenter;
import com.example.maxim.myweather.presenter.MyPresenter;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
//                    Network.DbCallable, LoaderManager.LoaderCallbacks<Cursor>,
        MyActivity{

    public static final String TAG = "myTag";
    public static final String CLASS = MainActivity.class.getSimpleName() + " ";


// above checked constants

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

        initView();

        MainPresenter presenter = new MainPresenter();
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
//        navigationView.setNavigationItemSelectedListener(this);
//        updateDrawerItems();

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
//            Intent intent = new Intent(this, PreferenceActivity.class);
//            startActivity(intent);
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    final int NEW_LOCATION_REQUEST_CODE = 121;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//
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
        return true;
    }



//    private void updateInfo() {
//        long locationId = placeList.get(displayingLocationIndex).getLocationId();
//        Network.getInstance().requestTodayWeather(locationId);
//        Network.getInstance().requestForecastWeather(locationId);
//        getSupportLoaderManager().restartLoader(ID_LOADER, null, this);
//
//        navigationView.post(onNavChange);
//        getSupportActionBar().setTitle(placeList.get(displayingLocationIndex).getCityName());
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case NEW_LOCATION_REQUEST_CODE: {
//                    Place place = (Place) data.getParcelableExtra(SearchActivity.PLACE_KEY);
//                    addFavoriteLocation(place);
//                    getFavoriteLocations(placeList);
//                    displayingLocationIndex = placeList.size() - 1;
//                    updateDrawerItems();
//                    updateInfo();
//                }
//            }
//        }
//    }
//
//    private void updateDrawerItems() {
//        int LOCATION_ID = 0;
//        int FAVOURITES_ID = 1;
//        int GROUP_ID = 1;
//        int ITEM_ORDER = 100;
//
//        final Menu menu = navigationView.getMenu();
//        MenuItem location = menu.getItem(LOCATION_ID);
//        Menu menuLocation = location.getSubMenu();
//        menuLocation.getItem(0).setTitle(placeList.get(0).getCityName());
//
//        MenuItem favourites = menu.getItem(FAVOURITES_ID);
//        Menu menuFavourites = favourites.getSubMenu();
//        menuFavourites.removeGroup(GROUP_ID);
//        for (int i = 1; i < placeList.size(); i++) {
//            final MenuItem menuItem = menuFavourites
//                    .add(GROUP_ID, i, ITEM_ORDER + i, placeList.get(i).getCityName())
//                    .setIcon(R.drawable.ic_place_black_24dp)
//                    .setActionView(R.layout.action_view_delete);
//
//            View actionView = menuItem.getActionView();
//            ImageView imageView = (ImageView) actionView.findViewById(R.id.iv_delete);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // TODO: 20.07.18 Realize deleting
//                    Log.d(TAG, "clicked: " + menuItem.getItemId());
//                    int id = menuItem.getItemId();
//                    deleteFavoriteLocation(placeList.get(id));
//                    if (displayingLocationIndex == id)
//                        displayingLocationIndex = 0;
//                    else if (displayingLocationIndex > id)
//                        displayingLocationIndex--;
//                    getFavoriteLocations(placeList);
//                    updateDrawerItems();
//                }
//            });
//        }
//
//        int addNewLocationBtnId = placeList.size();
//        menuFavourites
//                .add(GROUP_ID, addNewLocationBtnId,
//                        ITEM_ORDER + addNewLocationBtnId,
//                        R.string.add_new_location)
//                .setIcon(R.drawable.ic_add_black_24dp);
//        navigationView.post(onNavChange);
//    }
//
//    private Runnable onNavChange = new Runnable() {
//        @Override
//        public void run() {
//            if (displayingLocationIndex == 0){
//                clearCheckedPosition();
//                MenuItem menuItem = navigationView.getMenu()
//                        .getItem(0)
//                        .getSubMenu()
//                        .getItem(0);
//                menuItem.setChecked(true);
//            }
//            else if (displayingLocationIndex <= placeList.size()){
//                clearCheckedPosition();
//                MenuItem menuItem = navigationView.getMenu()
//                        .getItem(1)
//                        .getSubMenu()
//                        .getItem(displayingLocationIndex -1);
//                menuItem.setChecked(true);
//            }
//        }
//    };
//
//    private void clearCheckedPosition(){
//        navigationView.getMenu()
//                .getItem(0)
//                .getSubMenu()
//                .getItem(0)
//                .setChecked(false);
//        for (int i = 0; i < placeList.size() - 1; i++) {
//            navigationView.getMenu()
//                    .getItem(1)
//                    .getSubMenu()
//                    .getItem(i)
//                    .setChecked(false);
//        }
//    }
//
//
//
//    // последний гарантированно вызываемый метод перед закрытием
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        AppPreferences preferences = new AppPreferences(this);
//        preferences.savePreference(LAST_LOCATION_KEY,
//                placeList.get(displayingLocationIndex).getCityName());
//    }




}
