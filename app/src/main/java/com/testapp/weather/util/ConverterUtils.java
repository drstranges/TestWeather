package com.testapp.weather.util;

import com.testapp.weather.R;

/**
 * Helper class to provide resource id according to the weather condition
 * Created by d_rom on 25.12.2015.
 */
public class ConverterUtils {

    public static int getIconResourceByWeatherId(int _weatherId) {
        if (_weatherId >= 200 && _weatherId <= 232) {
            return R.drawable.ic_weather_lightning;
        } else if (_weatherId >= 300 && _weatherId <= 321) {
            return R.drawable.ic_weather_rainy;
        } else if (_weatherId >= 500 && _weatherId <= 504) {
            return R.drawable.ic_weather_pouring;
        } else if (_weatherId == 511) {
            return R.drawable.ic_weather_snowy;
        } else if (_weatherId >= 520 && _weatherId <= 531) {
            return R.drawable.ic_weather_pouring;
        } else if (_weatherId >= 600 && _weatherId <= 622) {
            return R.drawable.ic_weather_snowy;
        } else if (_weatherId >= 701 && _weatherId <= 761) {
            return R.drawable.ic_weather_fog;
        } else if (_weatherId == 761 || _weatherId == 781) {
            return R.drawable.ic_weather_lightning;
        } else if (_weatherId == 800) {
            return R.drawable.ic_weather_sunny;
        } else if (_weatherId == 801) {
            return R.drawable.ic_weather_partlycloudy;
        } else if (_weatherId >= 802 && _weatherId <= 804) {
            return R.drawable.ic_weather_cloudy;
        }
        return 0;
    }

    public static int getArtIconResourceByWeatherId(int _weatherId) {
        if (_weatherId >= 200 && _weatherId <= 232) {
            return R.drawable.ic_weather_lightning_art;
        } else if (_weatherId >= 300 && _weatherId <= 321) {
            return R.drawable.ic_weather_rainy_art;
        } else if (_weatherId >= 500 && _weatherId <= 504) {
            return R.drawable.ic_weather_pouring_art;
        } else if (_weatherId == 511) {
            return R.drawable.ic_weather_snowy_art;
        } else if (_weatherId >= 520 && _weatherId <= 531) {
            return R.drawable.ic_weather_pouring_art;
        } else if (_weatherId >= 600 && _weatherId <= 622) {
            return R.drawable.ic_weather_snowy_art;
        } else if (_weatherId >= 701 && _weatherId <= 761) {
            return R.drawable.ic_weather_fog_art;
        } else if (_weatherId == 761 || _weatherId == 781) {
            return R.drawable.ic_weather_lightning_art;
        } else if (_weatherId == 800) {
            return R.drawable.ic_weather_sunny_art;
        } else if (_weatherId == 801) {
            return R.drawable.ic_weather_partlycloudy_art;
        } else if (_weatherId >= 802 && _weatherId <= 804) {
            return R.drawable.ic_weather_cloudy_art;
        }
        return 0;
    }

    public static int getColorResIdByWeatherId(int _weatherId) {
        if (_weatherId >= 200 && _weatherId <= 232) {
            return R.drawable.ic_weather_lightning_art;
        } else if (_weatherId >= 300 && _weatherId <= 321) {
            return R.drawable.ic_weather_rainy_art;
        } else if (_weatherId >= 500 && _weatherId <= 504) {
            return R.drawable.ic_weather_pouring_art;
        } else if (_weatherId == 511) {
            return R.drawable.ic_weather_snowy_art;
        } else if (_weatherId >= 520 && _weatherId <= 531) {
            return R.drawable.ic_weather_pouring_art;
        } else if (_weatherId >= 600 && _weatherId <= 622) {
            return R.drawable.ic_weather_snowy_art;
        } else if (_weatherId >= 701 && _weatherId <= 761) {
            return R.drawable.ic_weather_fog_art;
        } else if (_weatherId == 761 || _weatherId == 781) {
            return R.drawable.ic_weather_lightning_art;
        } else if (_weatherId == 800) {
            return R.drawable.ic_weather_sunny_art;
        } else if (_weatherId == 801) {
            return R.drawable.ic_weather_partlycloudy_art;
        } else if (_weatherId >= 802 && _weatherId <= 804) {
            return R.drawable.ic_weather_cloudy_art;
        }
        return 0;
    }
}
