package com.testapp.weather.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testapp.weather.R;
import com.testapp.weather.databinding.FragmentDayBinding;
import com.testapp.weather.util.ForecastUtils;
import com.testapp.weather.viewmodel.DayViewModel;

import java.util.Date;

/**
 * Created on 23.12.2015.
 */
public class DayFragment extends BaseFragment<DayViewModel, FragmentDayBinding> implements DayViewModel.Callback {
    public static final String ARG_TIME = "ARG_TIME";
    public static final String ARG_COLOR = "ARG_COLOR";

    private long mTimeMillis;
    private Integer mColor;

    @NonNull
    public static Bundle buildArgs(long _timeMillis, int _color) {
        final Bundle args = new Bundle();
        args.putLong(ARG_TIME, _timeMillis);
        args.putInt(ARG_COLOR, _color);
        return args;
    }

    @Override
    protected void onInitArgs() {
        Bundle args = getArguments();
        if (args != null) {
            mTimeMillis = args.getLong(ARG_TIME, new Date().getTime());
            mColor = args.getInt(ARG_COLOR, Color.BLACK);
        } else {
            mTimeMillis = new Date().getTime();
            mColor = null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(ForecastUtils.getRelativeDate(getContext(), mTimeMillis));
        if (mColor != null) setToolbarColor(mColor);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_day;
    }

    @Override
    protected DayViewModel createViewModel() {
        return new DayViewModel(getContext(), mTimeMillis, getLoaderManager(), this);
    }

    @Override
    protected void onBindViewModel(FragmentDayBinding _binding, DayViewModel _viewModel) {
        _binding.setViewModel(_viewModel);
    }

    @Override
    public void setBgColor(int _color) {
        setToolbarColor(_color);
    }

}
