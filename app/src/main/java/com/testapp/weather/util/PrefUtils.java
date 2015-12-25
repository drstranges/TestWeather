package com.testapp.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.testapp.weather.R;

/**
 * Created on 24.12.2015.
 */
public final class PrefUtils {


    public static void setPreferredLocation(Context _context, String _location) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        pref.edit().putString(_context.getString(R.string.pref_location_key), _location).apply();
    }

    public static String getPreferredLocation(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return pref.getString(
                _context.getString(R.string.pref_location_key),
                _context.getString(R.string.pref_location_default));
    }

    public static long getSyncInterval(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return Long.valueOf(pref.getString(
                _context.getString(R.string.pref_sync_interval_key),
                _context.getString(R.string.pref_sync_interval_default)));
    }

    public static boolean isAutosyncEnabled(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return pref.getBoolean(
                _context.getString(R.string.pref_sync_auto_key),
                Boolean.valueOf(_context.getString(R.string.pref_sync_auto_default)));
    }

    public static boolean isWifiOnly(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return pref.getBoolean(
                _context.getString(R.string.pref_sync_wifi_only_key),
                Boolean.valueOf(_context.getString(R.string.pref_sync_wifi_only_default)));
    }

    public static boolean isSyncOnStartEnabled(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return pref.getBoolean(
                _context.getString(R.string.pref_sync_update_on_start_key),
                Boolean.valueOf(_context.getString(R.string.pref_sync_update_on_start_default)));
    }

    public static boolean isBackPressTwiceEnabled(Context _context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(_context);
        return pref.getBoolean(
                _context.getString(R.string.pref_ask_on_exit_key),
                Boolean.valueOf(_context.getString(R.string.pref_ask_on_exit_default)));
    }
}
