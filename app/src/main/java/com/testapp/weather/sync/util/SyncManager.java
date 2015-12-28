package com.testapp.weather.sync.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.os.Build;
import android.os.Bundle;

import com.testapp.weather.R;
import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.util.LogHelper;
import com.testapp.weather.util.PrefUtils;

import java.util.concurrent.TimeUnit;

/**
 * Helper class for manage syncing
 * Created on 25.12.2015.
 */
public class SyncManager {

    private static final String LOG_TAG = LogHelper.makeLogTag(SyncManager.class);

    //    private static final long DEFAULT_SYNC_INTERVAL = TimeUnit.HOURS.toMillis(1); // 1 hour
    private static final long DEFAULT_SYNC_FLEXTIME = TimeUnit.MINUTES.toMillis(10); // 10 min

    public static void initializeSyncAdapter(Context _context) {
        final Account account = getSyncAccount(_context);
        configureAccount(account, _context);
    }

    public static Account getSyncAccount(Context _context) {
        AccountManager accountManager =
                (AccountManager) _context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                _context.getString(R.string.app_name), _context.getString(R.string.sync_account_type));

        // Check if account exist
        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", Bundle.EMPTY)) {
                LogHelper.LOGD(LOG_TAG, "Error while account creating...");
                return null;
            }
            LogHelper.LOGD(LOG_TAG, "Account has been created");
        }
        return newAccount;
    }

    private static void configureAccount(Account _account, Context _context) {
        if (_account == null) return;
        if (PrefUtils.isAutosyncEnabled(_context)) {
            final long syncInterval = PrefUtils.getSyncInterval(_context);
            configurePeriodicSync(_context, syncInterval, DEFAULT_SYNC_FLEXTIME);
            ContentResolver.setSyncAutomatically(_account,
                    _context.getString(R.string.forecast_content_authority), true);
        } else {
            ContentResolver.setSyncAutomatically(_account,
                    _context.getString(R.string.forecast_content_authority), false);
            if (PrefUtils.isSyncOnStartEnabled(_context)) {
                syncImmediately(_context);
            }
        }
    }

    public static void configurePeriodicSync(Context _context, long _syncInterval, long _flexTime) {
        Account account = getSyncAccount(_context);
        String authority = _context.getString(R.string.forecast_content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(_syncInterval, _flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(Bundle.EMPTY).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), _syncInterval);
        }
    }

    public static void syncImmediately(Context _context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(_context),
                _context.getString(R.string.forecast_content_authority), bundle);
    }

    public static void clearData(Context _context) {
        _context.getContentResolver().delete(ForecastTable.CONTENT_URI, null, new String[]{});
    }
}
