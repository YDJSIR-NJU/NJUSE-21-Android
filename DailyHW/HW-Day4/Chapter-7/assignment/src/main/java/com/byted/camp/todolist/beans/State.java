package com.byted.camp.todolist.beans;

/**
 * 已读状态
 * @author YDJSIR
 * @date 2021-07-18
 */
public enum State {
    TODO(0), DONE(1);

    public final int intValue;

    State(int intValue) {
        this.intValue = intValue;
    }

    public static State from(int intValue) {
        for (State state : State.values()) {
            if (state.intValue == intValue) {
                return state;
            }
        }
        return TODO; // default
    }
}
