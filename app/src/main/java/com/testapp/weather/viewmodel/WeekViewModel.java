package com.testapp.weather.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.testapp.weather.adapter.BindableAdapter;
import com.testapp.weather.adapter.ForecastAdapter;
import com.testapp.weather.adapter.util.ListConfig;
import com.testapp.weather.db.DatabaseHelper;
import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.model.Model;
import com.testapp.weather.sync.StatusReceiver;
import com.testapp.weather.util.binding.ClickAction;
import com.testapp.weather.util.binding.OnActionClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 25.12.2015.
 */
public class WeekViewModel implements ViewModel, OnActionClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_FORECAST = 1;
    public static final int FORECAST_DAY_COUNT = 7;

    private Context mContext;
    private Callback mCallback;
    public ListConfig listConfig;
    public BindableAdapter<ForecastItem> bindableAdapter;
    public ObservableBoolean isEmptyMessageVisible = new ObservableBoolean(false);
    public ObservableBoolean isProgressVisible = new ObservableBoolean(false);
    private final List<ForecastItem> mLoadedModels;

    public interface Callback {

        void onError(Exception _e);

        void onForecastClicked(View _view, ForecastItem _forecast);
    }

    public WeekViewModel(Context _context, LoaderManager _loaderManager, Callback _callback) {
        mContext = _context.getApplicationContext();
        mCallback = _callback != null ? _callback : new EmptyCallback();
//        mScrollListener = new EndlessOnScrollListener(this);
        mLoadedModels = new ArrayList<>(7);
        bindableAdapter = new ForecastAdapter(mLoadedModels, this);
        listConfig = getListConfig();
        _loaderManager.initLoader(LOADER_ID_FORECAST, null, this);
    }

    protected ListConfig getListConfig() {
        ListConfig.Builder builder = new ListConfig.Builder(bindableAdapter)
                .setHasFixedSize(true)
//                .addOnScrollListener(mScrollListener)
                .setDefaultDividerEnabled(true);
        return builder.build(mContext);
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mCallback = null;
    }


    @Override
    public void onActionFired(View _view, ClickAction _action, Model _model) {
        mCallback.onForecastClicked(_view, (ForecastItem) _model);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = ForecastTable.FIELD_DATE_TIME + " ASC";
        final Uri uri = ForecastTable.buildUriWithStartDate(new Date(), 0, FORECAST_DAY_COUNT);
        return new CursorLoader(mContext,
                uri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> _loader, Cursor _data) {
        final List<ForecastItem> forecast = DatabaseHelper.getInstance(mContext)
                .convertCursorToList(_data, ForecastItem.class, false);
        mLoadedModels.clear();
        mLoadedModels.addAll(forecast);
        bindableAdapter.notifyDataSetChanged();
        refreshStatus();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> _loader) {
        refreshStatus();
    }

    private void refreshStatus() {
        isEmptyMessageVisible.set(mLoadedModels.isEmpty());
    }


    private static class EmptyCallback implements Callback {

        @Override
        public void onError(Exception _e) {

        }

        @Override
        public void onForecastClicked(View _view, ForecastItem _forecast) {

        }
    }
}
