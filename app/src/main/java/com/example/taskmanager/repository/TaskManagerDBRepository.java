package com.example.taskmanager.repository;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database2.TaskDBHelper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

public class TaskManagerDBRepository implements IRepositoryUser, IRepositoryTask {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static TaskManagerDBRepository sInstance;



    public TaskManagerDBRepository(Context context) {
        mContext = context.getApplicationContext();

        TaskDBHelper taskDBHelper = new TaskDBHelper (mContext);
        mDatabase = taskDBHelper.getWritableDatabase();
    }

    public static TaskManagerDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new TaskManagerDBRepository(context);

        return sInstance;
    }

    @Override
    public void insertTask(Task task) {

    }

    @Override
    public Task getTask(UUID id) {
        return null;
    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void deleteTask(Task task) {

    }

    @Override
    public List<Task> getTaskList() {
        return null;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public User getUser(UUID userId) {
        return null;
    }

    @Override
    public void insertUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public int getPosition(UUID userId) {
        return 0;
    }
}
