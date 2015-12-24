package com.testapp.weather.db.table;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.testapp.weather.db.util.DbUtils;
import com.testapp.weather.model.ForecastItem;

public class ForecastTable extends SQLBaseTable<ForecastItem> {

    public static final String TABLE_NAME = "forecast";

    public static final String CONTENT_TYPE = CONTENT_TYPE_PREFIX + TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE = CONTENT_ITEM_TYPE_PREFIX + TABLE_NAME;

    public static final String URI_PARAM_QUERY_OFFSET = "offset";
    public static final String URI_PARAM_QUERY_LIMIT = "limit";
    public static final String URI_PARAM_START_DATE = "date";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String FIELD_DATE_TIME = "dateTime";
    public static final String FIELD_WEATHER_ID = "weatherId";
    public static final String FIELD_SHORT_DESCRIPTION = "shortDescription";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ICON_NAME = "iconName";
    public static final String FIELD_PRESSURE = "pressure";
    public static final String FIELD_HUMIDITY = "humidity";
    public static final String FIELD_WIND_SPEED = "windSpeed";
    public static final String FIELD_WIND_DIRECTION = "windDirection";
    public static final String FIELD_MAX_TEMP = "maxTemp";
    public static final String FIELD_MIN_TEMP = "minTemp";

    protected static final String SCRIPT_CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
            FIELD_ID + " integer primary key, " +
            FIELD_DATE_TIME + " integer not null, " +
            FIELD_WEATHER_ID + " integer, " +
            FIELD_SHORT_DESCRIPTION + " text, " +
            FIELD_DESCRIPTION + " text, " +
            FIELD_ICON_NAME + " text, " +
            FIELD_PRESSURE + " integer, " +
            FIELD_HUMIDITY + " integer, " +
            FIELD_WIND_SPEED + " integer, " +
            FIELD_WIND_DIRECTION + " integer, " +
            FIELD_MAX_TEMP + " integer, " +
            FIELD_MIN_TEMP + " integer" +
            ");";

    public static void onCreateDb(SQLiteDatabase db) throws SQLException {
        db.execSQL(SCRIPT_CREATE_TABLE);
    }

    public static Uri buildUri(long _id) {
        return ContentUris.withAppendedId(CONTENT_URI, _id);
    }

    public static Uri buildUriForAllWithLimit(long _offset, long _limit) {
        return CONTENT_URI.buildUpon()
                .appendQueryParameter(URI_PARAM_QUERY_OFFSET, String.valueOf(_offset))
                .appendQueryParameter(URI_PARAM_QUERY_LIMIT, String.valueOf(_limit))
                .build();
    }

    public static Uri buildUriWithStartDate(long startUtcDate) {
        return CONTENT_URI.buildUpon()
                .appendQueryParameter(URI_PARAM_START_DATE, Long.toString(startUtcDate)).build();
    }

    public static Uri buildUriWithDate(long utcDate) {
        return CONTENT_URI.buildUpon().appendPath(Long.toString(utcDate)).build();
    }

    public static long getStartDateFromUri(Uri uri) {
        String dateString = uri.getQueryParameter(URI_PARAM_START_DATE);
        if (!TextUtils.isEmpty(dateString)) {
            return Long.parseLong(dateString);
        }
        return 0;
    }

    public static long getDateFromUri(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(1));
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected ForecastItem loadDbItem(final Cursor _cur) {
        final ForecastItem item = new ForecastItem();
        item.dateTime = _cur.getLong(_cur.getColumnIndex(FIELD_DATE_TIME));
        item.shortDescription = _cur.getString(_cur.getColumnIndex(FIELD_SHORT_DESCRIPTION));
        item.description = _cur.getString(_cur.getColumnIndex(FIELD_DESCRIPTION));
        item.iconName = _cur.getString(_cur.getColumnIndex(FIELD_ICON_NAME));
        item.pressure = _cur.getDouble(_cur.getColumnIndex(FIELD_PRESSURE));
        item.humidity = _cur.getInt(_cur.getColumnIndex(FIELD_HUMIDITY));
        item.windSpeed = _cur.getDouble(_cur.getColumnIndex(FIELD_WIND_SPEED));
        item.windDirection = _cur.getDouble(_cur.getColumnIndex(FIELD_WIND_DIRECTION));
        item.maxTemp = _cur.getDouble(_cur.getColumnIndex(FIELD_MAX_TEMP));
        item.minTemp = _cur.getDouble(_cur.getColumnIndex(FIELD_MIN_TEMP));

        return item;
    }

    @Override
    public ContentValues convertToCV(final ForecastItem item) {
        ContentValues cv = new ContentValues();
        if (item.id != null) {
            cv.put(FIELD_ID, item.id);
        }
        cv.put(FIELD_DATE_TIME, item.dateTime);
        cv.put(FIELD_SHORT_DESCRIPTION, item.shortDescription);
        cv.put(FIELD_DESCRIPTION, item.description);
        cv.put(FIELD_ICON_NAME, item.iconName);
        cv.put(FIELD_PRESSURE, item.pressure);
        cv.put(FIELD_HUMIDITY, item.humidity);
        cv.put(FIELD_WIND_SPEED, item.windSpeed);
        cv.put(FIELD_WIND_DIRECTION, item.windDirection);
        cv.put(FIELD_MAX_TEMP, item.maxTemp);
        cv.put(FIELD_MIN_TEMP, item.minTemp);
        return cv;
    }

}