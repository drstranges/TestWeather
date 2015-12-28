package com.testapp.weather.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.testapp.weather.db.DatabaseHelper;
import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.util.ForecastUtils;
import com.testapp.weather.util.ObservedCursorLoader;

import java.util.Date;

/**
 * Created on 25.12.2015.
 */
public class DayViewModel implements ViewModel, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_FORECAST = 2;

    private Context mContext;
    private Callback mCallback;
    public ObservableBoolean isEmptyMessageVisible = new ObservableBoolean(false);
//    public ObservableBoolean isProgressVisible = new ObservableBoolean(false);
    public ObservableField<ForecastItem> forecast = new ObservableField<>();
    public final long timeMillis;

    public interface Callback {
        void setBgColor(int _color);
    }

    public DayViewModel(Context _context, long _timeMillis, LoaderManager _loaderManager, Callback _callback) {
        timeMillis = _timeMillis;
        mContext = _context.getApplicationContext();
        mCallback = _callback != null ? _callback : new EmptyCallback();
        _loaderManager.initLoader(LOADER_ID_FORECAST, null, this);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mCallback = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = ForecastTable.buildUriWithDate(new Date(timeMillis));
        return new ObservedCursorLoader(mContext,
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> _loader, Cursor _data) {
        final ForecastItem forecast = DatabaseHelper.getInstance(mContext)
                .convertFromCursor(_data, ForecastItem.class, false);
        this.forecast.set(forecast);
        refreshStatus();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> _loader) {}

    private void refreshStatus() {
        final ForecastItem forecastItem = forecast.get();
        final boolean hasForecast = forecastItem != null;
        isEmptyMessageVisible.set(!hasForecast);
        if (hasForecast) {
            mCallback.setBgColor(
                    mContext.getResources().getColor(
                            ForecastUtils.getWeatherCondition(forecastItem.weatherId).getColorResId()));
        }
    }


    private static class EmptyCallback implements Callback {

        @Override
        public void setBgColor(int _color) {
        }
    }
}
