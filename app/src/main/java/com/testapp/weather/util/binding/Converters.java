package com.testapp.weather.util.binding;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.testapp.weather.adapter.util.ListConfig;
import com.testapp.weather.model.Model;
import com.testapp.weather.util.ConverterUtils;
import com.testapp.weather.util.LogHelper;

public class Converters {
    private static final String LOG_TAG = LogHelper.makeLogTag(Converters.class);

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }

    @BindingConversion
    public static int convertBooleanToInt(boolean value) {
        return value ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter({"bind:listConfig"})
    public static void configRecyclerView(final RecyclerView _recyclerView,
                                          final ListConfig _config) {
        _config.applyConfig(_recyclerView);
    }

    @BindingAdapter(value = {"weatherId", "showArtIcon"}, requireAll = false)
    public static void setWeatherIcon(final ImageView _view,
                                          final int _weatherId, boolean _showArtIcon) {
        final int resId = _showArtIcon ? ConverterUtils.getArtIconResourceByWeatherId(_weatherId)
                                       : ConverterUtils.getIconResourceByWeatherId(_weatherId);
        if (resId != 0) _view.setImageResource(resId);
    }

    @BindingAdapter(value = {"weatherId"})
    public static void setWeatherBackground(final ViewGroup _view, final int _weatherId) {
        final int resId = ConverterUtils.getColorResIdByWeatherId(_weatherId);
        if (resId != 0) _view.setBackgroundColor(_view.getContext().getColor(resId));
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


    @BindingAdapter({"bind:textHtml"})
    public static void setHtmlText(final TextView _textView, final String _htmlText) {
        _textView.setText(Html.fromHtml(_htmlText));
    }

}