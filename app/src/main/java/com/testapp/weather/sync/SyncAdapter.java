package com.testapp.weather.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;

import com.testapp.weather.R;
import com.testapp.weather.db.ForecastManager;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.util.LogHelper;
import com.testapp.weather.util.PrefUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Sync adapter for downloading data from the server
 * Created on 24.12.2015.
 */
public final class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = LogHelper.makeLogTag(SyncAdapter.class);
    private static final int NUM_DAYS_SYNC = 7;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        final Context context = getContext();
        final String location = PrefUtils.getInstance(context).getPreferredLocation();
        if (TextUtils.isEmpty(location)) {
            LogHelper.LOGD(LOG_TAG, "Error: Location not found");
            SyncStatusReceiver.sendSyncError(context, 0, context.getString(R.string.error_location_not_found));
            return;
        }
        final String url = OpenWeatherContract.getDailyForecastUrl(location, NUM_DAYS_SYNC);
        LogHelper.LOGD(LOG_TAG, "onPerformSync.url = " + url);
        SyncStatusReceiver.sendSyncStarted(context);
        try {
            String results = downloadData(url);
            final JSONObject jsonResponse = new JSONObject(results);
            List<ForecastItem> forecast = ForecastItem.from(jsonResponse);
            ForecastManager.updateForecast(context, forecast);
            SyncStatusReceiver.sendSyncFinishedSuccessfull(context);
        } catch (Exception e) {
            LogHelper.LOGD(LOG_TAG, "Sync failed", e);
            final int errorCode = getErrorCode(e);
            final String message = e.getMessage();
            SyncStatusReceiver.sendSyncError(context, errorCode, message);
        }

    }

    private String downloadData(String requestUrl) throws IOException, SyncException {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestMethod("GET");

        int responseCode = urlConnection.getResponseCode();

        LogHelper.LOGD(LOG_TAG, "responseCode = " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            final String response = streamToStringAndClose(inputStream);
            LogHelper.LOGD(LOG_TAG, "Response taken: " + response);
            return response;
        } else {
            String responseMessage = urlConnection.getResponseMessage();
            throw new SyncException(responseCode, responseMessage);
        }
    }


    private String streamToStringAndClose(InputStream inputStream) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder(inputStream.available());
        String line;
        while ((line = r.readLine()) != null) {
            result.append(line);
        }
        inputStream.close();

        return result.toString();
    }


    private int getErrorCode(Exception _e) {
        if (_e instanceof SyncException) {
            return ((SyncException) _e).getStatusCode();
        }
        return -1;
    }

    public static class SyncException extends Exception {
        private final int mStatusCode;

        public SyncException(int _statusCode, String _message) {
            super(_message);
            mStatusCode = _statusCode;
        }

        public int getStatusCode() {
            return mStatusCode;
        }
    }
}
