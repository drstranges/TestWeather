package com.testapp.weather.model;


import android.support.annotation.NonNull;

import com.testapp.weather.sync.util.OpenWeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the weather forecast for a certain place and date
 * Data takes from json Object like this:
 {
 "list": [
 {
 "dt": 1450868400,
 "temp": {
 "day": 9.5,
 "min": 9.45,
 "max": 10.99,
 "night": 10.99,
 "eve": 9.5,
 "morn": 9.5
 },
 "pressure": 1026.97,
 "humidity": 83,
 "weather": [
 {
 "id": 500,
 "main": "Rain",
 "description": "light rain",
 "icon": "10d"
 }
 ],
 "speed": 6.17,
 "deg": 196,
 "clouds": 36,
 "rain": 0.33
 }]}
 * Temperature in Kelvin (subtract 273.15 to convert to Celsius)
 * Created on 23.12.2015.
 */
public class ForecastItem {
    public Long id;
    public long dateTime;       // dataTime in milliseconds
    public int weatherId;       // Weather condition codes
    public String shortDescription;  // Short weather condition description
    public String description;  // Weather condition description
    public String iconName;     // name of icon to show a weather condition
    public double pressure;     // Atmospheric pressure, hPa
    public int humidity;        // Humidity, %
    public double windSpeed;    // Wind speed, mps
    public double windDirection; // Wind direction, degrees (meteorological)
    public double maxTemp;      // Maximum temperature at the moment.
    public double minTemp;      // Minimum temperature at the moment.

    public ForecastItem() {}

    private ForecastItem(JSONObject _jsonObject) throws JSONException {
        parseJsonWeather(_jsonObject);
    }

    public static List<ForecastItem> from(@NonNull JSONObject _jsonObject) throws JSONException {
        List<ForecastItem> list = new ArrayList<>();
        if (_jsonObject.has(OpenWeatherContract.OWM_LIST)) {
            JSONArray jsonArray = _jsonObject.getJSONArray(OpenWeatherContract.OWM_LIST);
            for (int index = 0; index < jsonArray.length(); index++) {
                list.add(new ForecastItem(jsonArray.getJSONObject(index)));
            }
        } else {
            list.add(new ForecastItem(_jsonObject));
        }
        return list;
    }

    public void parseJsonWeather(JSONObject _jsonForecast) throws JSONException {

        // The date/time is returned as a long in unixtime GMT (for convert multiply 1000).
        dateTime = _jsonForecast.getLong(OpenWeatherContract.OWM_DATETIME) * 1000;
        pressure = _jsonForecast.getDouble(OpenWeatherContract.OWM_PRESSURE);
        humidity = _jsonForecast.getInt(OpenWeatherContract.OWM_HUMIDITY);

        windSpeed = _jsonForecast.getDouble(OpenWeatherContract.OWM_SPEED);
        windDirection = _jsonForecast.getDouble(OpenWeatherContract.OWM_DEG);
        // Description is in a child array called "weather", which is 1 element long.
        JSONObject weatherObject =
                _jsonForecast.getJSONArray(OpenWeatherContract.OWM_WEATHER).getJSONObject(0);
        weatherId = weatherObject.getInt(OpenWeatherContract.OWM_WEATHER_ID);
        shortDescription = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_MAIN);
        description = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_DESCRIPTION);
        iconName = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_ICON);

        // Temperatures are in a child object called "main".
        JSONObject temperatureObject =
                _jsonForecast.getJSONObject(OpenWeatherContract.OWM_TEMP);
        maxTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_TEMP_MAX);
        minTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_TEMP_MIN);
    }

    public String getWindDirection() {
        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        String direction = "";
        if (windDirection >= 337.5 || windDirection < 22.5) {
            direction = "N";
        } else if (windDirection >= 22.5 && windDirection < 67.5) {
            direction = "NE";
        } else if (windDirection >= 67.5 && windDirection < 112.5) {
            direction = "E";
        } else if (windDirection >= 112.5 && windDirection < 157.5) {
            direction = "SE";
        } else if (windDirection >= 157.5 && windDirection < 202.5) {
            direction = "S";
        } else if (windDirection >= 202.5 && windDirection < 247.5) {
            direction = "SW";
        } else if (windDirection >= 247.5 && windDirection < 292.5) {
            direction = "W";
        } else if (windDirection >= 292.5 || windDirection < 337.5) {
            direction = "NW";
        }
        return direction;
    }

    public double getTemperatureInCelsius(double temp) {
        return temp - 273.15;
    }

    @Override
    public String toString() {
        return "ForecastItem{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", weatherId=" + weatherId +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", iconName='" + iconName + '\'' +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                '}';
    }
}
