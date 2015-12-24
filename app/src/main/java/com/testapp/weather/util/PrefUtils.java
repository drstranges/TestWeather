package com.testapp.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.testapp.weather.R;

/**
 * Created on 24.12.2015.
 */
public final class PrefUtils {

    private static PrefUtils INSTANCE;

    private final String KEY_SYNC_WIFI_ONLY;
    private final String KEY_SYNC_UPDATE_ON_START;
    private final String KEY_ASK_ON_EXIT;
    private final String KEY_LOCATION;

    private final SharedPreferences sPreferences;

    private PrefUtils(Context _context) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        KEY_SYNC_WIFI_ONLY = _context.getString(R.string.pref_sync_wifi_only_key);
        KEY_SYNC_UPDATE_ON_START = _context.getString(R.string.pref_sync_update_on_start_key);
        KEY_ASK_ON_EXIT = _context.getString(R.string.pref_ask_on_exit_key);
        KEY_LOCATION = _context.getString(R.string.pref_location_key);
    }

    public static PrefUtils getInstance(Context _context) {
        if (INSTANCE == null) {
            INSTANCE = new PrefUtils(_context);
        }
        return INSTANCE;
    }

    public String getPreferredLocation() {
        return sPreferences.getString(KEY_LOCATION, null);
    }
}
