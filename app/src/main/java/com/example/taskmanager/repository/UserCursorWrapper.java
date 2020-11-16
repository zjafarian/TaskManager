package com.example.taskmanager.repository;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.taskmanager.model.User;
import com.example.taskmanager.database.TaskManagerSchema.UserTable.userCols;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser(){
        int id = getInt(getColumnIndex(userCols.id));
        UUID uuid = UUID.fromString(getString(getColumnIndex(userCols.userUUID)));
        String userName = getString(getColumnIndex(userCols.userName));
        String password = getString(getColumnIndex(userCols.password));
        String date = getString(getColumnIndex(userCols.dateRegister));
        return new User(userName,password);
    }
}
