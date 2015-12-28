package com.testapp.weather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 25.12.2015.
 */
public class NetworkUtils {

    public static boolean isWifiConnected(Context _context) {
        boolean isConnected;
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        isConnected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

        return isConnected && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }
}
