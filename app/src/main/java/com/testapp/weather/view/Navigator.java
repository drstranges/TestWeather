package com.testapp.weather.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by d_rom on 27.12.2015.
 */
public interface Navigator {
    void navigateToScreen(Class<? extends Fragment> _fragmentClass, Bundle _bundle, boolean _useBackStack);
}
