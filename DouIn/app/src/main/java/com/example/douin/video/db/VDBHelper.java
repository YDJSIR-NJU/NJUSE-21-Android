package com.example.douin.video.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.douin.util.network.VideoItem;

import java.util.ArrayList;

/**
 * 用户数据表
 * 数据库工具类
 *
 * @author 方昊
 */
public class VDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Vdo.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VContract.VEntry.TABLE_NAME + " (" +
                    VContract.VEntry._ID + " INTEGER PRIMARY KEY," +
                    VContract.VEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    VContract.VEntry.COLUMN_NAME_URI + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VContract.VEntry.TABLE_NAME;

    public VDBHelper(Context context) {
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


    public boolean insert(String email, String uri) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VContract.VEntry.COLUMN_NAME_EMAIL, email);
        values.put(VContract.VEntry.COLUMN_NAME_URI, uri);
        long newRowId;
        newRowId = db.insert(
                VContract.VEntry.TABLE_NAME,
                null,
                values
        );
        return newRowId != -1;
    }


    public ArrayList<String> getUri(String email) {
        ArrayList<String> videolist=new  ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();


        String[] projection = {
                VContract.VEntry.COLUMN_NAME_URI,
                VContract.VEntry._ID
        };

        String selection = VContract.VEntry.COLUMN_NAME_EMAIL + " LIKE ?";
        String[] args = {email};
        String sortOrder = VContract.VEntry._ID + " DESC";
        @SuppressLint("Recycle") Cursor c = db.query(
                VContract.VEntry.TABLE_NAME,  //表名
                projection,                              // The columns to return
                selection,                                // The columns for the WHERE clause
                args,                         // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                          // 排序方式
        );
        if (c != null && c.moveToFirst()) {
            do {
                videolist.add(c.getString(c.getColumnIndex(VContract.VEntry.COLUMN_NAME_URI)));

            } while (c.moveToNext());
        }
        return videolist;
    }


}
