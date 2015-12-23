package com.testapp.weather.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Helper class for hiding keyboard
 * Created on 23.12.2015.
 */
public class KeyboardUtils {

    public static void hideSoftKeyboard(final Activity _activity) {
        if(_activity != null && _activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(_activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
