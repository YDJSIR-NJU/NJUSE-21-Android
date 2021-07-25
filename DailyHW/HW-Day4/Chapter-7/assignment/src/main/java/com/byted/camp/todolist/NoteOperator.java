package com.byted.camp.todolist;

import com.byted.camp.todolist.beans.Note;

/**
 * @author zhongshan
 * @date 2020-04-19.
 */
public interface NoteOperator {

    void deleteNote(Note note);

    void updateNote(Note note);
}
