package com.testapp.weather.view.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.testapp.weather.R;

/**
 * Created on 23.12.2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle _bundle, String _s) {
        addPreferencesFromResource(R.xml.pref_general);
    }
}
