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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    //not change DATE_FORMAT
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    protected static final String SCRIPT_CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
            FIELD_ID + " integer primary key, " +
            FIELD_DATE_TIME + " text not null, " +
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
            ", UNIQUE(" + FIELD_DATE_TIME + ") ON CONFLICT REPLACE" +

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

    public static Uri buildUriWithStartDate(Date _startDate, long _offset, long _limit) {
//        long startDate = getDateForMidnight(_startDate);
        return CONTENT_URI.buildUpon()
                .appendQueryParameter(URI_PARAM_START_DATE, dateToDatabaseFormat(_startDate))
                .appendQueryParameter(URI_PARAM_QUERY_OFFSET, String.valueOf(_offset))
                .appendQueryParameter(URI_PARAM_QUERY_LIMIT, String.valueOf(_limit))
                .build();
    }

//    private static long getDateForMidnight(long _millis) {
//        Calendar date = Calendar.getInstance();
//        date.setTimeInMillis(_millis);
//        date.set(Calendar.HOUR_OF_DAY, 0);
//        date.set(Calendar.MINUTE, 0);
//        date.set(Calendar.SECOND, 0);
//        date.set(Calendar.MILLISECOND, 0);
//        return date.getTimeInMillis();
//    }

    public static Uri buildUriWithDate(Date _date) {
        //        long startDate = getDateForMidnight(_date);
        return CONTENT_URI.buildUpon().appendPath(dateToDatabaseFormat(_date)).build();
    }

    public static String getStartDateFromUri(Uri uri) {
        return uri.getQueryParameter(URI_PARAM_START_DATE);
    }

    public static String getDateFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected ForecastItem loadDbItem(final Cursor _cur) {
        final ForecastItem item = new ForecastItem();
        try {
            item.dateTime = dateFromDatabaseFormat(_cur.getString(_cur.getColumnIndex(FIELD_DATE_TIME))).getTime();
        } catch (ParseException _e) {
            _e.printStackTrace();
        }
        item.weatherId = _cur.getInt(_cur.getColumnIndex(FIELD_WEATHER_ID));
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
        cv.put(FIELD_DATE_TIME, dateToDatabaseFormat(new Date(item.dateTime)));
        cv.put(FIELD_WEATHER_ID, item.weatherId);
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

    public static String dateToDatabaseFormat(Date _date) {
        return DATE_FORMAT.format(_date);
    }

    public static Date dateFromDatabaseFormat(String _date) throws ParseException {
        return DATE_FORMAT.parse(_date);
    }

}