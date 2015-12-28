package com.testapp.weather.model;


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
public final class ForecastItem implements Model {
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
