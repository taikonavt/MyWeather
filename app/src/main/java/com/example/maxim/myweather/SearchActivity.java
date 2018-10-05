package com.example.maxim.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;

public class SearchActivity extends AppCompatActivity
            implements SearchView.OnQueryTextListener{
    public static final String TAG = "myTag";
    public static final String REQUEST_NEW_LOCATION_KEY = "new_location";
    public static final String NAME_KEY = "name";
    public static final String ID_KEY = "id";

    SearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_search);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new SearchAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Place place) {
                sendResult(place);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 2){
            doMySearch(s);
        }
        return false;
    }

    private void doMySearch(String string){
        Place[] result = getFakeData();
        adapter.swap(result);
    }

    private void sendResult(Place place){
        Intent intent = new Intent();
        intent.putExtra(NAME_KEY, place.getCityName());
        intent.putExtra(ID_KEY, place.getLocationId());
        setResult(RESULT_OK, intent);
        finish();
    }

    public Place[] getFakeData() {
        Place[] fakeData = new Place[2];

        Place place = new Place();
        place.setCityName("Firozpur Jhirka");
        place.setLocationId(1271881);
        place.setCoordLat(76.949997f);
        place.setCoordLong(76.949997f);
        place.setTodayLastUpdate(0);
        place.setForecastLastUpdate(0);
        fakeData[0] = place;

        Place place1 = new Place();
        place1.setCityName("N.Novgorod");
        place1.setLocationId(1234567);
        place1.setCoordLat(72.45467f);
        place1.setCoordLong(12.785624f);
        place1.setTodayLastUpdate(0);
        place1.setForecastLastUpdate(0);
        fakeData[1] = place1;

        return fakeData;
    }
}
