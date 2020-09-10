package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository sInstance;
    private List<Task> mTaskList = new ArrayList<>();

    public static TaskRepository getInstance() {
        if (sInstance == null)
            sInstance = new TaskRepository();
        return sInstance;
    }

    public static void setInstance(TaskRepository instance) {
        sInstance = instance;
    }

    public List<Task> getTaskList() {
        return mTaskList;
    }

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }
}
