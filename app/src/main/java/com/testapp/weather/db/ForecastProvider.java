package com.testapp.weather.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.db.table.SQLBaseTable;

public class ForecastProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    private static final int FORECAST = 100;
    private static final int FORECAST_WITH_DATE = 101;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SQLBaseTable.CONTENT_AUTHORITY;

        matcher.addURI(authority, ForecastTable.TABLE_NAME, FORECAST);
        matcher.addURI(authority, ForecastTable.TABLE_NAME + "/*", FORECAST_WITH_DATE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());
        return true;
    }

    private static final String mForecastByDateSelection = "DATETIME(" + ForecastTable.FIELD_DATE_TIME + ") == DATETIME(?)";
    private static final String mForecastByStartDateSelection = "DATETIME(" + ForecastTable.FIELD_DATE_TIME + ") >= ?";

    private Cursor queryForecastTable(final Uri _uri, final String[] _projection, final String _selection,
                                      final String[] _selectionArgs, final String _sortOrder) {
        String limitClouse = null;
        final String limit = _uri.getQueryParameter(ForecastTable.URI_PARAM_QUERY_LIMIT);
        if (!TextUtils.isEmpty(limit)) {
            final String offset = _uri.getQueryParameter(ForecastTable.URI_PARAM_QUERY_OFFSET);
            limitClouse = (TextUtils.isEmpty(offset) ? "" : offset + ",") + limit;
        }

        String[] selectionArgs = _selectionArgs;
        String selection = _selection;

        String startDate = ForecastTable.getStartDateFromUri(_uri);
        if (!TextUtils.isEmpty(startDate)) {
            selection = mForecastByStartDateSelection;
            selectionArgs = new String[]{startDate};
        }

        return mOpenHelper.openForecastTable(false).getAll(
                _projection,
                selection,
                selectionArgs,
                _sortOrder,
                limitClouse);
    }

    @Override
    public Cursor query(Uri _uri, String[] _projection, String _selection, String[] _selectionArgs, String _sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(_uri)) {

            case FORECAST: {
                retCursor = queryForecastTable(_uri, _projection, _selection, _selectionArgs, _sortOrder);
                break;
            }

            case FORECAST_WITH_DATE: {
                String date = ForecastTable.getDateFromUri(_uri);
                retCursor = queryForecastTable(_uri, _projection,
                        mForecastByDateSelection,
                        new String[]{date},
                        _sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + _uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), _uri);
        return retCursor;
    }

    @Override
    public String getType(Uri _uri) {
        final int match = sUriMatcher.match(_uri);

        switch (match) {
            case FORECAST:
                return ForecastTable.CONTENT_TYPE;
            case FORECAST_WITH_DATE:
                return ForecastTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + _uri);
        }
    }

    @Override
    public Uri insert(Uri _uri, ContentValues _values) {
        final int match = sUriMatcher.match(_uri);
        Uri returnUri;

        switch (match) {
            case FORECAST: {
                long _id = mOpenHelper.openForecastTable(true).updateOrInsert(_values);
                if (_id > 0)
                    returnUri = ForecastTable.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + _uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown _uri: " + _uri);
        }
        getContext().getContentResolver().notifyChange(_uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri _uri, String _selection, String[] _selectionArgs) {
        final int match = sUriMatcher.match(_uri);
        int rowsDeleted;
        switch (match) {
            case FORECAST:
                rowsDeleted = mOpenHelper.openForecastTable(true).delete(_selection, _selectionArgs);
                break;
            case FORECAST_WITH_DATE:
                final String date = ForecastTable.getDateFromUri(_uri);
                final String[] selectionArgs = new String[]{date};
                rowsDeleted = mOpenHelper.openForecastTable(true).delete(mForecastByDateSelection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown _uri: " + _uri);
        }
        // Because a null deletes all rows
        if (_selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(_uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri _uri, ContentValues _values, String _selection, String[] _selectionArgs) {
        final int match = sUriMatcher.match(_uri);
        int rowsUpdated = 0;

        switch (match) {
            case FORECAST:
                rowsUpdated = mOpenHelper.openForecastTable(true)
                        .update(_values, _selection, _selectionArgs);
                break;
            case FORECAST_WITH_DATE:
                final String date = ForecastTable.getDateFromUri(_uri);
                final String[] selectionArgs = new String[]{date};
                rowsUpdated = mOpenHelper.openForecastTable(true)
                        .update(_values, mForecastByDateSelection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown _uri: " + _uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(_uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri _uri, ContentValues[] _values) {
        final int match = sUriMatcher.match(_uri);
        int returnCount = 0;
        switch (match) {
            case FORECAST:
                returnCount = mOpenHelper.openForecastTable(true).bulkInsertOrUpdate(_values);

                getContext().getContentResolver().notifyChange(_uri, null);
                return returnCount;
            default:
                return super.bulkInsert(_uri, _values);
        }
    }
}