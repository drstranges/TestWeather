package com.testapp.weather.adapter;

import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.v7.widget.RecyclerView;

import com.testapp.weather.adapter.util.BindingHolder;

import java.util.List;

/**
 * Extended RecyclerView.Adapter to use with data binding
 * Created on 25.12.2015.
 */
public abstract class BindableAdapter<T> extends RecyclerView.Adapter<BindingHolder> implements Observable {

    protected List<T> mDataSource;

    public BindableAdapter(List<T> _dataSource) {
        mDataSource = _dataSource;
    }

    private transient PropertyChangeRegistry mCallbacks;

    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback listener) {
        if (this.mCallbacks == null) {
            this.mCallbacks = new PropertyChangeRegistry();
        }

        this.mCallbacks.add(listener);
    }

    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback listener) {
        if (this.mCallbacks != null) {
            this.mCallbacks.remove(listener);
        }
    }

    public synchronized void notifyChange() {
        if (this.mCallbacks != null) {
            this.mCallbacks.notifyCallbacks(this, 0, null);
        }
    }

    public void notifyPropertyChanged(int fieldId) {
        if (this.mCallbacks != null) {
            this.mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
}
