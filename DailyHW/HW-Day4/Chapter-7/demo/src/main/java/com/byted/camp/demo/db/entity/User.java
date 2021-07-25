package com.byted.camp.demo.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author zhongshan
 * 2020-04-19.
 */
@Entity(tableName = "user")
public class User {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

//
//    @ColumnInfo(name = "age")
//    public String age;

}
