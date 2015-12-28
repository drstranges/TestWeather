package com.testapp.weather.adapter.util;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BindingHolder - extended RecyclerView.ViewHolder to use with data binding
 * @param <VB> view binding class
 */
public class BindingHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private VB binding;

    public static <VB extends ViewDataBinding> BindingHolder<VB> newInstance(
            @LayoutRes int _layoutId, LayoutInflater _inflater,
            @Nullable ViewGroup _parent, boolean _attachToParent) {

        VB vb = DataBindingUtil.inflate(_inflater, _layoutId, _parent, _attachToParent);
        return new BindingHolder<>(vb);
    }

    public BindingHolder(VB _binding) {
        super(_binding.getRoot());
        binding = _binding;
    }

    public BindingHolder(View _view) {
        super(_view);
        binding = DataBindingUtil.bind(_view);
    }

    public VB getBinding() {
        return binding;
    }
}
