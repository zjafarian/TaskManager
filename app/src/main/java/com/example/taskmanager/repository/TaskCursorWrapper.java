package com.example.taskmanager.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.database.TaskManagerSchema.TaskTable.taskCols;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper  extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask(){
        int idTask = getInt(getColumnIndex(taskCols.idTask));
        int idUser = getInt(getColumnIndex(taskCols.idUser));
        UUID uuidTask = UUID.fromString(getString(getColumnIndex(taskCols.taskUUID)));
        UUID uuidUser = UUID.fromString(getString(getColumnIndex(taskCols.userUUID)));
        String title = getString(getColumnIndex(taskCols.title));
        String description = getString(getColumnIndex(taskCols.description));
        Date date = new Date(getLong(getColumnIndex(taskCols.date)));
        State state = State.valueOf(getString(getColumnIndex(taskCols.state)));
        return new Task(title,description,state,uuidUser);
    }
}
