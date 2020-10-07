package com.example.taskmanager.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database2.TaskDBHelper;
import com.example.taskmanager.database2.TaskManagerSchema;
import com.example.taskmanager.database2.TaskManagerSchema.TaskTable.taskCols;
import com.example.taskmanager.database2.TaskManagerSchema.UserTable.userCols;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskManagerDBRepository implements IRepositoryUser, IRepositoryTask {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static TaskManagerDBRepository sInstance;


    public TaskManagerDBRepository(Context context) {
        mContext = context.getApplicationContext();
        TaskDBHelper taskDBHelper = new TaskDBHelper(mContext);
        mDatabase = taskDBHelper.getWritableDatabase();
    }

    public static TaskManagerDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskManagerDBRepository(context);

        return sInstance;
    }

    @Override
    public void insertTask(Task task) {
        ContentValues values = getContentTaskValues(task);
        mDatabase.insert(TaskManagerSchema.TaskTable.NAME,null,values);
    }

    @Override
    public Task getTask(UUID id) {
        String where = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{id.toString()};

        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(where, whereArgs);

        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return null;

        try {
            taskCursorWrapper.moveToFirst();
            return taskCursorWrapper.getTask();
        } finally {
            taskCursorWrapper.close();
        }
    }

    @Override
    public void updateTask(Task task) {
        ContentValues values = getContentTaskValues(task);
        String whereClause = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{task.getIdTask().toString()};
        mDatabase.update(TaskManagerSchema.TaskTable.NAME,values,whereClause,whereArgs);
    }

    @Override
    public void deleteTask(Task task) {
        String whereClause = taskCols.taskUUID + " = ?";
        String[] whereArgs = new String[]{task.getIdTask().toString()};
        mDatabase.delete(TaskManagerSchema.TaskTable.NAME,whereClause,whereArgs);
    }

    @Override
    public List<Task> getTaskList() {
        List<Task> tasks = new ArrayList<>();
        TaskCursorWrapper taskCursorWrapper = queryTaskCursor(null, null);
        if (taskCursorWrapper == null || taskCursorWrapper.getCount() == 0)
            return tasks;

        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                tasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }

        } finally {
            taskCursorWrapper.close();
        }
        return tasks;
    }

    @Override
    public List<User> getUserList() {
        List<User> users = new ArrayList<>();
        UserCursorWrapper userCursorWrapper  = queryUserCursor(null, null);
        if (userCursorWrapper == null || userCursorWrapper.getCount() == 0)
            return users;

        try {
            userCursorWrapper.moveToFirst();
            while (!userCursorWrapper.isAfterLast()) {
                users.add(userCursorWrapper.getUser());

                userCursorWrapper.moveToNext();
            }

        } finally {
            userCursorWrapper.close();
        }
        return users;
    }

    @Override
    public User getUser(UUID userId) {
        String where = userCols.userUUID + " = ?";
        String[] whereArgs = new String[]{userId.toString()};

        UserCursorWrapper userCursorWrapper = queryUserCursor(where,whereArgs);


        if (userCursorWrapper == null || userCursorWrapper.getCount() == 0)
            return null;

        try {
            userCursorWrapper.moveToFirst();
            return userCursorWrapper.getUser();
        } finally {
            userCursorWrapper.close();
        }
    }

    @Override
    public void insertUser(User user) {
        ContentValues values = getContentUserValues(user);
        mDatabase.insert(TaskManagerSchema.UserTable.NAME,null,values);
    }

    @Override
    public void deleteUser(User user) {
        String whereClause = userCols.userUUID + " = ?";
        String[] whereArgs = new String[]{user.getIDUser().toString()};
        mDatabase.delete(TaskManagerSchema.UserTable.NAME,whereClause,whereArgs);
    }

    @Override
    public int getPosition(UUID userId) {
        List<User> users= getUserList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIDUser().equals(userId))
                return i;
        }
        return -1;
    }


    private TaskCursorWrapper queryTaskCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskManagerSchema.TaskTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        TaskCursorWrapper taskCursorWrapper = new TaskCursorWrapper(cursor);
        return taskCursorWrapper;
    }

    private UserCursorWrapper queryUserCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskManagerSchema.UserTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);
        UserCursorWrapper userCursorWrapper = new UserCursorWrapper(cursor);
        return userCursorWrapper;
    }


    private ContentValues getContentTaskValues(Task task ) {
        ContentValues values = new ContentValues();
        values.put(taskCols.taskUUID, task.getIdTask().toString());
        values.put(taskCols.userUUID,task.getIdUser().toString());
        values.put(taskCols.title, task.getTitleTask());
        values.put(taskCols.date, task.getDateTask().getTime());
        values.put(taskCols.description, task.getDescription());
        values.put(taskCols.state, task.getStateTask().toString());
        return values;
    }

    private ContentValues getContentUserValues(User user ) {
        ContentValues values = new ContentValues();
        values.put(userCols.userUUID,user.getIDUser().toString());
        values.put(userCols.userName,user.getUsername());
        values.put(userCols.password,user.getPassword());
        return values;
    }
}
