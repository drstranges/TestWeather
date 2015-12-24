package com.testapp.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.testapp.weather.db.table.ForecastTable;
import com.testapp.weather.db.table.SQLBaseTable;
import com.testapp.weather.model.ForecastItem;
import com.testapp.weather.util.LogHelper;

import java.util.Collections;
import java.util.List;

/**
 * Created on 24.12.2015.
 */
public final class DatabaseHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = LogHelper.makeLogTag(DatabaseHelper.class);

    private static final String DATABASE_NAME = "forecast.db";
    private static final int DATABASE_VERSION = 1;

    private volatile static DatabaseHelper sInstance = null;
    private static final Object sLock = new Object();

    private final ForecastTable mForecastTable = new ForecastTable();

    public static DatabaseHelper getInstance(Context _context) {
        if (_context == null)
            throw new RuntimeException("DatabaseHelper not initialized. You cannot get the instance until you initialized that");

        DatabaseHelper instance = sInstance;
        if (instance == null) {
            synchronized (sLock) {
                instance = sInstance;
                if (instance == null) {
                    sInstance = instance = new DatabaseHelper(_context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private DatabaseHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase _db) {
        LogHelper.LOGI(LOG_TAG, "onCreate");
        try {
            ForecastTable.onCreateDb(_db);
        } catch (SQLException e) {
            LogHelper.LOGE(LOG_TAG, "Can't create database. Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void dropTables(final SQLiteDatabase _db) {
        LogHelper.LOGI(LOG_TAG, "Drop database");
        try {
            _db.execSQL("DROP TABLE " + ForecastTable.TABLE_NAME);
        } catch (SQLException e) {
            LogHelper.LOGE(LOG_TAG, "Can't drop database. Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase _db, final int _oldVersion, final int _newVersion) {
        LogHelper.LOGI(LOG_TAG, "onUpdate from v." + _oldVersion + " to v." + _newVersion);
        switch (_oldVersion) {
//                case 1:
//                    upgradeToSecondVersion(_db, _oldVersion, _newVersion);
//                case 2:
//                    upgradeToThirdVersion(_db, _oldVersion, _newVersion);
            default:
                dropTables(_db);
                break;
        }
        onCreate(_db);
        LogHelper.LOGI(LOG_TAG, "Database has been upgraded successfully.");
    }

    @Override
    public void onDowngrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
        dropTables(_db);
        onCreate(_db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mForecastTable.onOpenDb(db);
    }

    public ForecastTable openForecastTable(boolean _withWritableAccess) {
        openDb(_withWritableAccess);
        return mForecastTable;
    }

    public <T> List<T> convertCursorToList(Cursor _cursor, Class<? extends SQLBaseTable<T>> _table) {
        try {
            if (ForecastTable.class.equals(_table)) {
                return (List<T>) mForecastTable.getList(_cursor);
            }// else if
        } catch (ClassCastException e) {
            LogHelper.LOGE(LOG_TAG, "convertCursorToList exception", e);
        }
        return Collections.emptyList();
    }

    public <T> ContentValues[] convertToCv(@NonNull List<T> items, Class<T> _class) {
        ContentValues[] contentValues = new ContentValues[items.size()];
        try {
            if (ForecastItem.class.equals(_class)) {
                return mForecastTable.convertToCV((List<ForecastItem>) items);
            }// else if
        } catch (ClassCastException e) {
            LogHelper.LOGE(LOG_TAG, "convertToCv exception", e);
        }
        return contentValues;
    }

    public <T> ContentValues convertToCv(@NonNull T item, Class<T> _class) {
        ContentValues contentValues = new ContentValues();
        try {
            if (ForecastItem.class.equals(_class)) {
                return mForecastTable.convertToCV((ForecastItem) item);
            }//else if
        } catch (ClassCastException e) {
            LogHelper.LOGE(LOG_TAG, "convertToCv exception", e);
        }
        return contentValues;
    }

    private void openDb(boolean _withWritableAccess) {
        if (_withWritableAccess) {
            getWritableDatabase();
        } else {
            getReadableDatabase();
        }
    }

    // Delete all data in the database
    public void clearData() {
        dropTables(getWritableDatabase());
        onCreate(getWritableDatabase());
    }

}
