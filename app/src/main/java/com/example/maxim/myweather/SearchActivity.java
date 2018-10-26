package com.example.maxim.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import static java.lang.Thread.sleep;

public class SearchActivity extends AppCompatActivity
            implements SearchView.OnQueryTextListener{
    public static final String TAG = "myTag";
    public static final String PLACE_KEY = "place";

    SearchAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_search);
        recyclerView = (RecyclerView) findViewById(R.id.rv_search);
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
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Place[] result = getFakeData();
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.swap(result);
    }

    private void sendResult(Place place){
        Intent intent = new Intent();
        intent.putExtra(PLACE_KEY, place);
        setResult(RESULT_OK, intent);
        finish();
    }

    public Place[] getFakeData() {
        Place[] fakeData = new Place[3];

        Place place = new Place();
        place.setCityName("Firozpur Jhirka");
        place.setCountryName("IN");
        place.setLocationId(1271881);
        place.setCoordLat(76.949997f);
        place.setCoordLong(76.949997f);
        place.setTodayLastUpdate(0);
        place.setForecastLastUpdate(0);
        fakeData[0] = place;

        Place place1 = new Place();
        place1.setCityName("Kathmandu");
        place1.setCountryName("NP");
        place1.setLocationId(1283240);
        place1.setCoordLat(27.716667f);
        place1.setCoordLong(85.316666f);
        place1.setTodayLastUpdate(0);
        place1.setForecastLastUpdate(0);
        fakeData[1] = place1;

        Place place2 = new Place();
        place2.setCityName("Merida");
        place2.setCountryName("VE");
        place2.setLocationId(3632308);
        place2.setCoordLat(8.598333f);
        place2.setCoordLong(-71.144997f);
        place2.setTodayLastUpdate(0);
        place2.setForecastLastUpdate(0);
        fakeData[2] = place2;

        return fakeData;
    }
}
