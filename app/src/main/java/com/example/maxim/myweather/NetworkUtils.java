package com.example.maxim.myweather;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String FORECAST_BASE_URL = "http://somesite.ru/weather";
    private static final String UNITS_PARAM = "units";

    public static URL getUrl(Context context){

        String units = "metric";

        Uri weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            return weatherQueryUrl;
        } catch (MalformedURLException e){
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput){
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
