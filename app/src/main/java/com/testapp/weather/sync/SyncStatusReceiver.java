package com.testapp.weather.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.testapp.weather.util.LogHelper;

/**
 * Created on 24.12.2015.
 */
public class SyncStatusReceiver extends BroadcastReceiver {

    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(SyncStatusReceiver.class);

    private static final String ACTION_SYNC_STARTED = "com.testapp.weather.ACTION_SYNC_STARTED";
    private static final String ACTION_SYNC_FINISHED = "com.testapp.weather.ACTION_SYNC_FINISHED";
    private static final String ACTION_SYNC_ERROR = "com.testapp.weather.ACTION_SYNC_ERROR";

    private static final String EXTRA_ERROR_CODE = "EXTRA_ERROR_CODE";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private SyncStatusListener mStatusListener;

    public interface SyncStatusListener {
        void onSyncStarted();

        void onSyncFinished();

        void onSyncError(final int _errorCode, final String _message);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SYNC_STARTED);
        intentFilter.addAction(ACTION_SYNC_FINISHED);
        intentFilter.addAction(ACTION_SYNC_ERROR);
        return intentFilter;
    }

    public SyncStatusReceiver(SyncStatusListener _listener) {
        mStatusListener = _listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (mStatusListener == null) return;
        final String action = intent.getAction();
        LogHelper.LOGD(LOG_TAG, "onReceive: " + action);
        switch (action){
            case ACTION_SYNC_STARTED:
                mStatusListener.onSyncStarted();
                break;
            case ACTION_SYNC_FINISHED:
                mStatusListener.onSyncFinished();
                break;
            case ACTION_SYNC_ERROR:
                final int errorCode = intent.getIntExtra(EXTRA_ERROR_CODE, -1);
                final String message = intent.getStringExtra(EXTRA_MESSAGE);
                mStatusListener.onSyncError(errorCode, message);
                break;
        }
    }

    public static void sendSyncStarted(final Context _context) {
        LocalBroadcastManager.getInstance(_context).sendBroadcast(new Intent(ACTION_SYNC_STARTED));
    }

    public static void sendSyncFinishedSuccessfull(final Context _context) {
        LocalBroadcastManager.getInstance(_context).sendBroadcast(new Intent(ACTION_SYNC_FINISHED));
    }

    public static void sendSyncError(final Context _context, final int _errorCode, final String _message) {
        final Intent intent = new Intent(ACTION_SYNC_ERROR);
        intent.putExtra(EXTRA_ERROR_CODE, _errorCode);
        intent.putExtra(EXTRA_MESSAGE, _message);
        LocalBroadcastManager.getInstance(_context).sendBroadcast(intent);
    }
}
