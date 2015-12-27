package com.testapp.weather.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.text.format.DateUtils;

import com.testapp.weather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Helper class to provide resource id according to the weather condition
 * Created by d_rom on 25.12.2015.
 */
public class ForecastUtils {
    public static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();

    public enum WeatherCondition {
        THUNDERSTORM(R.drawable.ic_weather_lightning, R.color.colorWeatherStorm),
        DRIZZLE(R.drawable.ic_weather_drizzle, R.color.colorWeatherDrizzle),
        RAIN(R.drawable.ic_weather_rain, R.color.colorWeatherPouring),
        SNOW(R.drawable.ic_weather_snow, R.color.colorWeatherSnowy),
        FOG(R.drawable.ic_weather_fog, R.color.colorWeatherFog),
        HAIL(R.drawable.ic_weather_hail, R.color.colorWeatherHail),
        CLEAR(R.drawable.ic_weather_clear, R.color.colorWeatherClear),
        CLOUDS(R.drawable.ic_weather_cloudy, R.color.colorWeatherCloudy),
        PARTLY_CLOUDS(R.drawable.ic_weather_partlycloudy, R.color.colorWeatherCloudy),
        STORM(R.drawable.ic_weather_windy, R.color.colorWeatherWindy);

        private final int mIconResId;
        private final int mColorResId;

        WeatherCondition(@DrawableRes int _iconResId, @ColorRes int _colorResId) {
            mIconResId = _iconResId;
            mColorResId = _colorResId;
        }

        public int getIconResId() {
            return mIconResId;
        }
        public int getColorResId() {
            return mColorResId;
        }
    }

    // See http://openweathermap.org/weather-conditions
    public static WeatherCondition getWeatherCondition(int _weatherId) {

        if (_weatherId >= 200 && _weatherId < 300) { //Thunderstorm
            return WeatherCondition.THUNDERSTORM;
        } else if (_weatherId >= 300 && _weatherId < 400) { //Drizzle
            return WeatherCondition.DRIZZLE;
        } else if (_weatherId >= 500 && _weatherId < 600) { //Rain
            return WeatherCondition.RAIN;
        } else if (_weatherId >= 600 && _weatherId < 700 || _weatherId == 903) { //Snow
            return WeatherCondition.SNOW;
        } else if (_weatherId >= 701 && _weatherId <= 761) { //Fog
            return WeatherCondition.FOG;
        } else if (_weatherId == 761 || _weatherId == 781
                || (_weatherId >= 900 && _weatherId <= 902) || _weatherId == 905
                || (_weatherId >= 956 && _weatherId <= 962)) { //storm
            return WeatherCondition.STORM;
        } else if (_weatherId == 800 || _weatherId == 904 || (_weatherId >= 951 && _weatherId <= 955)) { //Clear
            return WeatherCondition.CLEAR;
        } else if (_weatherId == 801) { //few clouds
            return WeatherCondition.PARTLY_CLOUDS;
        } else if (_weatherId >= 802 && _weatherId <= 804) { //Clouds
            return WeatherCondition.CLOUDS;
        } else if (_weatherId == 906) { //hail
            return WeatherCondition.HAIL;
        }
        return WeatherCondition.CLOUDS;
    }

    public String getWindDirection(double _windDirection) {
        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        String direction = "";
        if (_windDirection >= 337.5 || _windDirection < 22.5) {
            direction = "N";
        } else if (_windDirection >= 22.5 && _windDirection < 67.5) {
            direction = "NE";
        } else if (_windDirection >= 67.5 && _windDirection < 112.5) {
            direction = "E";
        } else if (_windDirection >= 112.5 && _windDirection < 157.5) {
            direction = "SE";
        } else if (_windDirection >= 157.5 && _windDirection < 202.5) {
            direction = "S";
        } else if (_windDirection >= 202.5 && _windDirection < 247.5) {
            direction = "SW";
        } else if (_windDirection >= 247.5 && _windDirection < 292.5) {
            direction = "W";
        } else if (_windDirection >= 292.5 || _windDirection < 337.5) {
            direction = "NW";
        }
        return direction;
    }

    public static double kelvinToCelsius(double temp) {
        return temp - 273.15;
    }

    public static CharSequence getRelativeDate(Context _context, long _timeMillis) {
//        return DateUtils.getRelativeDateTimeString(_context, _timeMillis, DateUtils.HOUR_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE);
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(_timeMillis, now, DateUtils.DAY_IN_MILLIS);
    }
}
