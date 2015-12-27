package com.testapp.weather.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.testapp.weather.db.util.DbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SQLBaseTable<T> {
    public static final String CONTENT_AUTHORITY = "com.testapp.weather.forecast.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String CONTENT_TYPE_PREFIX = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/";
    static final String CONTENT_ITEM_TYPE_PREFIX = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/";

    public static final String FIELD_ID = "_id";
    public static final String[] COUNT = new String[]{"count(*) AS count"};
    private static final String SELECT_BY_ID = FIELD_ID + " = ?";

    protected SQLiteDatabase mSQLiteDatabase;

    public void onOpenDb(SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    protected abstract String getTableName();

    /**
     * This is a method to create new item and load data into its field from database.
     *
     * @param cur opened cursor.
     * @return item instance.
     */
    protected abstract T loadDbItem(final Cursor cur);

    public abstract ContentValues convertToCV(final T item);

    public ContentValues[] convertToCV(@NonNull final List<T> items){
        ContentValues[] list = new ContentValues[items.size()];
        int count = items.size();
        for(int i = 0; i < count; i++){
            list[i] = convertToCV(items.get(i));
        }
        return list;
    }

    protected T get(final String selection, final String[] args) {
        return get(selection, args, null);
    }

    protected T get(final String selection, final String[] args, final String orderBy) {
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                     // The table name
                null,                               // A list of which columns to return
                selection,                          // SQL WHERE clause
                args,                               // selectionArgs to SQL WHERE clause
                null,                               // GROUP BY clause
                null,                               // SQL HAVING clause
                orderBy);                              // SQL ORDER BY clause
        return loadItemAndCloseCursor(cur);
    }

    protected T loadItemAndCloseCursor(Cursor _cur) {
        if (!DbUtils.checkCursor(_cur)) return null;
        final T item = loadDbItem(_cur);
        _cur.close();
        return item;
    }

    public T get(long id) {
        return get(FIELD_ID + " = ?", new String[]{String.valueOf(id)}, null);
    }

    protected int getCount(final String selection, final String[] args) {
        int res = 0;
        final String sqlQ = "select count(*) from " + getTableName()
                + " where " + selection + ";";
        final Cursor cur = mSQLiteDatabase.rawQuery(sqlQ, args);
        if (DbUtils.checkCursor(cur)) {
            res = cur.getInt(0);
            cur.close();
        }
        return res;
    }
    public int getCount() {
        int res = 0;
        final String sqlQ = "select count(*) from " + getTableName() + ";";
        final Cursor cur = mSQLiteDatabase.rawQuery(sqlQ, null);
        if (DbUtils.checkCursor(cur)) {
            res = cur.getInt(0);
            cur.close();
        }
        return res;
    }

    public List<T> getAll(final String orderBy) {
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                    // The table name
                null,
                null,
                null,
                null,                               // GROUP BY clause
                null,
                orderBy);                          // SQL ORDER BY clause
        return getList(cur, true);
    }

    protected List<T> getList(final String selection, final String[] selectionArgs, final String orderBy) {
        return getList(selection, selectionArgs, null, orderBy);
    }

    protected List<T> getList(final String selection, final String[] selectionArgs, final String groupBy,
                              final String orderBy) {
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                    // The table name
                null,
                selection,                         // SQL WHERE clause
                selectionArgs,                     // selectionArgs to SQL WHERE clause
                groupBy,                           // GROUP BY clause
                null,
                orderBy);                          // SQL ORDER BY clause
        return getList(cur, true);
    }

    public List<T> getList(final Cursor cur, final boolean shouldCloseCursor) {
        List<T> itemList = new ArrayList<>();
        if (DbUtils.checkCursor(cur)) {
            do {
                final T item = loadDbItem(cur);
                itemList.add(item);
            }
            while (cur.moveToNext());
            if (shouldCloseCursor) cur.close();
        }
        return itemList;
    }

    protected List<String> getStringList(final String column, final String selection, final String[] selectionArgs) {
        return getStringList(column, selection, selectionArgs, null, null);
    }

    protected List<String> getStringList(final String column, final String selection, final String[] selectionArgs,
                                         final String groupBy, final String orderBy) {
        List<String> itemList = new ArrayList<>();
        assert (column != null);
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                   // The table name
                new String[]{column},             // A list of which columns to return
                selection,
                selectionArgs,
                groupBy,                          // SQL GROUP BY clause
                null,
                orderBy);                          // SQL ORDER BY clause
        if (DbUtils.checkCursor(cur)) {
            do {
                itemList.add(cur.getString(cur.getColumnIndex(column)));
            }
            while (cur.moveToNext());
            cur.close();
        }
        return itemList;
    }

    public <J> List<J> getAllInColumn(final String column, Class<J> _clazz, final J _defaultValue, final String orderBy) {
        return getAllInColumn(column, _clazz, _defaultValue, null, null, null, orderBy);
    }

    public <J> List<J> getAllInColumn(final String column, Class<J> _clazz, final J _defaultValue, final String _selection, final String[] _selectionArgs) {
        return getAllInColumn(column, _clazz, _defaultValue, _selection, _selectionArgs, null, null);
    }

    public  <J> List<J> getAllInColumn(final String column, Class<J> _clazz, final J _defaultValue, final String selection, final String[] selectionArgs,
                                         final String groupBy, final String orderBy) {
        List<J> itemList = new ArrayList<>();
        assert (column != null);
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                   // The table name
                new String[]{column},             // A list of which columns to return
                selection,
                selectionArgs,
                groupBy,                          // SQL GROUP BY clause
                null,
                orderBy);                          // SQL ORDER BY clause
        if (DbUtils.checkCursor(cur)) {
            do {
                itemList.add(getDataItem(cur, cur.getColumnIndex(column), _clazz, _defaultValue));
            }
            while (cur.moveToNext());
            cur.close();
        }
        return itemList;
    }

    protected <J> J getData(final String column, Class<J> _clazz, final J _defaultValue, final String selection, final String[] selectionArgs,
                                                     final String groupBy, final String orderBy) {
        assert (column != null);
        final Cursor cur = mSQLiteDatabase.query(
                getTableName(),                   // The table name
                new String[]{column},             // A list of which columns to return
                selection,
                selectionArgs,
                groupBy,                          // SQL GROUP BY clause
                null,
                orderBy);                          // SQL ORDER BY clause
        if (!DbUtils.checkCursor(cur)) return null;
        final J item = getDataItem(cur, cur.getColumnIndex(column), _clazz, _defaultValue);
        cur.close();
        return item;
    }

    private <J> J getDataItem(Cursor _cur, int _columnIndex, Class<J> _clazz, final J _defaultValue) {
        try {
                if (Integer.class.equals(_clazz))
                    return _clazz.cast(_cur.getInt(_columnIndex));
                else if (Long.class.equals(_clazz))
                    return _clazz.cast(_cur.getLong(_columnIndex));
                else if (Boolean.class.equals(_clazz))
                    return _clazz.cast(_cur.getInt(_columnIndex) == 1);
            else if (String.class.equals(_clazz))
                return _clazz.cast(_cur.getString(_columnIndex));
            else
                return _defaultValue;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return _defaultValue;
    }

    public List<String> getAllInColumn(final String column) {
        return getStringList(column, null, null);
    }

    public int delete(final String selection, final String[] selectionArgs) {
        return mSQLiteDatabase.delete(getTableName(), selection, selectionArgs);
    }

    public int delete(final long _id) {
        return delete(SELECT_BY_ID, getArgsForSelectById(_id));
    }

    public int deleteAll() {
        return mSQLiteDatabase.delete(
                getTableName(),
                null,
                null);
    }

    public long insert(final T item) {
        ContentValues cv = convertToCV(item);
        return mSQLiteDatabase.insert(getTableName(), null, cv);
    }

    public long insertIgnoreConflict(final ContentValues _cv, final String _idColumnName) {
//        return mSQLiteDatabase.insertWithOnConflict(getTableName(), null, _cv, SQLiteDatabase.CONFLICT_IGNORE); // Not work correctly on android
        try {
            return mSQLiteDatabase.insertOrThrow(getTableName(), null, _cv);
        } catch (SQLException e) {
            e.printStackTrace();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append(_idColumnName);
            sql.append(" FROM ");
            sql.append(getTableName());
            sql.append(" WHERE ");

            Object[] bindArgs = new Object[_cv.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry: _cv.valueSet()) {
                sql.append((i > 0) ? " AND " : "");
                sql.append(entry.getKey());
                sql.append(" = ?");
                bindArgs[i++] = entry.getValue();
            }

            SQLiteStatement stmt = mSQLiteDatabase.compileStatement(sql.toString());
            for (i = 0; i < bindArgs.length; i++) {
                DatabaseUtils.bindObjectToProgram(stmt, i + 1, bindArgs[i]);
            }

            try {
                return stmt.simpleQueryForLong();
            } catch (SQLiteDoneException ex){
                ex.printStackTrace();
            } finally {
                stmt.close();
            }
            return -1;
        }
    }

    public long insertIgnoreConflict(final T item, final String _idColumnName) {
        ContentValues cv = convertToCV(item);
        return insertIgnoreConflict(cv, _idColumnName);
    }

    public synchronized long updateOrInsert(@Nullable final Long oldId, final T _item, final String _idColumnName) {
        ContentValues cv = convertToCV(_item);
        if(oldId != null){
            update(oldId, cv);
        }
        return insertIgnoreConflict(cv, _idColumnName);
    }

    public long updateOrInsert(@Nullable ContentValues _cv) {
        if (_cv == null) return -1;
        final Long id = _cv.getAsLong(FIELD_ID);
        if(id != null){
            update(id, _cv);
        }
        return insertIgnoreConflict(_cv, FIELD_ID);
    }

    public int update(final long oldId, final T item) {
        ContentValues cv = convertToCV(item);
        return update(oldId, cv);
    }

    protected int update(final String selection, final String[] selectionArgs, final T item) {
        ContentValues cv = convertToCV(item);
        return update(cv, selection, selectionArgs);
    }

    public int update(final long oldId, final ContentValues _cv) {
        return update(_cv, SELECT_BY_ID, getArgsForSelectById(oldId));
    }

    public int update(final ContentValues _cv, final String selection, final String[] selectionArgs) {
        try {
            return mSQLiteDatabase.update(getTableName(), _cv, selection, selectionArgs);
        } catch (SQLException e) {
            e.printStackTrace();
//            Log.e(DebugUtils.TAG_DEBUG, "Error updating:" + " Table " + getTableName()
//                    + "; class = " + item.getClass(), e);
            return -1;
        }
    }

    public boolean exists(final Long id) {
        return getCount(FIELD_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean exists(final T item) {
        List<T> itemList = getAll(null);
        return itemList.contains(item);
    }

    public void closeDb(){
        if (mSQLiteDatabase.isOpen()) {
            mSQLiteDatabase.close();
        }
    }

    public Cursor getAll(String[] _projection, String _selection, String[] _selectionArgs, String _orderBy, String _limit) {
        return mSQLiteDatabase.query(
                getTableName(),                    // The table name
                _projection,
                _selection,
                _selectionArgs,
                null,                               // GROUP BY clause
                null,
                _orderBy,
                _limit);
    }

    public Cursor get(String[] _projection, long _id, String _orderBy) {
        return mSQLiteDatabase.query(
                getTableName(),                     // The table name
                _projection,                        // A list of which columns to return
                SELECT_BY_ID,                       // SQL WHERE clause
                getArgsForSelectById(_id),          // selectionArgs to SQL WHERE clause
                null,                               // GROUP BY clause
                null,                               // SQL HAVING clause
                _orderBy);                          // SQL ORDER BY clause
    }

    private String[] getArgsForSelectById(long _id) {
        return new String[]{String.valueOf(_id)};
    }

    public int bulkInsertOrUpdate(ContentValues[] _values) {
        mSQLiteDatabase.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues cv : _values) {

                long rowId = updateOrInsert(cv);
                if (rowId > 0) returnCount++;
            }
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.w("db.bulkInsert(): ", ex);
        } finally {
            mSQLiteDatabase.endTransaction();
        }
        return returnCount;
    }

    public boolean isOpen(boolean _withWritableAccess) {
        return mSQLiteDatabase != null && mSQLiteDatabase.isOpen() && (!_withWritableAccess || !mSQLiteDatabase.isReadOnly());
    }
}

