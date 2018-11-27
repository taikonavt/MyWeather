package com.example.maxim.myweather.model;

import android.util.Log;

import com.example.maxim.myweather.network.OpenWeather;
import com.example.maxim.myweather.network.forecast.ForecastWeatherRequest;
import com.example.maxim.myweather.network.today.TodayWeatherRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Network {
    static final String TAG = "myTag";
    static final String CLASS = Network.class.getSimpleName() + " ";

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private final int forecastDays = 10;
    private OpenWeather openWeather;
    private MyModel model;

    Network(MyModel model){
        this.model = model;

        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    void requestTodayWeather(float coordLat, float coordLon){
        AppPreferences preferences = new AppPreferences(model.getPresenter().getAppContext());
        String units = preferences.getUnits();
        String keyApi = preferences.getMyApi();

        openWeather.loadTodayWeather(coordLat, coordLon, units, keyApi)
                .enqueue(new Callback<TodayWeatherRequest>() {
                    @Override
                    public void onResponse(Call<TodayWeatherRequest> call, Response<TodayWeatherRequest> response) {
                        if (response.body() != null) {
                            model.onCurrentPlaceDataReceive(response.body());
                            model.onTodayWeatherDataReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TodayWeatherRequest> call, Throwable t) {
                        model.onRequestFailure();
                        Log.d(TAG, CLASS + "requestTodayWeather(); onFailure(); " + t);
                    }
                });
    }

    void requestForecastWeather(float coordLat, float coordLon){
        AppPreferences preferences = new AppPreferences(model.getPresenter().getAppContext());
        String units = preferences.getUnits();
        String keyApi = preferences.getMyApi();

        openWeather.loadForecastWeather(coordLat, coordLon, units, forecastDays, keyApi)
                .enqueue(new Callback<ForecastWeatherRequest>() {
                    @Override
                    public void onResponse(Call<ForecastWeatherRequest> call, Response<ForecastWeatherRequest> response) {
                        if (response.body() != null) {
                            model.onForecastWeatherDataReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeatherRequest> call, Throwable t) {
                        model.onRequestFailure();
                        Log.d(TAG, CLASS + "requestTodayWeather(); onFailure(); " + t);
                    }
                });
    }

    void requestForecastWeather(long locationId){
        AppPreferences preferences = new AppPreferences(model.getPresenter().getAppContext());
        String units = preferences.getUnits();
        String keyApi = preferences.getMyApi();

        openWeather.loadForecastWeather(locationId, units, forecastDays, keyApi)
                .enqueue(new Callback<ForecastWeatherRequest>() {
                    @Override
                    public void onResponse(Call<ForecastWeatherRequest> call, Response<ForecastWeatherRequest> response) {
                        if (response.body() != null) {
                            model.onForecastWeatherDataReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeatherRequest> call, Throwable t) {
                        model.onRequestFailure();
                        Log.d(TAG, CLASS + "requestTodayWeather(); onFailure(); " + t);
                    }
                });
    }

    void requestTodayWeather(long locationId){
        AppPreferences preferences = new AppPreferences(model.getPresenter().getAppContext());
        String units = preferences.getUnits();
        String keyApi = preferences.getMyApi();

        openWeather.loadTodayWeather(locationId, units, keyApi)
                .enqueue(new Callback<TodayWeatherRequest>() {
                    @Override
                    public void onResponse(Call<TodayWeatherRequest> call, Response<TodayWeatherRequest> response) {
                        if (response.body() != null) {
                            model.onTodayWeatherDataReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TodayWeatherRequest> call, Throwable t) {
                        model.onRequestFailure();
                        Log.d(TAG, CLASS + "requestTodayWeather(); onFailure(); " + t);
                    }
                });
    }

//    public void requestTodayWeather(String cityName){
//        AppPreferences appPreferences = new AppPreferences(activity);
//        String units = appPreferences.getPreference(AppPreferences.UNITS_KEY, AppPreferences.UNITS_METRIC);
//        String keyApi = appPreferences.getPreference(AppPreferences.API_KEY, AppPreferences.MY_API);
//
//        openWeather.loadTodayWeather(cityName, units, keyApi)
//                .enqueue(new Callback<ForecastWeatherRequest>() {
//                    @Override
//                    public void onResponse(Call<ForecastWeatherRequest> call, Response<ForecastWeatherRequest> response) {
//                        if (response.body() != null) {
//                            MainActivity mainActivity = (MainActivity) activity;
//                            mainActivity.sendToDbNewLocation(response.body());
//                            mainActivity.sendToDbTodayWeather(response.body());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ForecastWeatherRequest> call, Throwable t) {
//                        Toast.makeText(activity, activity.getString(R.string.network_error),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

    public interface OnWeatherDataReceiveListener {
//        void updateCurrentLocation(TodayWeatherRequest todayWeatherRequest);
//        void sendToDbNewLocation(ForecastWeatherRequest todayWeatherRequest);
        void onTodayWeatherDataReceive(TodayWeatherRequest todayWeatherRequest);
        void onCurrentPlaceDataReceive(TodayWeatherRequest todayWeatherRequest);
        void onForecastWeatherDataReceive(ForecastWeatherRequest forecastWeatherRequest);
        void onRequestFailure();
    }
}


