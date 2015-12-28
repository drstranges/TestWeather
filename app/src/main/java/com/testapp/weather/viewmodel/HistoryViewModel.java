package com.testapp.weather.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.testapp.weather.adapter.BindableAdapter;
import com.testapp.weather.adapter.ForecastAdapter;
import com.testapp.weather.adapter.util.EndlessOnScrollListener;
import com.testapp.weather.adapter.util.ListConfig;
import com.testapp.weather.adapter.util.OnLoadMoreListener;
import com.testapp.weather.db.DatabaseHelper;
import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.model.Model;
import com.testapp.weather.util.ObservedCursorLoader;
import com.testapp.weather.util.binding.ClickAction;
import com.testapp.weather.util.binding.OnActionClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 25.12.2015.
 */
public class HistoryViewModel implements ViewModel, OnActionClickListener, LoaderManager.LoaderCallbacks<Cursor>,OnLoadMoreListener {

    private static final int LOADER_ID_FORECAST = 3;
    public static final int LOAD_LIMIT = 7;

    private Context mContext;
    private Callback mCallback;
    public ListConfig listConfig;
    public BindableAdapter<ForecastItem> bindableAdapter;
    public ObservableBoolean isEmptyMessageVisible = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    private final List<ForecastItem> mLoadedModels;
    private EndlessOnScrollListener mScrollListener;
    private LoaderManager mLoaderManager;
    private long mOffset = 0;
    private boolean isNothingToLoad;

    public interface Callback {

        void onForecastClicked(View _view, ForecastItem _forecast);
    }

    public HistoryViewModel(Context _context, LoaderManager _loaderManager, Callback _callback) {
        mContext = _context.getApplicationContext();
        mCallback = _callback != null ? _callback : new EmptyCallback();
        mScrollListener = new EndlessOnScrollListener(this);
        mLoadedModels = new ArrayList<>(7);
        bindableAdapter = new ForecastAdapter(mLoadedModels, this);
        listConfig = getListConfig();
        mLoaderManager = _loaderManager;
        Loader loader = mLoaderManager.getLoader(LOADER_ID_FORECAST);
        if (loader != null) {
            mLoaderManager.restartLoader(LOADER_ID_FORECAST, null, this);
        } else {
            mLoaderManager.initLoader(LOADER_ID_FORECAST, null, this);
        }
    }

    protected ListConfig getListConfig() {
        ListConfig.Builder builder = new ListConfig.Builder(bindableAdapter)
                .setHasFixedSize(true)
                .addOnScrollListener(mScrollListener)
                .setDefaultDividerEnabled(true);
        return builder.build(mContext);
    }

    @Override
    public void onDestroy() {
        mScrollListener = null;
        mLoaderManager = null;
        mContext = null;
        mCallback = null;
    }


    @Override
    public void onActionFired(View _view, ClickAction _action, Model _model) {
        mCallback.onForecastClicked(_view, (ForecastItem) _model);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        isLoading.set(true);
        String sortOrder = ForecastTable.FIELD_DATE_TIME + " DESC";
        final Uri uri = ForecastTable.buildUriForAllWithLimit(mOffset, LOAD_LIMIT);
        return new ObservedCursorLoader(mContext,
                uri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> _loader, Cursor _data) {
        isLoading.set(false);
        final List<ForecastItem> forecast = DatabaseHelper.getInstance(mContext)
                .convertCursorToList(_data, ForecastItem.class, false);
        if (mOffset == 0) mLoadedModels.clear();
        if (forecast.isEmpty()) isNothingToLoad = true;
        mLoadedModels.addAll(forecast);
        mOffset = mLoadedModels.size();
        bindableAdapter.notifyDataSetChanged();
        refreshStatus();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> _loader) {
        isLoading.set(false);
        refreshStatus();
    }

    private void refreshStatus() {
        isEmptyMessageVisible.set(mLoadedModels.isEmpty());
    }


    @Override
    public void onLoadMore() {
        if (!isNothingToLoad) {
            mLoaderManager.restartLoader(LOADER_ID_FORECAST, null, this);
        }
    }

    @Override
    public boolean isLoading() {
        return isLoading.get();
    }


    private static class EmptyCallback implements Callback {

        @Override
        public void onForecastClicked(View _view, ForecastItem _forecast) {

        }
    }
}
