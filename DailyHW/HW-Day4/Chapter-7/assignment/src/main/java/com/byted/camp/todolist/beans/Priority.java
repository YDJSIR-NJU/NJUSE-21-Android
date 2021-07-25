package com.byted.camp.todolist.beans;

/**
 * 优先级状态
 * @author YDJSIR
 * @date 2021-07-18
 */
public enum Priority {
    TRIVIAL(0), NORMAL(1), IMPORTANT(2), EMERGENCY(3);

    public final int intValue;

    Priority(int intValue) {
        this.intValue = intValue;
    }

    public static Priority from(int intValue) {
        for (Priority priority : Priority.values()) {
            if (priority.intValue == intValue) {
                return priority;
            }
        }
        return NORMAL; // default
    }
}
