package com.example.maxim.myweather;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class SyncIntentService extends IntentService implements SensorEventListener{

    private static final String TAG = SyncIntentService.class.getSimpleName();

    private SensorManager sensorManager;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;

    public SyncIntentService() {
        super(AppContext.getInstance().getString(R.string.intent_service_name));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MyWeatherSyncTask.syncWeather(this);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        sensorManager.registerListener(listenerTemperature, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(listenerHumidity, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);

        return super.onStartCommand(intent, flags, startId);
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
    }

    private void writeData(float[] values) {

        try {
            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            intent.putExtra(MainActivity.SENSOR_VAL, values[0]);
            sendBroadcast(intent);
        } catch (Throwable t1) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t1.toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            writeData(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
