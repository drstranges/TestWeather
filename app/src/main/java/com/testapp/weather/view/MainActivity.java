package com.testapp.weather.view;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.testapp.weather.R;
import com.testapp.weather.databinding.ActivityMainBinding;
import com.testapp.weather.util.KeyboardUtils;
import com.testapp.weather.view.fragment.SettingsFragment;
import com.testapp.weather.view.fragment.TodayFragment;
import com.testapp.weather.view.fragment.WeekFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        setSupportActionBar(mBinding.toolbar);
        initNavigationDrawer();
        if (savedInstanceState == null) {
            selectDrawerItem(R.id.menu_item_weather_today);
        }
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
        closeDrawerIfOpen();
        return result;
    }

    private boolean closeDrawerIfOpen() {
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
                replaceFragment(new TodayFragment());
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
        if (!closeDrawerIfOpen()){
            super.onBackPressed();
        }
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
}
