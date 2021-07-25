package com.byted.camp.todolist.beans;

import androidx.room.PrimaryKey;

import com.byted.camp.todolist.db.entity.NoteE;

import java.util.Date;

/**
 * 待办项Beans类
 * @author YDJSIR
 * @date 2021-07-18
 */
public class Note {

    @PrimaryKey
    public final long id;
    private Date date;
    private State state;
    private String content;
    private Priority priority; // 优先级

    public Note(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    // 从实体类到beans类
    public Note(NoteE noteE){
        this.id = noteE.id;
        this.date = new Date(noteE.date); // UNIX时间戳转Date对象
        this.state = State.from(noteE.state);
        this.content = noteE.content;
        this.priority = Priority.from(noteE.priority);
    }

    // 直接构造
    public Note(long id, long date, int state, String content, int priority){
        this.id = id;
        this.date = new Date(date);
        this.content = content;
        this.state = State.from(state);
        this.priority = Priority.from(priority);
    }

}
