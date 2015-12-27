package com.testapp.weather.view.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testapp.weather.databinding.FragmentDayBinding;
import com.testapp.weather.view.ColorToolbarHolder;
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
        final Bundle args = buildArgs(_timeMillis);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static Bundle buildArgs(long _timeMillis) {
        final Bundle args = new Bundle();
        args.putLong(ARG_TIME, _timeMillis);
        return args;
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
    public void setTitle(String _title) {
        getActivity().setTitle(_title);
    }

    @Override
    public void setBgColor(int _color) {
        setToolbarColor(_color);
    }

    protected void setToolbarColor(int _color) {
        Activity activity = getActivity();
        if (activity instanceof ColorToolbarHolder) {
            ((ColorToolbarHolder) activity).setToolbarColor(_color);
        }
    }

    @Override
    public void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }
}
