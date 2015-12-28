package com.testapp.weather.sync.util;

import android.support.annotation.NonNull;

import com.testapp.weather.model.ForecastItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing forecast response
 * Created by d_rom on 26.12.2015.
 */
public final class ParseUtils {

    public static List<ForecastItem> parseForecast(@NonNull JSONObject _jsonObject) throws JSONException {
        List<ForecastItem> list = new ArrayList<>();
        if (_jsonObject.has(OpenWeatherContract.OWM_LIST)) {
            JSONArray jsonArray = _jsonObject.getJSONArray(OpenWeatherContract.OWM_LIST);
            for (int index = 0; index < jsonArray.length(); index++) {
                list.add(parseForecastItem(jsonArray.getJSONObject(index)));
            }
        } else {
            list.add(parseForecastItem(_jsonObject));
        }
        return list;
    }

    private static ForecastItem parseForecastItem(JSONObject _jsonItem) throws JSONException {

        ForecastItem item = new ForecastItem();
        // The date/time is returned as a long in unixtime GMT (for convert multiply 1000).
        item.dateTime = _jsonItem.getLong(OpenWeatherContract.OWM_DATETIME) * 1000;
        item.pressure = _jsonItem.getDouble(OpenWeatherContract.OWM_PRESSURE);
        item.humidity = _jsonItem.getInt(OpenWeatherContract.OWM_HUMIDITY);

        item.windSpeed = _jsonItem.getDouble(OpenWeatherContract.OWM_SPEED);
        item.windDirection = _jsonItem.getDouble(OpenWeatherContract.OWM_DEG);
        // Description is in a child array called "weather", which is 1 element long.
        JSONObject weatherObject =
                _jsonItem.getJSONArray(OpenWeatherContract.OWM_WEATHER).getJSONObject(0);
        item.weatherId = weatherObject.getInt(OpenWeatherContract.OWM_WEATHER_ID);
        item.shortDescription = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_MAIN);
        item.description = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_DESCRIPTION);
        item.iconName = weatherObject.getString(OpenWeatherContract.OWM_WEATHER_ICON);

        // Temperatures are in a child object called "main".
        JSONObject temperatureObject =
                _jsonItem.getJSONObject(OpenWeatherContract.OWM_TEMP);
        item.maxTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_TEMP_MAX);
        item.minTemp = temperatureObject.getDouble(OpenWeatherContract.OWM_TEMP_MIN);
        return item;
    }
}
