package com.testapp.weather.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testapp.weather.databinding.FragmentDayBinding;
import com.testapp.weather.viewmodel.DayViewModel;

import java.util.Date;

/**
 * Created on 23.12.2015.
 */
public class DayFragment extends Fragment implements DayViewModel.Callback {
    public static final String ARG_TIME = "ARG_TIME";
    private DayViewModel mViewModel;
    private FragmentDayBinding mBinding;


    public static DayFragment newInstance(long _timeMillis) {
        DayFragment fragment = new DayFragment();
        final Bundle args = new Bundle();
        args.putLong(ARG_TIME, _timeMillis);
        fragment.setArguments(args);
        return fragment;
    }

    private long getArgTimeMillis() {
        long timeMillis = new Date().getTime();
        Bundle args = getArguments();
        if (args != null) {
            timeMillis = args.getLong(ARG_TIME, timeMillis);
        }
        return timeMillis;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDayBinding.inflate(inflater, container, false);
        final long timeMillis = getArgTimeMillis();
        mViewModel = new DayViewModel(getContext(), timeMillis, getLoaderManager(), this);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onError(Exception _e) {

    }

    @Override
    public void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }
}
