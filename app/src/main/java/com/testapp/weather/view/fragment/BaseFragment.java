package com.testapp.weather.view.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testapp.weather.databinding.FragmentDayBinding;
import com.testapp.weather.view.ColorToolbarHolder;
import com.testapp.weather.viewmodel.DayViewModel;
import com.testapp.weather.viewmodel.ViewModel;

/**
 * Created by d_rom on 28.12.2015.
 */
public abstract class BaseFragment<VM extends ViewModel, B extends ViewDataBinding> extends Fragment {

    protected VM mViewModel;
    protected B mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onInitArgs();
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mViewModel = createViewModel();
        onBindViewModel(mBinding, mViewModel);
        return mBinding.getRoot();
    }

    protected abstract void onInitArgs();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract VM createViewModel();

    protected abstract void onBindViewModel(B _binding, VM _viewModel);

    @Override
    public void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }

    public void setTitle(CharSequence _title) {
        getActivity().setTitle(_title);
    }

    protected void setToolbarColor(Integer _color) {
        Activity activity = getActivity();
        if (activity instanceof ColorToolbarHolder) {
            ((ColorToolbarHolder) activity).setToolbarColor(_color);
        }
    }
}
