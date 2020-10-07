package com.example.taskmanager.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TaskCursorWrapper  extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }
}
