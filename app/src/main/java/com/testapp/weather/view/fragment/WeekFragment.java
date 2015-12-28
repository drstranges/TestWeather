package com.testapp.weather.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testapp.weather.R;
import com.testapp.weather.databinding.FragmentWeekBinding;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.view.ColorToolbarHolder;
import com.testapp.weather.view.Navigator;
import com.testapp.weather.viewmodel.WeekViewModel;

/**
 * Created on 23.12.2015.
 */
public class WeekFragment extends Fragment implements WeekViewModel.Callback {
    private WeekViewModel mViewModel;
    private FragmentWeekBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWeekBinding.inflate(inflater, container, false);
        mViewModel = new WeekViewModel(getContext(), getLoaderManager(), this);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarColor(null);
        getActivity().setTitle(R.string.menu_navigation_weather_week);
    }

    protected void setToolbarColor(Integer _color) {
        Activity activity = getActivity();
        if (activity instanceof ColorToolbarHolder) {
            ((ColorToolbarHolder) activity).setToolbarColor(_color);
        }
    }

    @Override
    public void onError(Exception _e) {

    }

    @Override
    public void onForecastClicked(View _view, ForecastItem _forecast) {
        final Activity activity = getActivity();
        if (activity instanceof Navigator){
            ((Navigator) activity)
                    .navigateToScreen(DayFragment.class, DayFragment.buildArgs(_forecast.dateTime), true);
        }
    }

    @Override
    public void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }
}
