package com.testapp.weather.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.testapp.weather.R;
import com.testapp.weather.db.ForecastManager;
import com.testapp.weather.sync.StatusReceiver;
import com.testapp.weather.sync.util.SyncManager;
import com.testapp.weather.util.LocationLoader;
import com.testapp.weather.util.PermissionHelper;
import com.testapp.weather.util.PrefUtils;

/**
 * Created on 25.12.2015.
 */
public class MainViewModel implements ViewModel, SharedPreferences.OnSharedPreferenceChangeListener, StatusReceiver.SyncStatusListener {
    private static final int REQUEST_CODE_PERMISSIONS = 1;

    private Context mContext;
    private Callback mCallback;
    private final StatusReceiver mStatusReceiver;

    public interface Callback {
        void requestPermissions(int _requestCode, String[] _requiredPermissions);

        void onError(Exception _e);

        void onSyncStarted();

        void onSyncFinished();
    }

    public MainViewModel(Context _context, Callback _callback) {
        mContext = _context.getApplicationContext();
        mCallback = _callback != null ? _callback : new EmptyCallback();
        PreferenceManager.getDefaultSharedPreferences(_context)
                .registerOnSharedPreferenceChangeListener(this);
        mStatusReceiver = new StatusReceiver(this);
        mContext.registerReceiver(mStatusReceiver, StatusReceiver.getIntentFilter());
    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .unregisterOnSharedPreferenceChangeListener(this);
        mContext.unregisterReceiver(mStatusReceiver);
        mContext = null;
        mCallback = null;
    }

    public void performSync() {
        String location = PrefUtils.getPreferredLocation(mContext);
        if (TextUtils.isEmpty(location)) {
            findLocation();
            return;
        }
        SyncManager.initializeSyncAdapter(mContext);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mContext.getString(R.string.pref_location_key).equals(key)) {
            ForecastManager.clearOldData(mContext);
            performSync();
        }
    }

    private void findLocation() {
        try {
            LocationLoader.launch(mContext);
        } catch (PermissionHelper.PermissionSecurityException _e) {
            _e.printStackTrace();
            mCallback.requestPermissions(REQUEST_CODE_PERMISSIONS, _e.getRequiredPermissions());
        }
    }


    public boolean onRequestPermissionResult(int _requestCode, String[] _permissions, int[] _grantResults) {
        if (REQUEST_CODE_PERMISSIONS == _requestCode) {
            if (PermissionHelper.isSomePermissionGranted(_grantResults)) {
                findLocation();
            }
            return true;
        }
        return false;
    }


    @Override
    public void onSyncStarted() {
        mCallback.onSyncStarted();
    }

    @Override
    public void onSyncFinished() {
        mCallback.onSyncFinished();
    }

    @Override
    public void onSyncError(int _errorCode, String _message) {
        mCallback.onError(new Exception(_message));
    }

    private static class EmptyCallback implements Callback {
        @Override
        public void requestPermissions(int _requestCode, String[] _requiredPermissions) {
        }

        @Override
        public void onError(Exception _e) {
        }

        @Override
        public void onSyncStarted() {
        }

        @Override
        public void onSyncFinished() {
        }
    }
}
