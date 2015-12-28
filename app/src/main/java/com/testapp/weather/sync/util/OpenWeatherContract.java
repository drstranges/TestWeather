package com.testapp.weather.sync.util;

import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.testapp.weather.BuildConfig;

/**
 * Contract for use OpenWeatherMap Api
 * Created on 23.12.2015.
 */
public final class OpenWeatherContract {
    public static final String ROOT_URL = "http://api.openweathermap.org/";
    public static final String BASE_URL_DAILY_FORECAST = ROOT_URL + "data/2.5/forecast/daily?";

    public static final String PARAM_QUERY = "q";
    public static final String PARAM_FORMAT = "mode";
    public static final String PARAM_UNITS = "units";
    public static final String PARAM_DAYS = "cnt";
    public static final String PARAM_APPID = "APPID";

    //Json Items (see http://api.openweathermap.org/data/2.5/forecast/daily?q=London&mode=json&units=metric&cnt=7)
    // The temperature description are children of an array in the "list" object
    public static final String OWM_LIST = "list";
    public static final String OWM_DATETIME = "dt";
    public static final String OWM_PRESSURE = "pressure";
    public static final String OWM_HUMIDITY = "humidity";
    public static final String OWM_SPEED = "speed";
    public static final String OWM_DEG = "deg";
    public static final String OWM_CLOUDS = "clouds";
    public static final String OWM_RAIN = "rain";
    // The wind description are children of the "temp" object
    public static final String OWM_TEMP = "temp";
    public static final String OWM_TEMP_DAY = "day";
    public static final String OWM_TEMP_MIN = "min";
    public static final String OWM_TEMP_MAX = "max";
    public static final String OWM_TEMP_NIGHT = "night";
    // The weather description are children of the "weather" object
    public static final String OWM_WEATHER = "weather";
    public static final String OWM_WEATHER_ID = "id";
    public static final String OWM_WEATHER_MAIN = "main";
    public static final String OWM_WEATHER_DESCRIPTION = "description";
    public static final String OWM_WEATHER_ICON = "icon";

    public static String getDailyForecastUrl(@NonNull String _location, @IntRange(from = 1, to = 16) int _days) {
        Uri uri = Uri.parse(BASE_URL_DAILY_FORECAST).buildUpon()
                .appendQueryParameter(PARAM_QUERY, _location)
                .appendQueryParameter(PARAM_FORMAT, "json")
                .appendQueryParameter(PARAM_UNITS, "metric")
                .appendQueryParameter(PARAM_DAYS, Integer.toString(_days))
                .appendQueryParameter(PARAM_APPID, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                .build();
        return uri.toString();
    }

}
