package com.byted.camp.todolist.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.db.entity.NoteE;

import java.util.List;

/**
 * @author YDJSIR
 * @date 2021-07-18
 */
@Dao
public interface NoteDao {

    // 默认按照id大小和优先级排序，假定id是从小到大生成的
    @Query("SELECT * FROM todoitems ORDER BY date, priority")
    List<Note> getAll();

    @Insert
    void insertAll(NoteE... noteES);

    @Update
    void update(NoteE noteE);

    @Delete
    void delete(NoteE noteE);

}
