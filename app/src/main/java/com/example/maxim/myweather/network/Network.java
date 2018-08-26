package com.example.maxim.myweather.network;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.maxim.myweather.AppPreferences;
import com.example.maxim.myweather.MainActivity;
import com.example.maxim.myweather.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network instance = null;
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private OpenWeather openWeather;
    private AppCompatActivity activity;

    private Network(AppCompatActivity activity){
        this.activity = activity;

        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    public static void initNetwork(AppCompatActivity activity){
        instance = new Network(activity);
    }

    public static Network getInstance() {
        return instance;
    }

    public void requestTodayWeather(String coordLat, String coordLon){
        AppPreferences preferences = new AppPreferences(activity);
        String units = preferences.getPreference(AppPreferences.UNITS_KEY, AppPreferences.UNITS_METRIC);
        String keyApi = preferences.getPreference(AppPreferences.API_KEY, AppPreferences.MY_API);

        openWeather.loadTodayWeather(coordLat, coordLon, units, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            MainActivity mainActivity = (MainActivity) activity;
                            mainActivity.sendToDbTodayWeather(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Toast.makeText(activity, activity.getString(R.string.network_error),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void requestTodayWeather(long locationId){
        AppPreferences appPreferences = new AppPreferences(activity);
        String units = appPreferences.getPreference(AppPreferences.UNITS_KEY, AppPreferences.UNITS_METRIC);
        String keyApi = appPreferences.getPreference(AppPreferences.API_KEY, AppPreferences.MY_API);

        openWeather.loadTodayWeather(locationId, units, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            MainActivity mainActivity = (MainActivity) activity;
                            mainActivity.sendToDbTodayWeather(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Toast.makeText(activity, activity.getString(R.string.network_error),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public interface DbCallable{
        void sendToDbTodayWeather(WeatherRequest weatherRequest);
        void sendToDbForecastWeather(WeatherRequest weatherRequest);
    }
}


