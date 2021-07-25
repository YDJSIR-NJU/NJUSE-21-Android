package com.example.douin.user.db;

import android.provider.BaseColumns;

/**
 * 定义用户数据表的基本常量
 * @author 方昊
 */
public class RContract {
    private RContract() {

    }

    public static class REntry implements BaseColumns {
        public static final String TABLE_NAME = "Register";

        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";

    }

}
