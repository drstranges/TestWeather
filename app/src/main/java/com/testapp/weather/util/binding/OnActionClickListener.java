package com.testapp.weather.util.binding;

import android.view.View;

import com.testapp.weather.model.Model;

/**
 * Created by d_rom on 25.12.2015.
 */
public interface OnActionClickListener {
    void onActionFired(View _view, ClickAction _action, Model _model);
}
