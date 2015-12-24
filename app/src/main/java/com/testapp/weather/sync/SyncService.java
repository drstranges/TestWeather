package com.testapp.weather.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.db.ForecastManager;
import com.testapp.weather.util.LogHelper;

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
 * Sync service for downloading data from the server
 * Created on 24.12.2015.
 */
public final class SyncService extends IntentService {

    private static final String LOG_TAG = LogHelper.makeLogTag(SyncService.class);

    public static final String INTENT_ACTION = "com.testapp.weather.ACTION_SYNC_START";
    public static final String EXTRA_URL = "EXTRA_URL";

    public SyncService() {
        super(SyncService.class.getName());
    }

    public static void launch(Context _context, String _url) {
        _context.startService(getLaunchIntent(_url));
    }

    public static Intent getLaunchIntent(final String _url) {
        final Intent intent = new Intent(INTENT_ACTION);
        intent.setPackage("com.testapp.weather");
        intent.putExtra(EXTRA_URL, _url);
        return intent;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final String url = intent.getStringExtra(EXTRA_URL);
        LogHelper.LOGD(LOG_TAG, "onHandleIntent.url = " + url);
        if (TextUtils.isEmpty(url)) return;
        final Context context = getApplicationContext();
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
