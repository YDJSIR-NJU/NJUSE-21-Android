package com.example.douin.video.db;

import android.provider.BaseColumns;

/**
 * 定义用户数据表的基本常量
 * @author 方昊
 */
public class VContract {
    private VContract() {

    }

    public static class VEntry implements BaseColumns {
        public static final String TABLE_NAME = "Register";

        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_URI = "uri";

    }

}
