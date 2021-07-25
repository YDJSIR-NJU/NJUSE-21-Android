package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * 定义SQL表结构与基本建表操作
 * @author YDJSIR
 * @date 2021-07-18
 */
public class TodoContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_DATE + " INTEGER," + // 保存UNIX时间戳，单位为毫秒
                    FeedEntry.COLUMN_NAME_STATE + " TEXT," +
                    FeedEntry.COLUMN_NAME_CONTENT + " TEXT," +
                    FeedEntry.COLUMN_NAME_PRIORITY + " INTEGER)"
            ;
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    private TodoContract() {
    }

    /**
     * 定义表内容的内部类
     */
    public static class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "todoitems";

        public static final String COLUMN_NAME_DATE = "date";

        public static final String COLUMN_NAME_STATE = "state";

        public static final String COLUMN_NAME_CONTENT = "content";

        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
}
