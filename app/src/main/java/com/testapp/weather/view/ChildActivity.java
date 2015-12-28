package com.testapp.weather.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.testapp.weather.R;

public class ChildActivity extends AppCompatActivity implements ColorToolbarHolder {

    private static final String ARG_FRAGMENT_CLASS = "ARG_FRAGMENT_CLASS";

    public static void start(@NonNull Context _context, @NonNull Class<? extends Fragment> _fragmentClass, @Nullable Bundle _bundle) {
        _context.startActivity(getIntent(_context, _fragmentClass, _bundle));
    }

    public static Intent getIntent(@NonNull Context _context, @NonNull Class<? extends Fragment> _fragmentClass, @Nullable Bundle _bundle) {
        Intent intent = new Intent(_context, ChildActivity.class);
        final Bundle arg = _bundle != null ? new Bundle(_bundle) : new Bundle();
        arg.putString(ARG_FRAGMENT_CLASS, _fragmentClass.getName());
        intent.putExtras(arg);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        initToolbar();

        if (savedInstanceState == null) replaceFragment();
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_child;
    }

    protected void replaceFragment() {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.flFragmentHolder, getFragment()).
                commit();
    }

    protected Fragment getFragment() {
        final Bundle args = getIntent().getExtras();
        String fname = args.getString(ARG_FRAGMENT_CLASS);
        final Fragment fragment = Fragment.instantiate(getApplicationContext(), fname, args);
        return fragment;
    }

    protected void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onHomePressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onHomePressed() {
        finish();
    }

    @Override
    public void setToolbarColor(Integer _color) {
        if (_color != null) {
            final ColorDrawable colorDrawable = new ColorDrawable(_color);
            getWindow().setBackgroundDrawable(colorDrawable);
        } else {
            getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        }
    }
}
