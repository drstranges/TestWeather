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
import com.testapp.weather.util.ForecastUtils;
import com.testapp.weather.view.ColorToolbarHolder;
import com.testapp.weather.view.Navigator;
import com.testapp.weather.viewmodel.WeekViewModel;

/**
 * Created on 23.12.2015.
 */
public class WeekFragment extends BaseFragment<WeekViewModel, FragmentWeekBinding> implements WeekViewModel.Callback {


    @Override
    protected void onInitArgs() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_week;
    }

    @Override
    protected WeekViewModel createViewModel() {
        return new WeekViewModel(getContext(), getLoaderManager(), this);
    }

    @Override
    protected void onBindViewModel(FragmentWeekBinding _binding, WeekViewModel _viewModel) {
        _binding.setViewModel(_viewModel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbarColor(null);
        getActivity().setTitle(R.string.menu_navigation_weather_week);
    }

    @Override
    public void onForecastClicked(View _view, ForecastItem _forecast) {
        final Activity activity = getActivity();
        if (activity instanceof Navigator){
            int color = getResources().getColor(
                    ForecastUtils.getWeatherCondition(_forecast.weatherId).getColorResId());
            ((Navigator) activity)
                    .navigateToScreen(DayFragment.class,
                            DayFragment.buildArgs(_forecast.dateTime, color), true);
        }
    }

    @Override
    public void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }
}
