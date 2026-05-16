package com.github.knkydd.backend.tasktracker.bot.session.state;

public enum StateType {
    IDLE,
    WAITING_CATEGORY_TASK,
    WAITING_DESCRIPTION_TASK,
    WAITING_COMPLETED_TASK,
    WAITING_TASK_TO_DELETE
}
