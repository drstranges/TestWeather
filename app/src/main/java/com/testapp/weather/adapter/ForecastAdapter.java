package com.testapp.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testapp.weather.adapter.util.BindingHolder;
import com.testapp.weather.databinding.ItemForecastBinding;
import com.testapp.weather.model.ForecastItem;

import java.util.List;

/**
 * Created on 25.12.2015.
 */
public class ForecastAdapter extends BindableAdapter<ForecastItem> {

    private final OnForecastClickListener mForecastClickListener;

    public interface OnForecastClickListener {
        void onForecastClicked(View _view, ForecastItem _forecast);
    }

    public ForecastAdapter(final List<ForecastItem> _dataSource, OnForecastClickListener _clickListener) {
        super(_dataSource);
        mForecastClickListener = _clickListener;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        final ItemForecastBinding binding = ItemForecastBinding.
                inflate(LayoutInflater.from(_parent.getContext()), _parent, false);
        binding.setOnForecastClickListener(mForecastClickListener);
        return new BindingHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final BindingHolder _holder, final int _position) {
        final BindingHolder<ItemForecastBinding> viewHolder = (BindingHolder<ItemForecastBinding>) _holder;
        final ForecastItem item = mDataSource.get(_position);
        viewHolder.getBinding().setForecast(item);
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).id;
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

}
