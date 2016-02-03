package com.testapp.weather.util.binding;

import android.databinding.BaseObservable;

public class BindableBoolean extends BaseObservable {
    boolean value;

    public boolean get() {
        return value;
    }

    public void set(boolean _value) {
        if (this.value != _value) {
            this.value = _value;
            notifyChange();
        }
    }

}