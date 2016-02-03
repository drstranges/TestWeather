package com.testapp.weather.util.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.testapp.weather.adapter.util.ListConfig;
import com.testapp.weather.model.Model;
import com.testapp.weather.util.ForecastUtils;
import com.testapp.weather.util.LogHelper;

/**
 * Helper class which accumulate all custom BindingConversion and BindingAdapter,
 * which are used in the project
 */
public class Converters {
    private static final String LOG_TAG = LogHelper.makeLogTag(Converters.class);

    @BindingConversion
    public static boolean convertBindableToBoolean(BindableBoolean bindableBoolean) {
        return bindableBoolean.get();
    }

    @BindingConversion
    public static int convertBooleanToInt(boolean value) {
        return value ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter({"listConfig"})
    public static void configRecyclerView(final RecyclerView _recyclerView,
                                          final ListConfig _config) {
        _config.applyConfig(_recyclerView);
    }

    @BindingAdapter({"relativeDate"})
    public static void setRelativeDate(final TextView _view,
                                       final long _timeMillis) {
        _view.setText(ForecastUtils.getRelativeDate(_view.getContext(), _timeMillis));
    }

    @BindingAdapter({"android:onClick", "action", "model"})
    public static void onActionClick(final View _view, final OnActionClickListener _listener,
                                     final ClickAction _clickAction, final Model _model) {
        if (_listener == null) return;
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _listener.onActionFired(_view, _clickAction, _model);
            }
        });
    }

    @BindingAdapter(value = {"backgroundColorResId", "applyColorToParent"}, requireAll = false)
    public static void setBackgroundColorResId(final View _view, final @ColorRes int _colorResId, final boolean _applyToParent) {
        final int color = ContextCompat.getColor(_view.getContext(), _colorResId);

        final ViewParent parent = _view.getParent();
        if (_applyToParent && parent != null && parent instanceof View) {
            ((View) parent).setBackgroundColor(color);
        } else {
            _view.setBackgroundColor(color);
        }
    }

    @BindingAdapter({"maxTemp", "minTemp"})
    public static void setTemp(final TextView _view, final double _maxTemp, final double _minTemp) {
        final SpannableStringBuilder formattedTemp = ForecastUtils.getMaxMinTemp(_view.getContext(), _maxTemp, _minTemp);
        _view.setText(formattedTemp);
    }

    @BindingAdapter({"isRefreshing"})
    public static void setRefreshing(final SwipeRefreshLayout _view,
            final BindableBoolean _isRefreshing) {
        _view.setRefreshing(_isRefreshing.get());
    }

}