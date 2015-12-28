package com.testapp.weather.db;

import android.content.Context;

import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.util.LogHelper;

import java.util.List;

/**
 * Helper class to cover some work with add and clear forecast
 * Created on 24.12.2015.
 */
public final class ForecastManager {

    private static final java.lang.String LOG_TAG = LogHelper.makeLogTag(ForecastManager.class);

    public static void addForecast(Context _context, List<ForecastItem> _forecast) {
        _context.getContentResolver()
                .bulkInsert(ForecastTable.CONTENT_URI,
                        DatabaseHelper.getInstance(_context)
                                .convertToCv(_forecast, ForecastItem.class));
    }

    public static void clearOldData(Context _context) {
        _context.getContentResolver().delete(ForecastTable.CONTENT_URI, null, null);
    }

}
