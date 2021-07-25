package com.example.douin.user.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用户数据表
 * 数据库工具类
 *
 * @author 方昊
 */
public class RDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Rdo.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RContract.REntry.TABLE_NAME + " (" +
                    RContract.REntry._ID + " INTEGER PRIMARY KEY," +
                    RContract.REntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RContract.REntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    RContract.REntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RContract.REntry.TABLE_NAME;

    public RDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public boolean insert(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RContract.REntry.COLUMN_NAME_NAME, name);
        values.put(RContract.REntry.COLUMN_NAME_EMAIL, email);
        values.put(RContract.REntry.COLUMN_NAME_PASSWORD, password);
        long newRowId;
        newRowId = db.insert(
                RContract.REntry.TABLE_NAME,
                null,
                values
        );
        return newRowId != -1;
    }


    public String findPassword(String email) {
        SQLiteDatabase db = this.getWritableDatabase();


        String[] projection = {
                RContract.REntry.COLUMN_NAME_PASSWORD
        };

        String selection = RContract.REntry.COLUMN_NAME_EMAIL + " LIKE ?";
        String[] args = {email};
        String p = null;

        @SuppressLint("Recycle") Cursor c = db.query(
                RContract.REntry.TABLE_NAME,  //表名
                projection,                              // The columns to return
                selection,                                // The columns for the WHERE clause
                args,                         // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                             // 排序方式
        );
        if (c != null && c.moveToFirst()) {
            do {
                p = c.getString(c.getColumnIndex(RContract.REntry.COLUMN_NAME_PASSWORD));

            } while (c.moveToNext());
        }
        return p;
    }


}
