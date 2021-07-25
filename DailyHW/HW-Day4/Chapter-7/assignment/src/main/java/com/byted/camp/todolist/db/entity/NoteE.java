package com.byted.camp.todolist.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 与数据库交互的实体类
 * @author YDJSIR
 * @date 2021-07-18
 */
@Entity(tableName = "todoitems")
public class NoteE {
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "state")
    public int state;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "priority")
    public int priority;

}
