package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository implements IRepositoryTask {
    private static TaskRepository sInstance;
    private List<Task> mTaskList = new ArrayList<>();

    private TaskRepository() {
    }

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

    @Override
    public Task getTaskUser(UUID idUser) {
        return null;
    }

    @Override
    public File getPhotoFile(Task task) {
        return null;
    }

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }

    @Override
    public void insertTask (Task task){
        mTaskList.add(task);
    }

    @Override
    public Task getTask (UUID id){
        for (Task task: mTaskList) {
            if (task.getIdTask().equals(id))
                return task;
        }
        return null;
    }

    @Override
    public void updateTask (Task task){
        Task findTask = getTask(task.getIdTask());
        findTask.setTitleTask(task.getTitleTask());
        findTask.setStateTask(task.getStateTask());
        findTask.setDateTask(task.getDateTask());
        findTask.setDescription(task.getDescription());
    }

    @Override
    public void deleteTask (Task task){
        for (int i = 0; i < mTaskList.size(); i++) {
            if (mTaskList.get(i).getIdTask().equals(task.getIdTask())) {
                mTaskList.remove(i);
                return;
            }
        }
    }

    @Override
    public int getPosition (UUID taskId){
        for (int i = 0; i < mTaskList.size(); i++) {
            if (mTaskList.get(i).getIdTask().equals(taskId))
                return i;
        }

        return 0;

    }

}
