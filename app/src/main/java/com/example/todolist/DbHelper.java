package com.example.todolist;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";

    // Database creation SQL statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_TODO + "( " + COLUMN_ID
                    + " integer primary key autoincrement, " + COLUMN_TASK
                    + " text not null);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used, but you could handle upgrades here
    }
}

