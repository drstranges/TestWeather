package com.testapp.weather.view;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.testapp.weather.R;
import com.testapp.weather.databinding.ActivityMainBinding;
import com.testapp.weather.util.KeyboardUtils;
import com.testapp.weather.util.LogHelper;
import com.testapp.weather.util.PrefUtils;
import com.testapp.weather.view.fragment.SettingsFragment;
import com.testapp.weather.view.fragment.DayFragment;
import com.testapp.weather.view.fragment.WeekFragment;
import com.testapp.weather.viewmodel.MainViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainViewModel.Callback {

    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(MainActivity.class);

    private static final long BACK_PRESS_EXIT_DELAY = 10000; //10 sec
    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;
    private long mTimeLastBackPress;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        setSupportActionBar(mBinding.toolbar);
        initNavigationDrawer();
        if (savedInstanceState == null) {
            selectDrawerItem(R.id.menu_item_weather_today);
        }

        mViewModel = new MainViewModel(this, this);
        mViewModel.performSync();
    }

    private void initNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                KeyboardUtils.hideSoftKeyboard(MainActivity.this);
            }
        };
        mBinding.drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mBinding.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem _item) {
        boolean result = selectDrawerItem(_item.getItemId());
        closeOpenedDrawer();
        return result;
    }

    private boolean closeOpenedDrawer() {
        if (DrawerLayout.LOCK_MODE_UNLOCKED == mBinding.drawerLayout.getDrawerLockMode(GravityCompat.START)
                && mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean selectDrawerItem(@IdRes int _itemResId) {
        switch (_itemResId) {
            case R.id.menu_item_weather_today:
                replaceFragment(new DayFragment());
                return true;
            case R.id.menu_item_weather_week:
                replaceFragment(new WeekFragment());
                return true;
            case R.id.menu_item_settings:
                replaceFragment(new SettingsFragment());
                return true;
            case R.id.menu_item_logout:
                logout();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (closeOpenedDrawer() || !checkExitByBackPressed()) return;
        super.onBackPressed();
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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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

    private void replaceFragment(final Fragment _fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, _fragment)
                .commit();
    }

    @Override
    public void requestPermissions(int _requestCode, String[] _requiredPermissions) {
        LogHelper.LOGD(LOG_TAG, "Permissions requested: " + Arrays.toString(_requiredPermissions));
        ActivityCompat.requestPermissions(this, _requiredPermissions, _requestCode);
    }

    @Override
    public void onError(Exception _e) {
        Toast.makeText(getApplicationContext(), _e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] _permissions, int[] _grantResults) {
        LogHelper.LOGD(LOG_TAG, "onRequestPermissionsResult. permissions: " + Arrays.toString(_permissions) +
                "grantResults: " + Arrays.toString(_grantResults));
        if (!mViewModel.onRequestPermissionResult(requestCode, _permissions, _grantResults)) {
            super.onRequestPermissionsResult(requestCode, _permissions, _grantResults);
        }
    }
}
