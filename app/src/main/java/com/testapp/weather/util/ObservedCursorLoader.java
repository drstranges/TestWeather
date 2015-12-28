package com.testapp.weather.util;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.testapp.weather.sync.StatusReceiver;

/**
 * CursorLoader which call {@link #forceLoad()} each time as sync finished. Fix some bug with cursor observing
 * Created by d_rom on 28.12.2015.
 */
public class ObservedCursorLoader extends CursorLoader implements StatusReceiver.SyncStatusListener {
    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(ObservedCursorLoader.class);

    private final StatusReceiver mStatusReceiver;

    public ObservedCursorLoader(Context _context) {
        super(_context);

        mStatusReceiver = new StatusReceiver(this);
        _context.registerReceiver(mStatusReceiver, StatusReceiver.getIntentFilter());
    }

    public ObservedCursorLoader(Context _context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(_context, uri, projection, selection, selectionArgs, sortOrder);

        mStatusReceiver = new StatusReceiver(this);
        _context.registerReceiver(mStatusReceiver, StatusReceiver.getIntentFilter());
    }

    @Override
    protected void onReset() {
        getContext().unregisterReceiver(mStatusReceiver);
        super.onReset();
    }

    @Override
    public void onSyncStarted() {

    }

    @Override
    public void onSyncFinished() {
        forceLoad();
        LogHelper.LOGD(LOG_TAG, "onSyncFinished.forceLoad");
    }

    @Override
    public void onSyncError(int _errorCode, String _message) {

    }
}
