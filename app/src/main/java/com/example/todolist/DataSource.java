package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID, DbHelper.COLUMN_TASK};

    public DataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createTask(String task) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_TASK, task);
        database.insert(DbHelper.TABLE_TODO, null, values);
    }

    public void deleteTask(long taskId) {
        database.delete(DbHelper.TABLE_TODO, DbHelper.COLUMN_ID + " = " + taskId, null);
    }

    public Cursor getAllTasks() {
        return database.query(DbHelper.TABLE_TODO, allColumns, null,
                null, null, null, null);
    }
}