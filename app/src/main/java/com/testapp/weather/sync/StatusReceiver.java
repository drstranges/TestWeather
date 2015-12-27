package com.testapp.weather.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.testapp.weather.util.LogHelper;

/**
 * Created on 24.12.2015.
 */
public class StatusReceiver extends BroadcastReceiver {

    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(StatusReceiver.class);

    private static final String ACTION_STATUS = "com.testapp.weather.ACTION_STATUS";

    private static final String EXTRA_STATUS = "EXTRA_STATUS";
    private static final String EXTRA_ERROR_CODE = "EXTRA_ERROR_CODE";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private static final int STATUS_SYNC_STARTED = 1;
    private static final int STATUS_SYNC_FINISHED = 2;
    private static final int STATUS_ERROR = 3;

    private SyncStatusListener mStatusListener;

    public interface SyncStatusListener {
        void onSyncStarted();

        void onSyncFinished();

        void onSyncError(final int _errorCode, final String _message);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATUS);
        return intentFilter;
    }

    public StatusReceiver(SyncStatusListener _listener) {
        mStatusListener = _listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (mStatusListener == null) return;
        final String action = intent.getAction();
        if (!ACTION_STATUS.equals(action)) return;
        final int status = intent.getIntExtra(EXTRA_STATUS, -1);
        LogHelper.LOGD(LOG_TAG, "onReceive: " + action);
        switch (status){
            case STATUS_SYNC_STARTED:
                mStatusListener.onSyncStarted();
                break;
            case STATUS_SYNC_FINISHED:
                mStatusListener.onSyncFinished();
                break;
            case STATUS_ERROR:
                final int errorCode = intent.getIntExtra(EXTRA_ERROR_CODE, -1);
                final String message = intent.getStringExtra(EXTRA_MESSAGE);
                mStatusListener.onSyncError(errorCode, message);
                break;
        }
    }

    public static void sendSyncStarted(final Context _context) {
        final Intent intent = new Intent(ACTION_STATUS);
        intent.putExtra(EXTRA_STATUS, STATUS_SYNC_STARTED);
        _context.sendBroadcast(intent);
    }

    public static void sendSyncFinished(final Context _context) {
        final Intent intent = new Intent(ACTION_STATUS);
        intent.putExtra(EXTRA_STATUS, STATUS_SYNC_FINISHED);
        _context.sendBroadcast(intent);
    }

    public static void sendSyncError(final Context _context, final int _errorCode, final String _message) {
        final Intent intent = new Intent(ACTION_STATUS);
        intent.putExtra(EXTRA_STATUS, STATUS_ERROR);
        intent.putExtra(EXTRA_ERROR_CODE, _errorCode);
        intent.putExtra(EXTRA_MESSAGE, _message);
        _context.sendBroadcast(intent);
    }
}
