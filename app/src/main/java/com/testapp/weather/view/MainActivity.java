package com.testapp.weather.view;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.testapp.weather.R;
import com.testapp.weather.databinding.ActivityMainBinding;
import com.testapp.weather.util.LogHelper;
import com.testapp.weather.util.PrefUtils;
import com.testapp.weather.view.fragment.DayFragment;
import com.testapp.weather.view.fragment.HistoryFragment;
import com.testapp.weather.view.fragment.SettingsFragment;
import com.testapp.weather.view.fragment.WeekFragment;
import com.testapp.weather.viewmodel.MainViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements Navigator, ColorToolbarHolder, NavigationView.OnNavigationItemSelectedListener,
        MainViewModel.Callback, FragmentManager.OnBackStackChangedListener {

    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(MainActivity.class);

    private static final long BACK_PRESS_EXIT_DELAY = 10000; //10 sec
    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;
    private long mTimeLastBackPress;
    private MainViewModel mViewModel;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);
        initNavigationDrawer();
        refreshToolbar();
        mViewModel = new MainViewModel(this, this);
        if (savedInstanceState == null) {
            selectDrawerItem(R.id.menu_item_weather_today);
            mViewModel.performSync();
        }
    }

    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) mBinding.getRoot().findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mBinding.toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
        mBinding.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem _item) {
        boolean result = selectDrawerItem(_item.getItemId());
        closeOpenedDrawer();
        return result;
    }

    private boolean closeOpenedDrawer() {
        if (mDrawerLayout != null && DrawerLayout.LOCK_MODE_UNLOCKED == mDrawerLayout.getDrawerLockMode(GravityCompat.START)
                && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean selectDrawerItem(@IdRes int _itemResId) {
        switch (_itemResId) {
            case R.id.menu_item_weather_today:
                navigateToScreen(DayFragment.class, null, false);
                break;
            case R.id.menu_item_weather_week:
                navigateToScreen(WeekFragment.class, null, false);
                break;
            case R.id.menu_item_weather_history:
                navigateToScreen(HistoryFragment.class, null, false);
                break;
            case R.id.menu_item_settings:
                navigateToScreen(SettingsFragment.class, null, false);
                break;
            case R.id.menu_item_logout:
                logout();
                break;
            default:
                return false;
        }
        mBinding.navigationView.setCheckedItem(_itemResId);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isBackStackEmpty() && (closeOpenedDrawer() || !checkExitByBackPressed())) return;
        super.onBackPressed();
    }

    private boolean isBackStackEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    private boolean checkExitByBackPressed() {
        boolean isAllowed = true;
        if (PrefUtils.isBackPressTwiceEnabled(getApplicationContext())) {
            final long timeNow = System.currentTimeMillis();
            final long backPressDelay = timeNow - mTimeLastBackPress;
            mTimeLastBackPress = timeNow;
            isAllowed = backPressDelay < BACK_PRESS_EXIT_DELAY;
            if (!isAllowed) {
                Toast.makeText(getApplicationContext(), R.string.press_back_twice, Toast.LENGTH_SHORT).show();
            }
        }
        return isAllowed;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !isBackStackEmpty()) {
            getSupportFragmentManager().popBackStack();
        } else if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mBinding.unbind();
        mViewModel.onDestroy();
        super.onDestroy();
    }

    private void logout() {
        finish();
    }

    @Override
    public void navigateToScreen(Class<? extends Fragment> _fragmentClass, Bundle _args, boolean _asChildScreen) {
        if (mDrawerToggle != null && _asChildScreen) {
            ChildActivity.start(this, _fragmentClass, _args);
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (_asChildScreen) {
                ft.addToBackStack(null);
            } else {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            final Fragment fragment = Fragment.instantiate(getApplicationContext(), _fragmentClass.getName(), _args);
            ft.replace(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public void onError(Exception _e) {
        Toast.makeText(getApplicationContext(), _e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSyncStarted() {
        Toast.makeText(getApplicationContext(), "Syncing...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSyncFinished() {
        Toast.makeText(getApplicationContext(), "Weather syncing successfully!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void requestPermissions(int _requestCode, String[] _requiredPermissions) {
        LogHelper.LOGD(LOG_TAG, "Permissions requested: " + Arrays.toString(_requiredPermissions));
        ActivityCompat.requestPermissions(this, _requiredPermissions, _requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] _permissions, int[] _grantResults) {
        LogHelper.LOGD(LOG_TAG, "onRequestPermissionsResult. permissions: " + Arrays.toString(_permissions) +
                "grantResults: " + Arrays.toString(_grantResults));
        if (!mViewModel.onRequestPermissionResult(requestCode, _permissions, _grantResults)) {
            super.onRequestPermissionsResult(requestCode, _permissions, _grantResults);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        refreshToolbar();
    }

    public void refreshToolbar() {
        if (mDrawerToggle == null) {
            if (isBackStackEmpty()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().show();
            }
        }
    }

    @Override
    public void setToolbarColor(Integer _color) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (_color != null) {
                final ColorDrawable colorDrawable = new ColorDrawable(_color);
                getWindow().setBackgroundDrawable(colorDrawable);
                actionBar.setBackgroundDrawable(colorDrawable);
            } else {
                getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            }
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }
}
