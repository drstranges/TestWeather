package com.testapp.weather.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.testapp.weather.adapter.BindableAdapter;
import com.testapp.weather.adapter.ForecastAdapter;
import com.testapp.weather.adapter.util.ListConfig;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.model.Model;
import com.testapp.weather.util.binding.ClickAction;
import com.testapp.weather.util.binding.OnActionClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 25.12.2015.
 */
public class WeekViewModel implements ViewModel, OnActionClickListener {

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

    public WeekViewModel(Context _context, Callback _callback) {
        mContext = _context.getApplicationContext();
        mCallback = _callback != null ? _callback : new EmptyCallback();
//        mScrollListener = new EndlessOnScrollListener(this);
        mLoadedModels = new ArrayList<>();
        bindableAdapter = new ForecastAdapter(mLoadedModels, this);
        listConfig = getListConfig();
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

    private static class EmptyCallback implements Callback {

        @Override
        public void onError(Exception _e) {

        }

        @Override
        public void onForecastClicked(View _view, ForecastItem _forecast) {

        }
    }
}
